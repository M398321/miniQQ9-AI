package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import de.hdodenhof.circleimageview.BuildConfig;
import de.hdodenhof.circleimageview.CircleImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

public class ChatActivity extends AppCompatActivity {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Handler handler;
    private Button sendButton;
    private EditText messageEditText;
    private ListView listView;
    private ChatAdapter adapter; //非静态适配器
    private LinkedList<ChatData> myData;
    private ToggleButton AIBtn;
    //标志位
    private boolean isConnected=false;  //判断是否连接
    private boolean isAutoReplyEnabled = false; //判断AI功能是否打开

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //先确定是哪个页面，再绑定组件ID
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        listView=findViewById(R.id.listview_chat);
        sendButton=(Button)findViewById(R.id.chatSendBtn);
        messageEditText=(EditText)findViewById(R.id.messageInput);
        AIBtn=(ToggleButton) findViewById(R.id.aiBtn);


        //获取登录账号的用户名
        Intent intent = getIntent();
        String username = intent.getStringExtra("username2");

        //初始化消息列表(后面可以来放近一个月的历史消息)
        myData = new LinkedList<>();//ChatData是自定义的类
        for(int i = 0;i < 4;i++){
            if(i % 2 == 0){
                myData.add(new ChatData(R.drawable.xp," "+"在吗"+" ",0));
            }
            else{
                myData.add(new ChatData(R.drawable.miku," "+"在"+" ",1));
            }
        }

        //初始化handler,在connectToServer中使用到了
        handler = new Handler(Looper.getMainLooper());
        //连接服务器
        new Thread(this::connectToServer).start();

        //连接完服务器再发送才有用,设置延迟执行
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // 延迟后执行的代码（如更新UI）
            sendMessage(username);
        }, 200); // 延迟200毫秒


        //初始化ChatAdapter并设置给ListView
        adapter = new ChatAdapter(myData, this);
        listView.setAdapter(adapter);


        //监听发送按钮
        // 发送消息按钮点击事件
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString().trim();
               // if (!message.isEmpty()) {
                    sendMessage(message);
                    messageEditText.setText("");
                if (message.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "消息不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                    if(isConnected) {
                        myData.add(new ChatData(R.drawable.miku, " " + message + " ", 1));
                        adapter.notifyDataSetChanged();
                        listView.setSelection(myData.size() - 1);
                    }else{
                        myData.add(new ChatData(R.drawable.miku, " " + message+"(此为离线消息)"+ " ", 1));
                    }
                // **关键修改：刷新适配器**
                adapter.notifyDataSetChanged();
                // 可选：将列表定位到最后一条消息（提升用户体验）
                listView.setSelection(myData.size() - 1);   //这一行我必须加，不加就不显示
            }
        });

        // 设置按钮状态变化监听器
        AIBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sendAutoReplyStatus(isChecked);
            isAutoReplyEnabled = isChecked;  // 更新状态变量
            // 显示状态提示
            String status = isChecked ? "AI自动回复已开启" : "AI自动回复已关闭";
            Toast.makeText(ChatActivity.this, status, Toast.LENGTH_SHORT).show();
        });

        ListView listView = findViewById(R.id.listview_chat);
        listView.setAdapter(new ChatAdapter(myData,this));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ChatActivity.this, "你点击了我！", Toast.LENGTH_SHORT).show();
            }
        });

        //通过代码设置顶部状态栏颜色
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#39C5BB"));
    }

    private void connectToServer() {
        try {
            // 连接到服务器
            socket = new Socket("192.168.43.97", 8888);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 更新UI显示已连接
            handler.post(() -> showSendResult("已连接到服务器"));

            // 开始接收消息
            receiveMessages();

            //更新连接标志位状态
            isConnected=true;
        } catch (IOException e) {
            e.printStackTrace();
            handler.post(() -> showSendResult("连接服务器失败: " + e.getMessage()));

            //更新连接标志位状态
            isConnected=false;
        }
    }

    private void sendMessage(String message) {
        if (out != null) {
            // 发送消息到服务器
            new Thread(() -> out.println(message)).start();
        }
    }

    private void updateUI(String message) {
        myData.add(new ChatData(R.drawable.xp," "+message+" ",0));
        adapter.notifyDataSetChanged();
        listView.setSelection(myData.size() - 1);
    }

    private void showSendResult(String message) {
        Toast.makeText(ChatActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void receiveMessages() {
        new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {     //in通常是指从Socket中获取的输入流(如BufferedReader),用于读取服务端发送的数据
                    final String receivedMessage = message;
                    handler.post(() -> updateUI(receivedMessage));
                }
            } catch (IOException e) {
                e.printStackTrace();
                handler.post(() -> showSendResult("与服务器断开连接"));
                //更新连接标志位状态
                isConnected=false;
            } finally {
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                    if (socket != null) socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendAutoReplyStatus(boolean isEnabled) {
        try {
            // 构建JSON消息
            JSONObject message = new JSONObject();
            message.put("type", "auto_reply");
            message.put("enabled", isEnabled);

            // 发送消息
            sendMessage(String.valueOf(message));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
