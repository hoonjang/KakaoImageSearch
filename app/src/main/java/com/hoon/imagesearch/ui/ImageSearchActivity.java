package com.hoon.imagesearch.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.hoon.imagesearch.R;
import com.hoon.imagesearch.api.ImageSearchApi;
import com.hoon.imagesearch.api.KakaoRestApiHelper;
import com.hoon.imagesearch.data.Image;
import com.hoon.imagesearch.ui.adapter.ImageSearchAdapter;
import com.hoon.imagesearch.util.Utils;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ImageSearchActivity extends AppCompatActivity implements ImageSearchContract.View {

    private static final long SEARCH_DEBOUNCE_TIMEOUT_MILLIS = 1000L;
    private static final long SCROLL_SAMPLE_PERIOD_MILLIS = 100L;

    @NonNull
    private final CompositeDisposable mDisposables = new CompositeDisposable();

    private ImageSearchContract.Presenter mPresenter;

    private SearchView mSearchView;

    private RecyclerView mRecyclerView;
    private ImageSearchAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private TextView mErrorMsgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mSearchView = (SearchView) findViewById(R.id.search_view);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new ImageSearchAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

        mErrorMsgView = (TextView) findViewById(R.id.error_msg);

        Object lastCustomNonConfigurationInstance = getLastCustomNonConfigurationInstance();
        if (lastCustomNonConfigurationInstance instanceof ImageSearchContract.Presenter) {
            mPresenter = ((ImageSearchContract.Presenter) lastCustomNonConfigurationInstance);
        } else {
            mPresenter = new ImageSearchPresenter(
                    KakaoRestApiHelper.getRetrofit().create(ImageSearchApi.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.attachView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.detachView();
    }

    @Override
    protected void onPause() {
        mDisposables.clear();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Disposable searchViewDisposable = RxSearchView.queryTextChangeEvents(mSearchView)
                .skip(1) // ignore the initial value
                .debounce(SEARCH_DEBOUNCE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .map(event -> event.queryText().toString())
                .filter(query -> query.length() > 0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(query -> mPresenter.startNewSearch(query));
        mDisposables.add(searchViewDisposable);

        Disposable recyclerViewDisposable = RxRecyclerView.scrollEvents(mRecyclerView)
                .skip(1)// ignore the first event
                .sample(SCROLL_SAMPLE_PERIOD_MILLIS, TimeUnit.MILLISECONDS)
                .filter(event -> {
                    int totalItemCount = mLayoutManager.getItemCount();
                    int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                    return (lastVisibleItemPosition >= totalItemCount - 3);
                })
                .subscribe(event -> mPresenter.loadMoreImages());
        mDisposables.add(recyclerViewDisposable);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mPresenter;
    }

    @Override
    public void initQueryText(@Nullable String query) {
        mSearchView.setQuery(query == null ? "" : query, false);
    }

    @Override
    public void addImages(@NonNull List<Image> images) {
        mErrorMsgView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        mAdapter.addDataSet(images);

        Utils.hideKeyboard(this);
        mSearchView.clearFocus();
    }

    @Override
    public void clearImages() {
        mAdapter.clearDataSet();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mErrorMsgView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        // scrolling 중 RecyclerView 변경으로 인한 Exception 방지를 위해 post 사용
        mRecyclerView.post(() -> {
            mAdapter.setLoadingIndicator(active);
        });
    }

    @Override
    public void setLastImageIndicator(boolean active) {
        mErrorMsgView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        // scrolling 중 RecyclerView 변경으로 인한 Exception 방지를 위해 post 사용
        mRecyclerView.post(() -> mAdapter.setLastImageIndicator(active));
    }

    @Override
    public void showNoImages() {
        mRecyclerView.setVisibility(View.GONE);
        mErrorMsgView.setText(R.string.no_images_description);
        mErrorMsgView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSearchingImagesError() {
        mRecyclerView.setVisibility(View.GONE);
        mErrorMsgView.setText(R.string.error_description);
        mErrorMsgView.setVisibility(View.VISIBLE);
    }
}
