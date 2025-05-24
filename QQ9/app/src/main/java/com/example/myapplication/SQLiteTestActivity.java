package com.example.myapplication;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
public class SQLiteTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sqlite_test);
        UserDatabase user =new UserDatabase(SQLiteTestActivity.this);
        SQLiteDatabase sqLiteDatabase=user.getReadableDatabase();
        //重置数据库
        Button button6=(Button)findViewById(R.id.reset);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase sql_date=user.getWritableDatabase();
                user.resetDatabase(sql_date);
            }
        });

        //删除数据库


        //查询
        Button button4=(Button)findViewById(R.id.search);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase sql_date=user.getWritableDatabase();
                user.delete(sql_date);
            }
        });
        //删除
        Button button3=(Button)findViewById(R.id.delete);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase sql_date=user.getWritableDatabase();
                user.delete(sql_date);
            }
        });
        //更改
        Button button2=(Button)findViewById(R.id.update);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase sql_date=user.getWritableDatabase();
                user.update(sql_date);
            }
        });
        //添加
//        Button button=(Button)findViewById(R.id.add);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SQLiteDatabase sql_date=user.getWritableDatabase();
//                user.adddata(sql_date);
//            }
//        });
    }

}
