package com.townfeednews.model.news;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class News {
    @SerializedName("news_id")
    @Expose
    private String newsId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("permalink")
    @Expose
    private String permalink;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("hd_image")
    @Expose
    private String hdImage;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("datetime")
    @Expose
    private String datetime;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("category_id")
    @Expose
    private String cat_id;

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
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

    public String getHdImage() {
        return hdImage;
    }

    public void setHdImage(String hdImage) {
        this.hdImage = hdImage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
