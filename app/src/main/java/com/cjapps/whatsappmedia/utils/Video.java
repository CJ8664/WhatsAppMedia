package com.cjapps.whatsappmedia.utils;

/*
 ** Model to store Video object details
*/
public class Video {

    private String Title;
    private boolean isSelected;
    private int duration;

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String mTitle) {
        this.Title = mTitle;
    }
}
