package com.example.Mobil1;

import com.example.Mobil1.database.model.User;

import java.util.List;

public interface getUsersTask {
    void OnPostExecute(List<User> users);
}
