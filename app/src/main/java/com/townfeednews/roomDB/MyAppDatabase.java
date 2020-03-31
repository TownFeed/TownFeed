package com.townfeednews.roomDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {News.class}, version = 1,exportSchema = false)
public abstract class MyAppDatabase extends RoomDatabase {
    public abstract MyDataAccessObject myDataAccessObject();


}
