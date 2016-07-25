package com.mengshitech.colorrun.bean;

import android.graphics.Bitmap;

/**
 * Created by kanghuicong on 2016/7/22  21:09.
 * 515849594@qq.com
 */
public class ImageEntity {
    private Bitmap poster_bitmap;
    private Bitmap commodity_bitmap;
    private Bitmap header_bitmap;

    public Bitmap getHeader_bitmap() {
        return header_bitmap;
    }

    public void setHeader_bitmap(Bitmap header_bitmap) {
        this.header_bitmap = header_bitmap;
    }

    public Bitmap getPoster_bitmap() {
        return poster_bitmap;
    }

    public void setPoster_bitmap(Bitmap poster_bitmap) {
        this.poster_bitmap = poster_bitmap;
    }

    public Bitmap getCommodity_bitmap() {
        return commodity_bitmap;
    }
    public void setCommodity_bitmap(Bitmap commodity_bitmap) {
        this.commodity_bitmap = commodity_bitmap;
    }

}
