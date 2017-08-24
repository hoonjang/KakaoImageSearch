package com.hoon.imagesearch.api;

import android.support.annotation.NonNull;

import com.hoon.imagesearch.api.response.ImageSearchResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 카카오 이미지 검색 API
 */
public interface ImageSearchApi {

    @GET("v2/search/image")
    Observable<ImageSearchResponse> searchImages(
            @NonNull @Query("query") String query, @Query("page") int page);

    @GET("v2/search/image")
    Observable<ImageSearchResponse> searchImages(
            @NonNull @Query("query") String query, @Query("page") int page, @Query("size") int size);
}
