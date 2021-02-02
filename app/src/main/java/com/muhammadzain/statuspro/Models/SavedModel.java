package com.muhammadzain.statuspro.Models;

import java.io.Serializable;

public class SavedModel implements Serializable {
    private String title,path;

    public SavedModel() {}

    public SavedModel(String title, String path) {
        this.title = title;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
