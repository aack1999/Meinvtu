package com.aack.meinv.response;

import java.io.Serializable;
import java.util.List;

public class Results implements Serializable{
    private String summary;


    private String highlightWords;

    private String showid;

    private String stripe_bottom_no_status;//共多少集数

    private List<Serises> serises;

    private int exclusive;

    private String stripe_bottom;//最新更新到多少局

    private String show_thumburl;//缩图

    private String cats;

    private int paid;

    private int hide;

    private String pay_type;

    private String showname;//视频名称

    private double reputation;//评分

    private String source_name;//视频来源

    private String notice;

    private int completed;

    private int episode_total;

    private int source_id;

    private String show_vthumburl;//封面图片

    private int cate_id;

    private String play_videoid;

    private String play_title;

    private int sequence;

    private int is_youku;

    private int need_query;

    private int format_flag;

    private List<Episode> episodes;

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setHighlightWords(String highlightWords) {
        this.highlightWords = highlightWords;
    }

    public String getHighlightWords() {
        return this.highlightWords;
    }

    public void setShowid(String showid) {
        this.showid = showid;
    }

    public String getShowid() {
        return this.showid;
    }

    public void setStripe_bottom_no_status(String stripe_bottom_no_status) {
        this.stripe_bottom_no_status = stripe_bottom_no_status;
    }

    public String getStripe_bottom_no_status() {
        return this.stripe_bottom_no_status;
    }

    public void setSerises(List<Serises> serises) {
        this.serises = serises;
    }

    public List<Serises> getSerises() {
        return this.serises;
    }

    public void setExclusive(int exclusive) {
        this.exclusive = exclusive;
    }

    public int getExclusive() {
        return this.exclusive;
    }

    public void setStripe_bottom(String stripe_bottom) {
        this.stripe_bottom = stripe_bottom;
    }

    public String getStripe_bottom() {
        return this.stripe_bottom;
    }

    public void setShow_thumburl(String show_thumburl) {
        this.show_thumburl = show_thumburl;
    }

    public String getShow_thumburl() {
        return this.show_thumburl;
    }

    public void setCats(String cats) {
        this.cats = cats;
    }

    public String getCats() {
        return this.cats;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public int getPaid() {
        return this.paid;
    }

    public void setHide(int hide) {
        this.hide = hide;
    }

    public int getHide() {
        return this.hide;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_type() {
        return this.pay_type;
    }

    public void setShowname(String showname) {
        this.showname = showname;
    }

    public String getShowname() {
        return this.showname;
    }

    public void setReputation(double reputation) {
        this.reputation = reputation;
    }

    public double getReputation() {
        return this.reputation;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getSource_name() {
        return this.source_name;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getNotice() {
        return this.notice;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getCompleted() {
        return this.completed;
    }

    public void setEpisode_total(int episode_total) {
        this.episode_total = episode_total;
    }

    public int getEpisode_total() {
        return this.episode_total;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public int getSource_id() {
        return this.source_id;
    }

    public void setShow_vthumburl(String show_vthumburl) {
        this.show_vthumburl = show_vthumburl;
    }

    public String getShow_vthumburl() {
        return this.show_vthumburl;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public int getCate_id() {
        return this.cate_id;
    }

    public void setPlay_videoid(String play_videoid) {
        this.play_videoid = play_videoid;
    }

    public String getPlay_videoid() {
        return this.play_videoid;
    }

    public void setPlay_title(String play_title) {
        this.play_title = play_title;
    }

    public String getPlay_title() {
        return this.play_title;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getSequence() {
        return this.sequence;
    }

    public void setIs_youku(int is_youku) {
        this.is_youku = is_youku;
    }

    public int getIs_youku() {
        return this.is_youku;
    }

    public void setNeed_query(int need_query) {
        this.need_query = need_query;
    }

    public int getNeed_query() {
        return this.need_query;
    }

    public void setFormat_flag(int format_flag) {
        this.format_flag = format_flag;
    }

    public int getFormat_flag() {
        return this.format_flag;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }
}