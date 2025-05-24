package com.example.myapplication;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.text.input.TextFieldValue;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.ai.client.generativeai.common.shared.Content;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBaseAdapter extends BaseAdapter{
    private List<Map<String,Object>>datas;
    private Context mContent;
    /*构造函数
    * datas表示需要绑定到View的数据，mContent表示传入上下文*/
    public MyBaseAdapter(List<Map<String,Object>>datas, Context mContext)
    {
        this.datas=datas;
        this.mContent=mContext;
    }

    @Override
    public int getCount()
    {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            //使用自定义的布局文件作为Layout
            convertView = LayoutInflater.from(mContent).inflate(R.layout.list_item_layout, parent,false);
            //减少findView的次数
            holder = new ViewHolder();
            //初始化布局中的元素
            holder.mImageView = (ImageView) convertView.findViewById(R.id.imageview);
            holder.mTextView = (TextView) convertView.findViewById(R.id.textview);
            holder.mTextView_end = (TextView) convertView.findViewById(R.id.textview_end);
            holder.mTextView2 = (TextView) convertView.findViewById(R.id.textview2);
            holder.mTextView2_end = (ImageView) convertView.findViewById(R.id.textview2_end);
        //    holder.mButton = (Button) convertView.findViewById(R.id.button);
//            holder.mButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(mContent, "你点击了我！", Toast.LENGTH_SHORT).show();
//                }
//            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //从传入的数据中提取数据并绑定到指定的View中
        holder.mImageView.setImageResource((Integer) datas.get(position).get("img"));
        holder.mTextView.setText(datas.get(position).get("title").toString());
        holder.mTextView_end.setText(datas.get(position).get("time").toString());
        holder.mTextView2.setText(datas.get(position).get("shortmessage").toString());
        holder.mTextView2_end.setImageResource((Integer) datas.get(position).get("static"));
  //      holder.mButton.setText(datas.get(position).get("button").toString());

        return convertView;
    }
        static class ViewHolder{
        ImageView mImageView;
        TextView mTextView;
        TextView mTextView2;
        TextView mTextView_end;
        ImageView mTextView2_end;
    //    Button mButton;
    }
    // 私有构造器防止实例化


}
