package com.example.Mobil1.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.Mobil1.database.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    long insertUser(User user);
    @Update
    int updateUser(User user);

    @Delete
    void deleteUser(User user);
    @Query("SELECT * FROM user")
    List<User> getUser();

    @Query("SELECT * FROM user WHERE email=:username and password=:password")
    User getuserLogin(String username,String password);

}
