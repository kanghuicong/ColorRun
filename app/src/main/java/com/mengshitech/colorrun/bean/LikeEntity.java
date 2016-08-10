package com.mengshitech.colorrun.bean;

import java.io.Serializable;

/**
 * Created by kanghuicong on 2016/8/10  15:19.
 */
public class LikeEntity implements Serializable {
    String user_id;
    String user_header;
    String user_name;
    String like_time;

    public LikeEntity(String user_id, String user_name, String user_header, String like_time) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_header = user_header;
        this.like_time = like_time;
    }

    public LikeEntity() {
        super();
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLike_time() {
        return like_time;
    }

    public void setLike_time(String like_time) {
        this.like_time = like_time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_header() {
        return user_header;
    }

    public void setUser_header(String user_header) {
        this.user_header = user_header;
    }
}
