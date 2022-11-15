package com.example.cakeshopper;

import android.graphics.Bitmap;

public class PicModel {
    Bitmap bitmap;
    String url;

    public PicModel(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
