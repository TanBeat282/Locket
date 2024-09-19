package com.tandev.locket.model.firend;

import java.io.Serializable;

public class Result implements Serializable {
    private int status;
    private UserData data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }
}
