package com.aack.meinv.response;

import java.util.List;

/**
 * Created by root on 16-2-25.
 */
public class GalleryResponse extends BaseResponse {

    int total;

    List<Gallery> tngou;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Gallery> getTngou() {
        return tngou;
    }

    public void setTngou(List<Gallery> tngou) {
        this.tngou = tngou;
    }
}
