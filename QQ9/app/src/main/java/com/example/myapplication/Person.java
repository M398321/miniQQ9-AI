package com.example.myapplication;
import android.content.Intent;
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
public class Person{
    private String username;
    private String paswd;
    public Person(String username,String paswd)
    {
        this.username=username;
        this.paswd=paswd;
    }
    public void setUsername(String username)
    {
        this.username=username;
    }
    public String getUsername()
    {
        return username;
    }
    public void setPaswd(String paswd)
    {
        this.paswd=paswd;
    }
    public String getPaswd()
    {
        return paswd;
    }
    @Override
    public String toString()
    {
      return "Person{" + "username='"+ username + '\''+",paswd='"+paswd+'\''+'}';
    }
}
