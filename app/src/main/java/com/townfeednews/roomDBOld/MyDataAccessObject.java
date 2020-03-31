package com.townfeednews.roomDBOld;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyDataAccessObject {

    @Insert
    public void addCategory(Category category);

    @Query("select * from category")
    public List<Category> getCategories();

    @Query("delete from category")
    public void deleteAllCategoryData();

    @Query("delete from category")
    public void logOutUser();

    @Update
    public void updateCategory(Category category);

    @Query("select * from category where cat_id like:clicked_Cat")
    public Category getCategoryStatus(String clicked_Cat);

}
