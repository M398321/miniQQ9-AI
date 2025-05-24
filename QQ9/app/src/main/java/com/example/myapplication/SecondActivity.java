package com.example.myapplication;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SecondActivity extends AppCompatActivity{
    private ListView mListView;
    private MyBaseAdapter myAdapter;
    private List<Map<String,Object>>list=new ArrayList<Map<String, Object>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.second_activity);

        //下面的不能删除，用于设置系统上栏一样颜色的东西，还有不占用系统下栏
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.second), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //找到原因了，原来是因为找到的button不是fragment的，然后触发了它没有的功能导致崩溃，我不能再取button这个名字了，太多一样的了

        // 从loginActivity.java中获取用户名
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");

        //从一键登录(MainActivity.java)中获取用户名
        String username2 = intent.getStringExtra("username");

        // 使用用户名（例如显示在TextView中）
        //TextView tvWelcome = findViewById(R.id.tvWelcome);
        //tvWelcome.setText("欢迎, " + username + "!");
        Toast.makeText(SecondActivity.this, "欢迎, " + username2 + "!", Toast.LENGTH_SHORT).show();

        initData();
        mListView=(ListView)findViewById(R.id.listview);
        myAdapter= new MyBaseAdapter(list,this);
        mListView.setAdapter(myAdapter);

        ImageView imageView=(ImageView) findViewById(R.id.shouye);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SecondActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView3=(ImageView) findViewById(R.id.dongtai);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SecondActivity.this,ChatActivity.class);
                intent.putExtra("username", username2);
                startActivity(intent);
            }
        });

        // 第一步：对listView添加监听器
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(SecondActivity.this, "你点击了我！", Toast.LENGTH_SHORT).show();
                // 第二步：通过Intent跳转至新的页面
                Intent intent = new Intent(SecondActivity.this, ChatActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("username2", username2);//传递登录账号的用户名
                startActivity(intent);
            }
        });


        //通过代码设置顶部状态栏颜色
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#39C5BB"));
    }

    private void initData()
    {
//        Map<String, String> user = getUserData(SecondActivity.this);//用对象访问存储的注册信息数据
//        String password = user.get("paswd");
//        String username = user.get("username");

        Map<String,Object>map=new HashMap<String,Object>();
        map=new HashMap<String,Object>();
        map.put("img",R.drawable.miku);
        map.put("title","Miku");
        map.put("time","20:58");
        map.put("shortmessage","这个Context必须理解");
        map.put("static",R.mipmap.ic_launcher);
        list.add(map);
        map=new HashMap<String,Object>();
        map.put("img",R.drawable.computer);
        map.put("title","我的电脑");
        map.put("time","20:57");
        map.put("shortmessage","你已在电脑登录，可传文件到电脑");
        map.put("static",R.mipmap.ic_launcher);
        list.add(map);
        map=new HashMap<String,Object>();
        map.put("img",R.drawable.email);
        map.put("title","邮件");
        map.put("time","2024/08/08");
        map.put("shortmessage","Cisco NetWorking Academy: Join US for Inter...");
        map.put("static",R.mipmap.ic_launcher);
        list.add(map);

        map=new HashMap<String,Object>();
        map.put("img",R.drawable.xp);
        map.put("title","我的小号");
        map.put("time","20:51");
        map.put("shortmessage","[图片]");
        map.put("static",R.mipmap.ic_launcher);
        list.add(map);

/*----------------------------------------滥竽充数内容-----------------------------------------*/
        map=new HashMap<String,Object>();
        map.put("img",R.drawable.cplus);
        map.put("title","C++学习群");
        map.put("time","20:58");
        map.put("shortmessage","G: Unity引擎怎么入门？");
        map.put("static",R.mipmap.ic_launcher);
        //   map.put("button","学习");
        list.add(map);
        map=new HashMap<String,Object>();
        map.put("img",R.drawable.python);
        map.put("title","Python学习群");
        map.put("time","20:57");
        map.put("shortmessage","暖宝宝: Python怎么写推理代码？");
        map.put("static",R.mipmap.ic_launcher);
        //  map.put("button","学习");
        list.add(map);
        map=new HashMap<String,Object>();
        map.put("img",R.drawable.java);
        map.put("title","Java交流群");
        map.put("time","2024/08/08");
        map.put("shortmessage","Dear: Java");
        map.put("static",R.mipmap.ic_launcher);
        //  map.put("button","学习");
        list.add(map);

    }
    //读取注册信息
//    public static Map<String, String> getUserData(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("user_mes", Context.MODE_PRIVATE);
//        Map<String, String> user = new HashMap<>();
//        user.put("username", sharedPreferences.getString("username", null));
//        user.put("paswd", sharedPreferences.getString("paswd", null));
//        return user;
//    }
}
