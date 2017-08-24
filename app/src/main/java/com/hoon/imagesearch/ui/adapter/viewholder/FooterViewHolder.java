package com.hoon.imagesearch.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoon.imagesearch.R;

/**
 * 이미지 검색 결과 목록 끝에 프로그레스 또는 마지막 이미지 표시를 출력하기 위한 ViewHolder
 */
public class FooterViewHolder extends RecyclerView.ViewHolder {

    private View progress;
    private View lastImageIndicator;

    private FooterViewHolder(View itemView) {
        super(itemView);
        progress = itemView.findViewById(R.id.progress);
        lastImageIndicator = itemView.findViewById(R.id.last_image_indicator);
    }

    public static FooterViewHolder newInstance(ViewGroup parent) {
        return new FooterViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.list_image_search_footer, parent, false));
    }

    public void bind(boolean progressActive, boolean lastImageIndicatorActive) {
        progress.setVisibility(progressActive? View.VISIBLE : View.GONE);
        lastImageIndicator.setVisibility(lastImageIndicatorActive? View.VISIBLE : View.GONE);
    }
}
