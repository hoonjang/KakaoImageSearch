package com.hoon.imagesearch.api;

import android.support.annotation.NonNull;

import com.hoon.imagesearch.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 카카오 REST API + Retrofit 사용을 위한 헬퍼 클래스
 */
public class KakaoRestApiHelper {

    private static final String BASE_URL = "https://dapi.kakao.com/";
    private static final String KAKAO_API_KEY = "1acfe43b618f3aaf93d9ade30a808cbe";

    private final Retrofit mRetrofit;

    private KakaoRestApiHelper() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ?
                HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        OkHttpClient client = httpClient
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(loggingInterceptor)
                .build();

        mRetrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static Retrofit getRetrofit() {
        return SingletonHolder.INSTANCE.mRetrofit;
    }

    private static class HeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request original = chain.request();

            Request.Builder requestBuilder = original.newBuilder();

            requestBuilder.addHeader("Authorization",
                    "KakaoAK " + KAKAO_API_KEY);

            return chain.proceed(requestBuilder.build());
        }
    }

    private static class SingletonHolder {
        private static final KakaoRestApiHelper INSTANCE = new KakaoRestApiHelper();
    }

}