package com.townfeednews.model.category;

import java.util.ArrayList;

public class CategoriesNameResponseData {
    private ArrayList<CategoryData> data;

    public ArrayList<CategoryData> getData() {
        return data;
    }

    public void setData(ArrayList<CategoryData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ClassPojo [data = " + data + "]";
    }
}
