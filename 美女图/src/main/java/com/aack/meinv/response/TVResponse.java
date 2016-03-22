package com.aack.meinv.response;

import java.util.List;

public class TVResponse {

    private int pg;

    private int pz;

    private String qc_str;

    private List<Results> results;

    private String status;

    private int total;

    public void setPg(int pg) {
        this.pg = pg;
    }

    public int getPg() {
        return this.pg;
    }

    public void setPz(int pz) {
        this.pz = pz;
    }

    public int getPz() {
        return this.pz;
    }

    public void setQc_str(String qc_str) {
        this.qc_str = qc_str;
    }

    public String getQc_str() {
        return this.qc_str;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public List<Results> getResults() {
        return this.results;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return this.total;
    }

}