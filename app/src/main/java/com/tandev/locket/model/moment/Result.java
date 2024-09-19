package com.tandev.locket.model.moment;

import java.io.Serializable;
import java.util.ArrayList;

public class Result implements Serializable {
    private ArrayList<Data> data;
    private int missed_moments_count;
    private String sync_token;
    private int status;

    public  ArrayList<Data> getData() {
        return data;
    }

    public void setData( ArrayList<Data> data) {
        this.data = data;
    }

    public int getMissed_moments_count() {
        return missed_moments_count;
    }

    public void setMissed_moments_count(int missed_moments_count) {
        this.missed_moments_count = missed_moments_count;
    }

    public String getSync_token() {
        return sync_token;
    }

    public void setSync_token(String sync_token) {
        this.sync_token = sync_token;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
