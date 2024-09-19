package com.tandev.locket.model.moment;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {
    private Date date;
    private ArrayList<Overlay> overlays;
    private String caption;
    private String thumbnail_url;
    private String canonical_uid;
    private String user;
    private String md5;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public  ArrayList<Overlay> getOverlays() {
        return overlays;
    }

    public void setOverlays( ArrayList<Overlay> overlays) {
        this.overlays = overlays;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getCanonical_uid() {
        return canonical_uid;
    }

    public void setCanonical_uid(String canonical_uid) {
        this.canonical_uid = canonical_uid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
