package com.townfeednews.model.category;

import java.util.ArrayList;

public class CategoryData {

    private String cat_name;
    private String cat_id;
    private String status;

    public String getCat_name ()
    {
        return cat_name;
    }

    public void setCat_name (String cat_name)
    {
        this.cat_name = cat_name;
    }

    public String getCat_id ()
    {
        return cat_id;
    }

    public void setCat_id (String cat_id)
    {
        this.cat_id = cat_id;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [cat_name = "+cat_name+", cat_id = "+cat_id+", status = "+status+"]";
    }
}
