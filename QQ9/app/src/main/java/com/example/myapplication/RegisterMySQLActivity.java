package com.example.myapplication;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import android.Manifest;

public class RegisterMySQLActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText username,account,paswd,email;
    private Button register_btn,show_btn;
    private ProgressDialog progressDialog;
    private static final int PERMISSION_INTERNET = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mysql_register);

        //初始化组件
        init();
        // 申请网络权限（Android 6.0+ 需要动态权限）
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PERMISSION_INTERNET);
        } else {
            initListener();
        }

        // 解决 Android 10+ 网络请求限制（仅测试环境使用，正式环境建议使用 OkHttp）
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    // 必须重写 onClick 方法
@Override
public void onClick(View view) {        //使用onClick()必须在类定义中加上implements View.OnClickListener
    if (view.getId() == R.id.register) {
        // 处理注册按钮点击事件
        String usernameR = username.getText().toString().trim();
        String passwordR = paswd.getText().toString().trim();
        String emailR = email.getText().toString().trim();
        // 调用注册逻辑
        new Thread(() -> registerUser(usernameR, passwordR, emailR)).start();
    }
}

public void register(String username, String password, String email) {
    new Thread(() -> {
        try {
            Socket socket = new Socket("192.168.43.97", 8889);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 发送注册信息（格式：username|password|email）
            out.println(username + "|" + password + "|" + email);

            // 接收注册结果
            String result = in.readLine();
            runOnUiThread(() -> {
                if ("注册成功".equals(result)) {
                    Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "注册失败: " + result, Toast.LENGTH_SHORT).show();
                }
            });

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }).start();
}
    private void init()
    {
        username=(EditText) findViewById(R.id.username);
        account=(EditText) findViewById(R.id.account);
        paswd=(EditText) findViewById(R.id.paswd);
        email=(EditText) findViewById(R.id.email);
        register_btn=(Button) findViewById(R.id.register);
        show_btn=(Button) findViewById(R.id.show);
        progressDialog = new ProgressDialog(this);
        //对按钮添加监听
        register_btn.setOnClickListener(this);
        show_btn.setOnClickListener(this);
    }

    private void initListener() {
        register_btn.setOnClickListener(v -> {
            String Rusername = username.getText().toString().trim();
            String Rpassword = paswd.getText().toString().trim();
            String Remail = email.getText().toString().trim();

            if (TextUtils.isEmpty(Rusername) || TextUtils.isEmpty(Rpassword) || TextUtils.isEmpty(Remail)) {
                Toast.makeText(RegisterMySQLActivity.this, "请填写完整注册信息", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.setMessage("正在注册...");
            progressDialog.show();

            new Thread(() -> registerUser(Rusername, Rpassword, Remail)).start();
        });
    }

    private void registerUser(String username, String password, String email) {
        String serverIp = "192.168.43.97"; // 替换为你的服务器IP
        int registerPort = 8889; // 注册服务端口（需与服务端一致）

        try (Socket socket = new Socket(serverIp, registerPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 发送注册信息（格式：username|password|email）
            String request = username + "|" + password + "|" + email;
            out.println(request);

            // 接收注册结果
            String response = in.readLine();

            runOnUiThread(() -> {
                progressDialog.dismiss();
                if ("注册成功".equals(response)) {
                    Toast.makeText(RegisterMySQLActivity.this, "注册成功！请登录", Toast.LENGTH_SHORT).show();
                    finish(); // 注册成功后返回登录界面
                } else {
                    Toast.makeText(RegisterMySQLActivity.this, "注册失败：" + response, Toast.LENGTH_SHORT).show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            runOnUiThread(() -> {
                progressDialog.dismiss();
                Toast.makeText(RegisterMySQLActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_INTERNET) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initListener();
            } else {
                Toast.makeText(this, "需要网络权限才能注册", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
