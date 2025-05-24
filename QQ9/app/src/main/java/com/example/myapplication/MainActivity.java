package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.TextUtils;
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

import org.w3c.dom.Text;

import java.awt.font.TextAttribute;
import java.util.List;

import de.hdodenhof.circleimageview.BuildConfig;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity{

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
// 启动服务
        Intent serviceIntent = new Intent(this, TimeService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CircleImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.xp);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button=(Button)findViewById(R.id.loginBtn1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String username =findViewById(R.id.loginBtn1).toString();
                String username ="Miku"; //后面写好数据库功能和默认账号绑定
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(MainActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        ImageView imagelogin=(ImageView) findViewById(R.id.add);
        imagelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,loginActivity.class);
                startActivity(intent);
            }
        });

        TextView textView=(TextView) findViewById(R.id.username1);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, SQLiteTestActivity.class);
                startActivity(intent);
            }
        });
    }

}