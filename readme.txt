一、文件介绍
#server：Python后端(运行main会自动创建数据库)
#QQ9：安卓前端
#apk：安卓手机可以直接安装使用，成功运行程序后，手机可以使用该软件和模拟器相互聊天

二、项目介绍
项目组成：安卓前端+Python后端+MySQL数据库+云端AI模型
项目介绍：
前端：注册界面、登录界面、消息列表界面、聊天界面
后端：数据库服务、聊天服务、注册服务、登录服务
数据库：目前仅包含用户表
功能介绍：主要功能只有两个：注册登录和实时聊天（聊天还有一个AI自动回复功能）
//以后有时间了，会更新好友列表、自定义头像、历史消息等功能，给数据库增加聊天表，写一个Web可视化数据库管理（CRUD）等等

三、使用方法
1.Python环境（Python版本为3.11，下面的为conda list显示内容，按照名字和版本号下载所需要的依赖）
#
# Name                    		Version                   Build  Channel
annotated-types     		0.7.0                   	pypi_0    pypi
anyio                     			4.9.0                    	pypi_0    pypi
bcrypt                    			4.3.0                   	 pypi_0    pypi
blinker                   			1.9.0                    	pypi_0    pypi
bzip2                     			1.0.8                		h2bbff1b_6
ca-certificates           		2025.2.25            	haa95532_0
certifi                  		 	2025.1.31                pypi_0    pypi
charset-normalizer        		3.4.1                    	pypi_0    pypi
click                     			8.1.8                    	pypi_0    pypi
colorama                	 	 	0.4.6                    	pypi_0    pypi
distro                    			1.9.0                    	pypi_0    pypi
fastapi                   			0.115.0                  	pypi_0    pypi
flask                     			3.1.1                   	pypi_0    pypi
flask-cors               		 	6.0.0                    	pypi_0    pypi
h11                       			0.14.0                   	pypi_0    pypi
httpcore                  			1.0.8                    	pypi_0    pypi
httpx                     			0.28.1                   	pypi_0    pypi
idna                      			3.10                     	pypi_0    pypi
itsdangerous              		2.2.0                    	pypi_0    pypi
jinja2                    			3.1.6                    	pypi_0    pypi
jiter                     			0.9.0                    	pypi_0    pypi
libffi                    			3.4.4                		hd77b12b_1
markupsafe                		3.0.2                    	pypi_0    pypi
mysql-connector-python    	9.3.0                    	pypi_0    pypi
openai                    			1.73.0                   	pypi_0    pypi
openssl                   			1.1.1w               	h2bbff1b_0
pip                       			25.0            		py311haa95532_0
pydantic                  		2.11.3                   	pypi_0    pypi
pydantic-core             		2.33.1                   	pypi_0    pypi
python                    			3.11.0               	h966fe2a_3
requests                  			2.32.3                   	pypi_0    pypi
setuptools                		75.8.0          		py311haa95532_0
sniffio                  	 		1.3.1                    	pypi_0    pypi
sqlite                    			3.45.3               	h2bbff1b_0
starlette                 			0.38.6                   	pypi_0    pypi
tk                        			8.6.14               	h0416ee5_0
tqdm                      			4.67.1                   	pypi_0    pypi
typing-extensions         		4.13.2                   	pypi_0    pypi
typing-inspection         		0.4.0                    	pypi_0    pypi
tzdata                    			2025a               	h04d1e81_0
urllib3                  			2.4.0                    	pypi_0    pypi
uvicorn                   			0.34.1                   	pypi_0    pypi
vc                        			14.42                	haa95532_4
vs2015_runtime            		14.42.34433          	he0abc0d_4
werkzeug                  		3.1.3                    	pypi_0    pypi
wheel                     			0.45.1          		py311haa95532_0
xz                        			5.6.4                		h4754444_1
zlib                      			1.2.13               	h8cc25b3_1

2.电脑连接手机热点，cmd输入ipconfig，记住连接的热点IP地址
3.修改IP
	server/chat_server.py 第86行 修改为你自己的IP
	server/register_server.py 第10行 修改为你自己的IP	
	用android studio打开qq9项目，ChatAcivity.java 第150行 修改为你自己的IP，loginActivity.java 第53行修改为你自己的IP
4.修改数据库
	server/db_manager.py 第5行，将self后面的属性改为你自己的数据库的内容
5.修改密钥
	server/chat_server.py 第8行，将密钥修改为你自己的（网站是硅基流动：https://siliconflow.cn/）


//程序演示视频：https://www.bilibili.com/video/BV1aXjJziEsk/?spm_id_from=333.1387.homepage.video_card.click
