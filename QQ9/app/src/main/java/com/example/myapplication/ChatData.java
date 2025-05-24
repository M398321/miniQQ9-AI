package com.example.myapplication;

public class ChatData {
    private int imageId;    //图片ID
    private String text;    //文本内容
    private int type;   //类型，左边还是右边
    public ChatData()
    {

    }
    public ChatData(int imageId, String text, int type)
    {
        this.imageId=imageId;
        this.text=text;
        this.type=type;
    }
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
