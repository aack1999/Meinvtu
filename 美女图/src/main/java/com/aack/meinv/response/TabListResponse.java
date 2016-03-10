package com.aack.meinv.response;

import java.util.List;

/**
 * Created by root on 16-2-25.
 */
public class TabListResponse extends BaseResponse {

    List<TabModel> tngou;

    public static class TabModel{
        String description;
        int id;
        String keywords;
        String name;
        int seq;
        String title;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSeq() {
            return seq;
        }

        public void setSeq(int seq) {
            this.seq = seq;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public List<TabModel> getTngou() {
        return tngou;
    }

    public void setTngou(List<TabModel> tngou) {
        this.tngou = tngou;
    }
}
