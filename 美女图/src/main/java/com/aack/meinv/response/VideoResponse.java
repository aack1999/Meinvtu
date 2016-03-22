package com.aack.meinv.response;

import java.util.List;

public class VideoResponse {
    private String channel_description;

    private String title;

    private String icon_url;

    private String cover_url;

    private String module_description;

    private String result;

    private List<VideoModel> video_list;

    public void setChannel_description(String channel_description) {
        this.channel_description = channel_description;
    }

    public String getChannel_description() {
        return this.channel_description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getIcon_url() {
        return this.icon_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getCover_url() {
        return this.cover_url;
    }

    public void setModule_description(String module_description) {
        this.module_description = module_description;
    }

    public String getModule_description() {
        return this.module_description;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return this.result;
    }

    public void setVideo_list(List<VideoModel> video_list) {
        this.video_list = video_list;
    }

    public List<VideoModel> getVideo_list() {
        return this.video_list;
    }

}