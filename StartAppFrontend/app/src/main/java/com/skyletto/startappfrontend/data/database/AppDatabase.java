package com.skyletto.startappfrontend.data.database;

import android.content.Context;

import com.skyletto.startappfrontend.domain.entities.Tag;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Tag.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase db;
    private static final String DB_NAME = "main.db";

    public static AppDatabase getInstance(Context context){
        if (db ==null)
            synchronized (AppDatabase.class) {
                if (db == null)
                    db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
            }
        return db;
    }
}
