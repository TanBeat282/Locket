package com.tandev.locket.model.firend;

import java.io.Serializable;

public class Friend implements Serializable {
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
