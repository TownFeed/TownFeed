package com.townfeednews.roomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "news")
public class News {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "news_id")
    private String news_id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "permalink")
    private String permalink;

    @ColumnInfo(name = "details")
    private String details;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "datetime")
    private String datetime;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "category_id")
    private String category_id;

    @ColumnInfo(name = "source")
    private String source;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
