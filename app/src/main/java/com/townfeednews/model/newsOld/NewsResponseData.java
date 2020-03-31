package com.townfeednews.model.newsOld;

import java.util.ArrayList;

public class NewsResponseData {

    private ArrayList<News> news;

    public ArrayList<News> getNews ()
    {
        return news;
    }

    public void setNews (ArrayList<News> news)
    {
        this.news = news;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [news = "+news+"]";
    }
}
