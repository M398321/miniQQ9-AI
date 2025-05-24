package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

@Insert
void insert(User user);
@Delete
void delete(User user);
@Update
void update(User user);
    @Query("SELECT * FROM USERS")
    List<User> query();
}
