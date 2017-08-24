package com.hoon.imagesearch.data;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * 이미지 데이터
 */
public class Image {

    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private int height;

    public String getImageUrl() {
        return imageUrl;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(imageUrl) && width > 0 && height > 0;
    }
}
