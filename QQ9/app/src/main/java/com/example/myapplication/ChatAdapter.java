package com.example.myapplication;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

public class ChatAdapter extends BaseAdapter {
    private Context mContext;
    private LinkedList<ChatData> myData;

    public ChatAdapter() {}

    public ChatAdapter(LinkedList<ChatData> myData, Context mContext) {
        this.myData = myData;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return myData.size();
    }

    @Override
    public Object getItem(int position) {
        return myData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        TextView textView;
        if(myData.get(position).getType() == 0){
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.fragment_chat_left,parent,false);
            imageView = convertView.findViewById(R.id.image_left);
            textView = convertView.findViewById(R.id.text_left);
        }
        else {
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.fragment_chat_right,parent,false);
            imageView = convertView.findViewById(R.id.image_right);
            textView = convertView.findViewById(R.id.text_right);
        }
        imageView.setImageResource(myData.get(position).getImageId());
        textView.setText(myData.get(position).getText());
        return convertView;
    }

    // 添加更新数据的方法
    public void updateData(LinkedList<ChatData> newData) {
        this.myData.clear();
        this.myData.addAll(newData);
        notifyDataSetChanged(); // 现在可以正常调用
    }

}
