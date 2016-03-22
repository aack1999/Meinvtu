package com.aack.meinv.response;

import java.util.List;

/**
 * Created by root on 16-3-22.
 */
public class Episode {

    int total;

    private List<Serises> serises;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Serises> getSerises() {
        return serises;
    }

    public void setSerises(List<Serises> serises) {
        this.serises = serises;
    }
}
