package com.example.myapplication;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class loginActivity extends AppCompatActivity{
    private EditText accounted; // 编辑框用于输入账号
    private EditText passworded; // 编辑框用于输入密码
    private Button loginBtned; // 登录按钮
    private OkHttpClient client;

    //private static final String API_URL = "http://你的服务器IP:5000/api/login"; // 修改为实际IP
    private static final String API_URL = "http://192.168.43.97:5000/api/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        accounted = findViewById(R.id.account); // 初始化编辑框
        passworded = findViewById(R.id.password); // 初始化编辑框
        loginBtned = findViewById(R.id.loginBtn); // 初始化按钮
        client = new OkHttpClient();

        // 输入框监听：自动禁用/启用按钮
        accounted.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputValidity();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        passworded.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputValidity();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 登录按钮点击事件
        loginBtned.setOnClickListener(v -> {
            String username = accounted.getText().toString().trim();
            String password = passworded.getText().toString().trim();
            login(username, password);
        });


        ImageView imgregister=(ImageView) findViewById(R.id.register);
        imgregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(loginActivity.this, RegisterMySQLActivity.class);
                startActivity(intent);
            }
        });
    }


    //SQLite存储的验证方法
    private boolean validateLogin()
    {
        String username=accounted.getText().toString();
        String paswd=passworded.getText().toString(); // 获取输入的密码
        UserDatabase userDatabase=new UserDatabase(loginActivity.this);
        SQLiteDatabase sqLiteDatabase=userDatabase.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from user where username" +
                "=? and paswd=?",new String[]{username,paswd});
        if(cursor.getCount()>0)return true;else return false;
    }

    private void checkInputValidity() {
        String username = accounted.getText().toString().trim();
        String password = passworded.getText().toString().trim();
        loginBtned.setEnabled(!username.isEmpty() && !password.isEmpty());
    }

    private void login(String username, String password) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", username);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            showToast("请求格式错误");
            return;
        }

        Request request = new Request.Builder()
                .url(API_URL)
                .post(RequestBody.create(requestBody.toString(), MediaType.parse("application/json")))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> showToast(getString(R.string.error_network)));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject json = new JSONObject(responseData);
                        if (json.getBoolean("success")) {
                            // 登录成功，跳转主界面
                            runOnUiThread(() -> {
                                showToast(getString(R.string.success_login));
                                startActivity(new Intent(loginActivity.this, SecondActivity.class));
                                finish();
                            });
                        } else {
                            String message = json.getString("message");
                            runOnUiThread(() -> showToast(getString(R.string.error_login_failed, message)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> showToast("解析响应失败"));
                    }
                } else {
                    runOnUiThread(() -> showToast("登录失败：" + response.code()));
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}




