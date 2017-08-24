package com.hoon.imagesearch.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hoon.imagesearch.data.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * View-Presenter 간의 동작을 정의
 */
public interface ImageSearchContract {

    interface View {

        void initQueryText(@Nullable String query);

        void addImages(@NonNull List<Image> images);

        void clearImages();

        void setLoadingIndicator(boolean active);

        void setLastImageIndicator(boolean active);

        void showNoImages();

        void showSearchingImagesError();
    }

    interface Presenter {

        void attachView(@NonNull View view);

        void detachView();

        void startNewSearch(@NonNull String query);

        void loadMoreImages();

    }

}
