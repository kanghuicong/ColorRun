package com.mengshitech.colorrun.bean;

import java.io.Serializable;

/**
 * Created by atenklsy on 2016/7/16 16:57.
 * E-address:atenk@qq.com.
 */
public class CommentEntity implements Serializable {

    private String user_header;
    private String user_name;
    private String comment_time;
    private String comment_content;

    public String getUser_header() {
        return user_header;
    }

    public void setUser_header(String user_header) {
        this.user_header = user_header;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
