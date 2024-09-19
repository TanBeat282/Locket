package com.tandev.locket.model.moment;

import java.io.Serializable;

public class Moment implements Serializable {
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
