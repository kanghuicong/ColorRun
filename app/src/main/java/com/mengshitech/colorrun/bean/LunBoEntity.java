package com.mengshitech.colorrun.bean;

/**
 * Created by wschenyongyin on 2016/7/28.
 */
public class LunBoEntity {
    private int lunbo_id;
    private String lunbo_image;
    private String lunbo_url;
    private String lunbo_title;
    private String update_type;
    private String update_values;


    public LunBoEntity() {

    }

    public int getLunbo_id() {
        return lunbo_id;
    }

    public void setLunbo_id(int lunbo_id) {
        this.lunbo_id = lunbo_id;
    }

    public String getLunbo_image() {
        return lunbo_image;
    }

    public void setLunbo_image(String lunbo_image) {
        this.lunbo_image = lunbo_image;
    }

    public String getLunbo_url() {
        return lunbo_url;
    }

    public void setLunbo_url(String lunbo_url) {
        this.lunbo_url = lunbo_url;
    }

    public String getLunbo_title() {
        return lunbo_title;
    }

    public void setLunbo_title(String lunbo_title) {
        this.lunbo_title = lunbo_title;
    }

    public String getUpdate_type() {
        return update_type;
    }

    public void setUpdate_type(String update_type) {
        this.update_type = update_type;
    }

    public String getUpdate_values() {
        return update_values;
    }

    public void setUpdate_values(String update_values) {
        this.update_values = update_values;
    }
}
