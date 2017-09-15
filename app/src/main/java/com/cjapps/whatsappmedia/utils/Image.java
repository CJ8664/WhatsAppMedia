package com.cjapps.whatsappmedia.utils;

import android.net.Uri;

/*
 ** Model to store Image object details
*/
public class Image {

    private Uri imageUri;

    private long imageId;

    public Image(Uri imageUri,long imageId) {
        this.imageUri = imageUri;
        this.imageId = imageId;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public long getId() {
        return imageId;
    }

    public void setId(long imageId) {
        this.imageId = imageId;
    }
}
