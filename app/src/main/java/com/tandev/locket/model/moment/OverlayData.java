package com.tandev.locket.model.moment;

import java.io.Serializable;

public class OverlayData implements Serializable {
    private int max_lines;
    private Background background;
    private String text;
    private String text_color;
    private String type;

    public int getMax_lines() {
        return max_lines;
    }

    public void setMax_lines(int max_lines) {
        this.max_lines = max_lines;
    }

    public Background getBackground() {
        return background;
    }

    public void setBackground(Background background) {
        this.background = background;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText_color() {
        return text_color;
    }

    public void setText_color(String text_color) {
        this.text_color = text_color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
