package com.example.cakeshopper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CategoryModel extends AppCompatActivity {
    private int categoryIconLink;
    private String categoryName;

    public int getCategoryIconLink() {
        return categoryIconLink;
    }

    public void setCategoryIconLink(int categoryIconLink) {
        this.categoryIconLink = categoryIconLink;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public CategoryModel(int categoryIconLink, String categoryName) {
        this.categoryIconLink = categoryIconLink;
        this.categoryName = categoryName;
    }
}