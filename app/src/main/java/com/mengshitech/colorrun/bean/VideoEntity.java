package com.mengshitech.colorrun.bean;

/**
 *@Author: wschenyongyin
 *@Date: 2016-7-15
 *@explain:
 *@TestState:
 */
public class VideoEntity {

	private int video_id;
	private String video_image;
	private String video_url;
	private String video_title;
	private String update_type;
	private String update_values;
	

	public String getUpdate_type() {
		return update_type;
	}

	public void setUpdate_type(String updateType) {
		update_type = updateType;
	}

	public String getUpdate_values() {
		return update_values;
	}

	public void setUpdate_values(String updateValues) {
		update_values = updateValues;
	}

	public VideoEntity() {
	}

	public int getVideo_id() {
		return video_id;
	}

	public void setVideo_id(int videoId) {
		video_id = videoId;
	}

	public String getVideo_image() {
		return video_image;
	}

	public void setVideo_image(String videoImage) {
		video_image = videoImage;
	}

	public String getVideo_url() {
		return video_url;
	}

	public void setVideo_url(String videoUrl) {
		video_url = videoUrl;
	}

	public String getVideo_title() {
		return video_title;
	}

	public void setVideo_title(String videoTitle) {
		video_title = videoTitle;
	}

}
