package com.townfeednews.model.rv_list;

public class CategoryData {
    private String categoryName;
    private String categoryId;
    private int enableIcon;
    private int disableIcon;

    public CategoryData(String categoryName, String categoryId,int enableIcon,int disableIcon) {
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.enableIcon = enableIcon;
        this.disableIcon = disableIcon;
    }

    public int getEnableIcon() {
        return enableIcon;
    }

    public int getDisableIcon() {
        return disableIcon;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
