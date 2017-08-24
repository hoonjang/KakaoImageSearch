package com.hoon.imagesearch.ui;

import android.support.annotation.NonNull;

import com.hoon.imagesearch.api.ImageSearchApi;
import com.hoon.imagesearch.data.ImageSearchState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ImageSearchPresenter implements ImageSearchContract.Presenter {

    private static final int PAGE_SIZE = 25;

    @NonNull
    private final ImageSearchApi mSearchApi;
    @NonNull
    private final CompositeDisposable mDisposables;
    @NonNull
    private final ImageSearchState mCurrentState;

    private ImageSearchContract.View mView;

    ImageSearchPresenter(@NonNull ImageSearchApi searchApi) {
        mSearchApi = searchApi;
        mDisposables = new CompositeDisposable();
        mCurrentState = new ImageSearchState();
    }

    @Override
    public void attachView(@NonNull ImageSearchContract.View view) {
        mView = view;
        // restore previous view state
        mView.initQueryText(mCurrentState.getQuery());
        mView.clearImages();
        mView.addImages(mCurrentState.getImages());

        if (mCurrentState.isLoading()) {
            // cancelled while loading -- resume it
            mCurrentState.setLoading(false);
            loadNextPage();
        }
    }

    @Override
    public void detachView() {
        mView = null;
        mDisposables.clear();
    }

    @Override
    public void startNewSearch(@NonNull String query) {
        mCurrentState.reset(query);
        loadNextPage();
    }

    @Override
    public void loadMoreImages() {
        loadNextPage();
    }

    private void loadNextPage() {
        if (mCurrentState.isLastImage() || mCurrentState.isLoading()) {
            return;
        }

        mDisposables.clear(); // cancel previous jobs
        mCurrentState.setLoading(true);
        mView.setLoadingIndicator(true);
        mView.setLastImageIndicator(false);
        if (mCurrentState.getPage() == 0) {
            mView.clearImages();
        }
        final int newPage = mCurrentState.getPage() + 1;

        Disposable disposable =
                mSearchApi.searchImages(mCurrentState.getQuery(), newPage, PAGE_SIZE)
                        .subscribeOn(Schedulers.io())
                        .doOnNext(response -> {
                            mCurrentState.setLoading(false);
                            if (response.hasImages()) {
                                mCurrentState.getImages().addAll(response.getImages());
                                mCurrentState.setPage(newPage);
                                mCurrentState.setLastImage(response.isLastImage());
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (isViewAttached()) {
                                mView.setLoadingIndicator(false);
                                if (response.hasImages()) {
                                    mView.addImages(response.getImages());
                                    mView.setLastImageIndicator(response.isLastImage());
                                } else {
                                    mView.showNoImages();
                                }
                            }
                        }, error -> {
                            mCurrentState.setLoading(false);
                            if (isViewAttached()) {
                                mView.setLoadingIndicator(false);
                                mView.showSearchingImagesError();
                            }
                        });
        mDisposables.add(disposable);
    }

    private boolean isViewAttached() {
        return mView != null;
    }
}
