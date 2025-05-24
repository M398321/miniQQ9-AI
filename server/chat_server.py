import socket
import threading
import json
from db_manager import DBManager #数据库模块（仅用于注册，聊天逻辑无需数据库）
#导入OpenAi模块
from openai import OpenAI

api_key="your_api_key"  #填写你自己的密钥
base_url="https://api.siliconflow.cn/v1/"
client  = OpenAI(api_key=api_key,base_url=base_url)
# 用于保存完整对话的变量
#conversation_history = []  #选择的AI模型没有上下文记忆功能


clients = {}  # {username: (client_socket, address)}

# 全局变量保存自动回复状态
auto_reply_enabled = False  #不能使用全局变量，一个开启，全部开启


def handle_client(client_socket, client_address):
    global auto_reply_enabled
    username = None
    try:
        # 接收用户名（必须以\n结尾，安卓客户端使用readLine()）
        username = client_socket.recv(1024).decode('utf-8').strip()
        if not username:
            return

        # 校验用户名（不允许包含:）
        if ":" in username:
            client_socket.send("用户名不能包含冒号\n".encode('utf-8'))
            client_socket.close()
            return


        # 通知新用户加入（带换行符）
        broadcast(f"系统: {username} 加入了聊天", None)
        clients[username] = (client_socket, client_address)
        print(f"新用户 {username} 来自: {client_address}")

        while True:
            # 接收消息（带换行符）
            message = client_socket.recv(1024).decode('utf-8').strip()
            if not message:  # 允许发送空消息
                continue
            print(f"收到来自 {username} 的消息: {message}")
            # 尝试解析JSON消息
            try:
                json_message = json.loads(message)
                # 处理JSON命令
                handle_json_command(username, json_message)
            # 普通消息处理
            # 广播消息（带换行符）
            except json.JSONDecodeError:
                broadcast(f"{username}:{message}", username)
                if auto_reply_enabled:
                    auto_reply(username,ai_handle(message))


    except (socket.error, ConnectionResetError) as e:
        print(f"处理客户端 {username} 时出错: {e}")
    finally:
        if username and username in clients:
            del clients[username]
            client_socket.close()
            broadcast(f"系统: {username} 离开了聊天", None)
            print(f"{username} 连接已关闭")

def broadcast(message, sender_username):
    clients_copy = list(clients.items())  # 创建副本
    for username, (client_socket, _) in clients_copy:
        if sender_username is None or username != sender_username:
            try:
                # 确保消息以\n结尾，适配安卓客户端的readLine()
                client_socket.send((message + "\n").encode('utf-8'))
            except (socket.error, ConnectionResetError):
                if username in clients:
                    del clients[username]
                    client_socket.close()
                    print(f"已移除断开的客户端: {username}")

def start_server():
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  #第一个参数表示使用IPv4地址族，第二个参数表示使用TCP协议
    server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)  # 添加端口复用选项
    server.bind(('192.168.43.97', 8888))    #指定服务器在哪个端口和地址监听客户端连接
    server.listen(5)    #将socket转为监听模，准别接受客户端连接(参数5，表示等待连接的最大队列长度)。 #启动监听后，服务器会阻塞在accpet()方法，等待客户端连接
    print("服务器已启动，等待连接")

    try:
        while True:
            client_socket, client_address = server.accept()     #client_socket:客户端的socket对象，client_address:客户端的地址元组（如 ('192.168.1.100', 54321)）
            client_thread = threading.Thread(target=handle_client, args=(client_socket, client_address))    #创建线程处理客户端
            #threading.Thread()创建新线程 参数1：target=handle_client 指定线程要执行的函数 参数2：args=(client_socket,client_address)传递给handle_client函数的参数(客户端套接字和地址)s
            client_thread.daemon = True #设置守护线程（若主线程结束，守护线程会被强行终止）
            client_thread.start()
            print(f"新连接来自: {client_address}")
    except KeyboardInterrupt:
        print("服务器关闭")
    finally:
        for username, (client_socket, _) in list(clients.items()):
            client_socket.close()
            del clients[username]
        server.close()


def handle_json_command(username, json_message):
    """处理JSON格式的命令消息"""
    global auto_reply_enabled
    message_type = json_message.get('type')

    if message_type == 'auto_reply':
        # 处理自动回复命令
        enabled = json_message.get('enabled', False)
        auto_reply_enabled = enabled
        print(f"自动回复已{'开启' if enabled else '关闭'}")


def auto_reply(username, message):
    """发送自动回复消息"""
    if username in clients:
        client_socket, _ = clients[username]
        try:
            client_socket.send(f"AI助手:{message}\n".encode('utf-8'))
        except (socket.error, ConnectionResetError):
            print(f"发送自动回复给 {username} 失败")

def send_json_message(username, json_data):
    """向指定用户发送JSON格式消息"""
    if username in clients:
        client_socket, _ = clients[username]
        try:
            message = json.dumps(json_data) + "\n"
            client_socket.send(message.encode('utf-8'))
        except (socket.error, ConnectionResetError):
            print(f"发送JSON消息给 {username} 失败")

def ai_handle(message):
    response = client.chat.completions.create(
        model="Qwen/Qwen2.5-7B-Instruct",
        messages=[
            # {"role":"system","content":"我是系统"},
            {"role": "user", "content": message}
        ],
        max_tokens=4096,
        stream=True  # 流式输出
    )

    # 用于保存本次回复的完整内容
    full_response = ""
    last_was_newline = False  # 跟踪上一个字符是否是换行符

    for chunk in response:
        content = chunk.choices[0].delta.content
        if content is None:
            continue
        # 处理每个字符，合并连续的换行符
        for char in content:
            if char == '\n':
                # 如果上一个字符已经是换行符，则跳过当前换行符
                if last_was_newline:
                    continue
                last_was_newline = True
            else:
                last_was_newline = False
            print(char, end='', flush=True)  # 立即刷新输出
            full_response += char  # 这样就保存好了
    print('')  # 输出完回复之后换行
    return full_response


if __name__ == "__main__":
    start_server()