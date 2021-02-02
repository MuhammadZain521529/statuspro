package com.muhammadzain.statuspro.Models;

import java.io.File;
import java.io.Serializable;

public class ModelImage implements Serializable {

    private String imagePath,title;
    private File file;
    public boolean isSelected;

    public ModelImage() {}


    public ModelImage(String imagePath, String title, File file) {
        this.imagePath = imagePath;
        this.title = title;
        this.file = file;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

