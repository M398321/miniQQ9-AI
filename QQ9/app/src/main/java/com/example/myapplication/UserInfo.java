package com.example.myapplication;

public class UserInfo {
    private int id;
    private String username;
    private String paswd;
    public UserInfo(int id,String username,String paswd)
    {
        this.id=id;
        this.username=username;
    }
    public void getId(int id)
    {
        this.id=id;
    }
    public void setUsername(String username)
    {
        this.username=username;
    }
    public void setPaswd(String paswd)
    {
        this.paswd=paswd;
    }

}
