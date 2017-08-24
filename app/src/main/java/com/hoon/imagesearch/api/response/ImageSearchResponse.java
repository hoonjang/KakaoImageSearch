package com.hoon.imagesearch.api.response;

import com.google.gson.annotations.SerializedName;
import com.hoon.imagesearch.data.Image;
import com.hoon.imagesearch.data.ImageSearchMetaData;

import java.util.List;

/**
 * 카카오 이미지 검색 결과
 */
public class ImageSearchResponse {

    @SerializedName("meta")
    private ImageSearchMetaData meta;
    @SerializedName("documents")
    private List<Image> images;

    public ImageSearchMetaData getMeta() {
        return meta;
    }

    public List<Image> getImages() {
        return images;
    }

    public boolean hasImages() {
        return images != null && !images.isEmpty();
    }

    public boolean isLastImage() {
        return getMeta().isEnd();
    }
}
