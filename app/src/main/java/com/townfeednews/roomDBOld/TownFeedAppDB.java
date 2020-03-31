package com.townfeednews.roomDBOld;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Category.class},version = 1, exportSchema = false)
public abstract class TownFeedAppDB extends RoomDatabase {
    public abstract MyDataAccessObject myDataAccessObject();
}
