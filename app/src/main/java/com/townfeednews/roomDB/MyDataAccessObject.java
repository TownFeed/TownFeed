package com.townfeednews.roomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.twitter.sdk.android.core.models.User;

import java.util.List;

@Dao
public interface MyDataAccessObject {
    @Insert
    public void addNewsToDb(News news);

    @Query("select * from news")
    public List<News> getNews();

    @Query("DELETE from news")
    public void deleteAllNews();

}
