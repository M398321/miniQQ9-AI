from flask import Flask, request, jsonify
from flask_cors import CORS
import bcrypt
import mysql.connector

app = Flask(__name__)
CORS(app)  # 启用跨域支持
db_manager = None  # 全局数据库连接实例

def init_api(db_manager_instance):
    """初始化API服务器，注入数据库连接"""
    global db_manager
    db_manager = db_manager_instance
    if not db_manager or not db_manager.connection or not db_manager.connection.is_connected():
        raise Exception("API服务器数据库连接未正确初始化")

@app.route('/api/login', methods=['POST'])
def login():
    # 检查数据库连接是否已注入
    if not db_manager:
        return jsonify({'success': False, 'message': '服务器未初始化'}), 503

    # 检查请求是否为JSON格式
    if not request.is_json:
        return jsonify({'success': False, 'message': '请求必须为JSON格式'}), 415

    data = request.get_json()
    username = data.get('username')
    password = data.get('password')

    if not all([username, password]):
        return jsonify({'success': False, 'message': '用户名和密码为必填项'}), 400

    try:
        user = db_manager.get_user_by_username(username)
        if not user:
            return jsonify({'success': False, 'message': '用户不存在'}), 401

        # 验证密码（数据库中password为bcrypt哈希值）
        stored_hash = user['password'].encode('utf-8')
        input_password = password.encode('utf-8')
        if bcrypt.checkpw(input_password, stored_hash):
            return jsonify({
                'success': True,
                'message': '登录成功',
                'user_id': user['id']
            }), 200
        else:
            return jsonify({'success': False, 'message': '密码错误'}), 401

    except mysql.connector.Error as e:
        print(f"数据库错误: {e}")
        return jsonify({'success': False, 'message': '数据库连接失败'}), 503
    except Exception as e:
        print(f"服务器错误: {e}")
        return jsonify({'success': False, 'message': '服务器内部错误'}), 500

def start_api_server():
    """启动Flask API服务器"""
    print("API服务器已启动，监听端口5000...")
    app.run(host='0.0.0.0', port=5000, debug=False)