package com.hoon.imagesearch.data;

import android.support.annotation.Nullable;

import com.hoon.imagesearch.ui.ImageSearchContract;

import java.util.ArrayList;
import java.util.List;

/**
 * 이미지 검색의 상태 값을 저장하기 위한 클래스
 */
public class ImageSearchState {
    private List<Image> mImages;
    private String mQuery;
    private int mPage;
    private boolean mIsLastImage;
    private boolean mIsLoading;

    public ImageSearchState() {
        reset(null);
    }

    public void reset(@Nullable String query) {
        mQuery = query;

        mImages = new ArrayList<>(0);
        mPage = 0;
        mIsLastImage = false;
        mIsLoading = false;
    }

    public List<Image> getImages() {
        return mImages;
    }

    public String getQuery() {
        return mQuery;
    }

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        mPage = page;
    }

    public boolean isLastImage() {
        return mIsLastImage;
    }

    public void setLastImage(boolean lastImage) {
        mIsLastImage = lastImage;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public void setLoading(boolean loading) {
        mIsLoading = loading;
    }

    @Override
    public String toString() {
        return super.toString()
                + " {images = " + mImages.size()
                + ", page = " + mPage
                + ", last-image = " + mIsLastImage
                + ", loading = " + mIsLoading
                + "}";
    }
}
