package com.example.Mobil1.database.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.Mobil1.database.dao.UserDao;
@Database(entities={User.class},version = 1)
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao getUserDao();
    private static UserDatabase userDB;

    public static UserDatabase getInstance(Context context){

        if(userDB==null){

            userDB=buildDatabaseInstance(context);

        }
        return userDB;



    }

    private static UserDatabase buildDatabaseInstance(Context context){

        return Room.databaseBuilder(context,UserDatabase.class,"userdb.db").allowMainThreadQueries().build();




    }
}
