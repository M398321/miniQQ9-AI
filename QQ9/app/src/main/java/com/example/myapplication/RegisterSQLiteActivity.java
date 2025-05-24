package com.example.myapplication;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

public class RegisterSQLiteActivity extends AppCompatActivity implements View.OnClickListener{
    private SQLiteDatabase sqLiteDatabase;

    private EditText username,paswd,age,telephone,address;
    private Button register_btn,show_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sqlite_register);
        init();
    }
    //组件初始化方法
    private void init()
    {
        username=(EditText) findViewById(R.id.username);
        paswd=(EditText) findViewById(R.id.paswd);
        age=(EditText) findViewById(R.id.age);
        telephone=(EditText) findViewById(R.id.telephone);
        address=(EditText)findViewById(R.id.address);
        register_btn=(Button) findViewById(R.id.register);
        show_btn=(Button) findViewById(R.id.show);
        //对按钮添加监听
        register_btn.setOnClickListener(this);
        show_btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view)
    {
        int viewId = view.getId();
        UserDatabase user = new UserDatabase(RegisterSQLiteActivity.this);
        if (viewId == R.id.register) {
            // 获取用户的用户名和密码
            int age_int;
            String name_str = username.getText().toString();
            String paswd_str = paswd.getText().toString();
            String age_str = age.getText().toString();
            //age类型转换
            try {
                 age_int = Integer.parseInt(age_str);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "请输入有效的年龄数字", Toast.LENGTH_SHORT).show();
                return; // 终止操作
            }
            String telephone_str = telephone.getText().toString();
            String address_str=address.getText().toString();
            //调用数据库操作类的插入方法
            sqLiteDatabase=user.getWritableDatabase();
            user.adddata(sqLiteDatabase,name_str,paswd_str,age_int,telephone_str,address_str);
            Toast.makeText(this, "数据添加成功", Toast.LENGTH_SHORT).show();
        } else if (viewId == R.id.show) {
            // 查询最新添加的数据
            //这里查询的的是一个空的对象
            // 获取数据库实例(只要创建一个数据库的对象，就能访问数据库)
            SQLiteDatabase db = user.getReadableDatabase();
            Cursor cursor = db.query(
                    "user",
                    new String[]{"username", "paswd", "age", "telephone", "address"},
                    null, null, null,null,
                    "id DESC", // 按ID降序排列，获取最新记录
                    "1"        // 只获取1条记录
            );
            if (cursor != null && cursor.moveToFirst()) {
                String username = cursor.getString(0);
                String password = cursor.getString(1);
                int age = cursor.getInt(2);
                String telephone = cursor.getString(3);
                String address = cursor.getString(4);
                // 显示查询到的数据
                Toast.makeText(RegisterSQLiteActivity.this,
                        "最新添加的用户:\n" +
                                "用户名: " + username + "\n" +
                                "密码: " + password + "\n" +
                                "年龄: " + age + "\n" +
                                "手机: " + telephone + "\n" +
                                "地址: " + address,
                        Toast.LENGTH_LONG).show();
                cursor.close();
            }
        }
    }
}
