import mysql.connector
from mysql.connector import Error

class DBManager:
    def __init__(self, host='localhost', user='root', password='735378', database='chat_db'):
        self.host = host
        self.user = user
        self.password = password
        self.database = database
        self.connection = None  # 数据库连接对象

    def connect(self):
        """创建数据库连接并返回是否成功"""
        try:
            self.connection = mysql.connector.connect(
                host=self.host,
                user=self.user,
                password=self.password,
                database=self.database,
                charset='utf8mb4'  # 添加字符集设置
            )
            print("成功连接到MySQL数据库")
            return True
        except Error as e:
            print(f"数据库连接失败: {e}")
            return False

    def close(self):
        """安全关闭数据库连接"""
        if self.connection and self.connection.is_connected():
            self.connection.close()
            print("数据库连接已关闭")
            self.connection = None  # 置空连接

    def create_table(self):
        """创建用户表（带连接状态检查）"""
        if not self.connection:
            print("无法创建表：数据库连接未建立")
            return

        create_table_sql = """
        CREATE TABLE IF NOT EXISTS users (
            id INT AUTO_INCREMENT PRIMARY KEY,
            username VARCHAR(50) UNIQUE NOT NULL,
            password VARCHAR(60) NOT NULL, 
            email VARCHAR(100) NOT NULL,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
        """
        cursor = None
        try:
            cursor = self.connection.cursor()
            cursor.execute(create_table_sql)
            self.connection.commit()
            print("用户表已创建")
        except Error as e:
            print(f"创建表失败: {e}")
            self.connection.rollback()
        finally:
            if cursor:  # 直接关闭游标，无需检查是否已关闭
                cursor.close()

    def save_user(self, username, hashed_password, email):
        """存储用户注册信息（接收已加密的密码哈希）"""
        if not all([username, hashed_password, email]):
            print("参数缺失：用户名、密码、邮箱均为必填项")
            return False

        insert_sql = """
        INSERT INTO users (username, password, email)
        VALUES (%s, %s, %s)
        """
        cursor = None
        try:
            cursor = self.connection.cursor()
            cursor.execute(insert_sql, (username, hashed_password, email))
            self.connection.commit()
            print(f"用户 {username} 已注册")
            return True
        except Error as e:
            print(f"注册失败: {e}")
            self.connection.rollback()
            return False
        finally:
            if cursor:  # 直接关闭游标
                cursor.close()

    def check_username_exists(self, username):
        """检查用户名是否存在"""
        if not username:
            print("用户名不能为空")
            return True

        select_sql = "SELECT COUNT(*) FROM users WHERE username = %s"
        cursor = None
        try:
            cursor = self.connection.cursor()
            cursor.execute(select_sql, (username,))
            count = cursor.fetchone()[0]
            return count > 0
        except Error as e:
            print(f"查询失败: {e}")
            return True
        finally:
            if cursor:  # 直接关闭游标
                cursor.close()

    def get_user_by_username(self, username):
        """通过用户名获取用户信息（返回字典）"""
        if not username:
            print("用户名不能为空")
            return None

        select_sql = "SELECT id, username, password FROM users WHERE username = %s"
        cursor = None
        try:
            cursor = self.connection.cursor(dictionary=True)
            cursor.execute(select_sql, (username,))
            return cursor.fetchone()
        except Error as e:
            print(f"查询用户失败: {e}")
            return None
        finally:
            if cursor:  # 直接关闭游标
                cursor.close()