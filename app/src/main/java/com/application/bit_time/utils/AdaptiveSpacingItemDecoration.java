package com.application.bit_time.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdaptiveSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int size;
    private Boolean edgeEnabled;

    public AdaptiveSpacingItemDecoration(int size,Boolean edgeEnabled)
    {
        this.size=size;
        this.edgeEnabled = edgeEnabled;

    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        //super.getItemOffsets(outRect, view, parent, state);

        int standardPadding = 5;
        int position = parent.getChildAdapterPosition(view);
        int itemCount = state.getItemCount();
        boolean isLastPosition = (position == itemCount-1) ? true : false;
        boolean isFirstPosition = (position == 0) ? true : false;

        int sizeBasedOnEdge = standardPadding;
        int sizeBasedOnLastPosition = isLastPosition ? sizeBasedOnEdge : 0;
        int sizeBasedOnFirstPosition = isFirstPosition ? sizeBasedOnEdge : standardPadding;



        outRect.left = sizeBasedOnEdge;
        outRect.top = sizeBasedOnFirstPosition;
        outRect.right = sizeBasedOnEdge;
        outRect.bottom = sizeBasedOnLastPosition;



    }



}
