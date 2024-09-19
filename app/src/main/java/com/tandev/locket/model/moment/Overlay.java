package com.tandev.locket.model.moment;

import java.io.Serializable;

public class Overlay implements Serializable {
    private String overlay_id;
    private String overlay_type;
    private OverlayData data;
    private String alt_text;

    public String getOverlay_id() {
        return overlay_id;
    }

    public void setOverlay_id(String overlay_id) {
        this.overlay_id = overlay_id;
    }

    public String getOverlay_type() {
        return overlay_type;
    }

    public void setOverlay_type(String overlay_type) {
        this.overlay_type = overlay_type;
    }

    public OverlayData getData() {
        return data;
    }

    public void setData(OverlayData data) {
        this.data = data;
    }

    public String getAlt_text() {
        return alt_text;
    }

    public void setAlt_text(String alt_text) {
        this.alt_text = alt_text;
    }
}
