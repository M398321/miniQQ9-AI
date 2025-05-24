package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class UserDatabase extends SQLiteOpenHelper {

    public UserDatabase(Context context) {
        super(context, "user_db",null,1);//上下文内容、数据库名称、
    }
//数据库里面没有String类型
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql="create table user(id integer primary key autoincrement,username varchar(20)" +
                ",paswd varchar(20),age integer,telephone varchar(20),address varchar(20))";
        sqLiteDatabase.execSQL(sql); //创建完后要执行数据库
    }
    //i和i1分别是老版本和新版本
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }


    public void adddata(SQLiteDatabase sqLiteDatabase,String username,String paswd,int age,String telephone,String address)
    {
        //创建一个ContentValues对象
        ContentValues values=new ContentValues();
        values.put("username",username);
        values.put("paswd",paswd);
        values.put("age",age);
        values.put("telephone",telephone);
        values.put("address",address);
        sqLiteDatabase.insert("user",null,values);
        //sqLiteDatabase.close(); //加上这一句就无法打开数据库了
    }

    public void delete(SQLiteDatabase sqLiteDatabase)
    {
      sqLiteDatabase.delete("user","username=?",new String[]{"诸葛柳"});
      //sqLiteDatabase.close();
    }


    public void update(SQLiteDatabase sqLiteDatabase)
    {
        ContentValues values=new ContentValues();
        values.put("paswd","22233333");sqLiteDatabase.update("user",values
            ,"username=?",new String[]{"董卿玮"});
        //sqLiteDatabase.close();
    }

    //查询数据
//    public List<UserInfo>querydata(SQLiteDatabase sqLiteDatabase)
//    {
//        Cursor cursor=sqLiteDatabase.query("user",null,null,null,null,null,"id ASC");
//        List<UserInfo>list=new ArrayList<UserInfo>();
//        while(cursor.moveToNext())
//        {
//            @SuppressLint("Range") int id=cursor.getInt(cursor.getColumnIndex("id"));
//            String username=cursor.getString(1);
//            String paswd=cursor.getString(2);
//            list.add(new userInfo(id,username,paswd));
//            return  list;
//        }
//    }
    public void resetDatabase(SQLiteDatabase db) {
        db = getWritableDatabase();
        // 删除所有表
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS table2");
        // 重新创建表
        onCreate(db);
        db.close();
    }
    public static void deleteDatabase(Context context, String dbName) {
        context.deleteDatabase(dbName);
        Log.d("Database", "Database " + dbName + " deleted successfully");
    }
}


