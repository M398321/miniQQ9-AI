import threading
from chat_server import start_server
from register_server import RegisterMethodServer
from db_manager import DBManager
from api_server import start_api_server, init_api  # 导入修改后的API初始化函数

def main():
    # 初始化数据库连接（单例模式，共享连接）
    db_manager = DBManager()
    if not db_manager.connect():
        print("数据库连接失败，退出程序")
        return

    db_manager.create_table()  # 创建用户表（若不存在）

    # 启动注册服务器（注入共享的db_manager）
    register_server = RegisterMethodServer(db_manager)
    threading.Thread(target=register_server.start, daemon=True, name="RegisterServer").start()

    # 初始化API服务器并注入数据库连接
    init_api(db_manager)
    threading.Thread(target=start_api_server, daemon=True, name="APIServer").start()

    # 启动聊天服务器（假设chat_server无需数据库连接，若需要可传入db_manager）
    print("启动聊天服务器...")
    threading.Thread(target=start_server, daemon=True, name="ChatServer").start()

    # 保持主进程运行
    try:
        while True:
            pass
    except KeyboardInterrupt:
        print("\n接收到退出信号，关闭数据库连接...")
        db_manager.close()
        print("主程序退出")

if __name__ == "__main__":
    main()