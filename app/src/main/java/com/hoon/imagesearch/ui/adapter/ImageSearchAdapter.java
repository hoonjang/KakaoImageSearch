package com.hoon.imagesearch.ui.adapter;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hoon.imagesearch.data.Image;
import com.hoon.imagesearch.ui.adapter.viewholder.FooterViewHolder;
import com.hoon.imagesearch.ui.adapter.viewholder.ImageViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 이미지 검색 결과 목록을 출력하기 위한 Adapter
 */
public class ImageSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private List<Image> mImages = new ArrayList<>(0);

    private boolean mIsProgressActive = false;
    private boolean mIsLastImageIndicatorActive = false;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, @ViewType int viewType) {
        switch (viewType) {
            case ViewType.IMAGE:
                return ImageViewHolder.newInstance(parent);
            case ViewType.FOOTER:
                return FooterViewHolder.newInstance(parent);
            default:
                throw new IllegalArgumentException("invalid ViewType " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ViewType.IMAGE:
                ((ImageViewHolder) holder).bind(mImages.get(position));
                break;
            case ViewType.FOOTER:
                ((FooterViewHolder) holder).bind(
                        mIsProgressActive, mIsLastImageIndicatorActive);
                break;
        }
    }

    @Override
    @ViewType
    public int getItemViewType(int position) {
        if (shouldShowFooter()) {
            if (position == mImages.size()) {
                return ViewType.FOOTER;
            }
        }
        return ViewType.IMAGE;
    }

    @Override
    public int getItemCount() {
        int imageCount = mImages.size();
        if (shouldShowFooter()) {
            return imageCount + 1; // for the footer
        } else {
            return imageCount;
        }
    }

    public void addDataSet(@NonNull List<Image> newDataSet) {
        int newDataSetStartPosition = mImages.size();
        mImages.addAll(newDataSet);
        notifyItemRangeChanged(newDataSetStartPosition, newDataSet.size());
    }

    public void clearDataSet() {
        mImages.clear();
        notifyDataSetChanged();
    }

    public void setLoadingIndicator(boolean active) {
        mIsProgressActive = active;
        notifyItemChanged(mImages.size());
    }

    public void setLastImageIndicator(boolean active) {
        mIsLastImageIndicatorActive = active;
        notifyItemChanged(mImages.size());
    }

    private boolean shouldShowFooter() {
        return mIsProgressActive || mIsLastImageIndicatorActive;
    }

    @IntDef({ViewType.IMAGE, ViewType.FOOTER})
    private @interface ViewType {
        int IMAGE = 0;
        int FOOTER = 1;
    }
}
