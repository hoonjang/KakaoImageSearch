package com.hoon.imagesearch.data;

import com.google.gson.annotations.SerializedName;

/**
 * 이미지 검색 결과의 메타 데이터
 */
public class ImageSearchMetaData {
    @SerializedName("total_count")
    private int totalCount;
    @SerializedName("pageable_count")
    private int pageableCount;
    @SerializedName("is_end")
    private boolean isEnd;

    public int getTotalCount() {
        return totalCount;
    }

    public int getPageableCount() {
        return pageableCount;
    }

    public boolean isEnd() {
        return isEnd;
    }
}
