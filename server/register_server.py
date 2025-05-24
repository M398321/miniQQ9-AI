import socket
import threading
import bcrypt
from db_manager import DBManager

class RegisterMethodServer:
    def __init__(self, db_manager):
        self.db_manager = db_manager
        self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server.bind(('192.168.43.97', 8889))
        self.server.listen(5)
        print("注册服务器已启动，监听端口8889")

    def start(self):
        while True:
            client_socket, address = self.server.accept()
            print(f"接受到注册请求来自:{address}")
            threading.Thread(target=self.handle_register, args=(client_socket,), daemon=True).start()

    def handle_register(self, client_socket):
        try:
            data = client_socket.recv(1024).decode('utf-8').strip()
            if not data:
                client_socket.send("注册数据为空\n".encode('utf-8'))
                return

            username, password, email = data.split('|')  # 假设格式为 username|password|email

            # 校验用户名是否存在
            if self.db_manager.check_username_exists(username):
                client_socket.send("用户名已存在\n".encode('utf-8'))
                return

            # 加密密码（关键修复步骤）
            password_bytes = password.encode('utf-8')
            hashed_password = bcrypt.hashpw(password_bytes, bcrypt.gensalt())
            hashed_password_str = hashed_password.decode('utf-8')

            # 存储加密后的密码
            if self.db_manager.save_user(username, hashed_password_str, email):
                client_socket.send("注册成功\n".encode('utf-8'))
            else:
                client_socket.send("注册失败\n".encode('utf-8'))

        except Exception as e:
            print(f"注册处理失败:{e}")
            client_socket.send("注册失败，请重试\n".encode('utf-8'))
        finally:
            client_socket.close()