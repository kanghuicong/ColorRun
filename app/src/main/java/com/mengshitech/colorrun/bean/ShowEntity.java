package com.mengshitech.colorrun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by atenklsy on 2016/7/14 10:49.
 * E-address:atenk@qq.com.
 */
public class ShowEntity implements Serializable {
    String show_id;
    String user_id;
    String user_header;
    String user_name;
    String show_content;
    String show_image;
    String show_time;
    String comment_num;
    String like_num;

    public ShowEntity(String show_id, String like_num, String comment_num, String show_time, String show_image, String show_content, String user_id, String user_name,String user_header) {
        this.show_id = show_id;
        this.like_num = like_num;
        this.comment_num = comment_num;
        this.show_time = show_time;
        this.show_image = show_image;
        this.show_content = show_content;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_header = user_header;
    }

    public String getUser_header() {
        return user_header;
    }

    public void setUser_header(String user_header) {
        this.user_header = user_header;
    }

    public ShowEntity() {
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getLike_num() {
        return like_num;
    }

    public void setLike_num(String like_num) {
        this.like_num = like_num;
    }


    public String getShow_id() {
        return show_id;
    }

    public void setShow_id(String show_id) {
        this.show_id = show_id;
    }

    public String getShow_time() {
        return show_time;
    }

    public void setShow_time(String show_time) {
        this.show_time = show_time;
    }

    public String getShow_image() {
        return show_image;
    }

    public void setShow_image(String show_image) {
        this.show_image = show_image;
    }

    public String getShow_content() {
        return show_content;
    }

    public void setShow_content(String show_content) {
        this.show_content = show_content;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}

