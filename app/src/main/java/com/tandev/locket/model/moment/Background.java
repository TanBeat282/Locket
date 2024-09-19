package com.tandev.locket.model.moment;

import java.io.Serializable;

public class Background implements Serializable {
    private String material_blur;
    private String[] colors;

    public String getMaterial_blur() {
        return material_blur;
    }

    public void setMaterial_blur(String material_blur) {
        this.material_blur = material_blur;
    }

    public String[] getColors() {
        return colors;
    }

    public void setColors(String[] colors) {
        this.colors = colors;
    }
}
