package com.hoon.imagesearch.ui.adapter.viewholder;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hoon.imagesearch.R;
import com.hoon.imagesearch.data.Image;

/**
 * 이미지 검색 결과 목록에서 이미지를 표시하기 위한 ViewHolder
 */
public class ImageViewHolder extends RecyclerView.ViewHolder {

    private static final float DEFAULT_ASPECT_RATIO = 1.6f;

    private SimpleDraweeView mDraweeView;

    private ImageViewHolder(View itemView) {
        super(itemView);
        mDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.drawee_view);
    }

    public static ImageViewHolder newInstance(ViewGroup parent) {
        return new ImageViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.list_image_search_image, parent, false));
    }

    public void bind(@Nullable Image imageData) {
        if (imageData != null && imageData.isValid()) {
            mDraweeView.setAspectRatio((float) imageData.getWidth() / imageData.getHeight());
            mDraweeView.setImageURI(imageData.getImageUrl());
        } else {
            mDraweeView.setAspectRatio(DEFAULT_ASPECT_RATIO);
            mDraweeView.setImageURI((String) null);
        }
    }
}
