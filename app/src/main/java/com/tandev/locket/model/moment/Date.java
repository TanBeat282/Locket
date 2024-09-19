package com.tandev.locket.model.moment;

import java.io.Serializable;

public class Date implements Serializable {
    private long _seconds;
    private int _nanoseconds;

    public long get_seconds() {
        return _seconds;
    }

    public void set_seconds(long _seconds) {
        this._seconds = _seconds;
    }

    public int get_nanoseconds() {
        return _nanoseconds;
    }

    public void set_nanoseconds(int _nanoseconds) {
        this._nanoseconds = _nanoseconds;
    }
}
