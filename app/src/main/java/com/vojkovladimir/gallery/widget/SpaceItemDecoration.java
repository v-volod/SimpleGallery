package com.vojkovladimir.gallery.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author vojkovladimir.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private static final int UNDEFINED = -1;
    private static final int GRID_LAYOUT_MANAGER = 0x1;
    private static final int LINEAR_LAYOUT_MANAGER = 0x2;

    private final int mSpace;
    private final int mHalfSpace;

    private boolean mShowFirstDivider = false;
    private boolean mShowLastDivider = false;

    private int mOrientation = UNDEFINED;
    private int mSpanCount = UNDEFINED;
    private int mLayoutManager = UNDEFINED;

    public SpaceItemDecoration(int spaceInPx) {
        this(spaceInPx, false, false);
    }

    public SpaceItemDecoration(int spaceInPx, boolean showFirstDivider, boolean showLastDivider) {
        mSpace = spaceInPx;
        mHalfSpace = mSpace / 2;
        mShowFirstDivider = showFirstDivider;
        mShowLastDivider = showLastDivider;
    }

    public SpaceItemDecoration(Context ctx, @DimenRes int resId) {
        mSpace = ctx.getResources().getDimensionPixelSize(resId);
        mHalfSpace = mSpace / 2;
    }

    public SpaceItemDecoration(Context ctx, @DimenRes int resId, boolean showFirstDivider,
                               boolean showLastDivider) {
        this(ctx, resId);
        mShowFirstDivider = showFirstDivider;
        mShowLastDivider = showLastDivider;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (mSpace == 0) {
            return;
        }

        if (mLayoutManager == UNDEFINED) {
            mLayoutManager = getLayoutManager(parent);
        }
        if (mLayoutManager == GRID_LAYOUT_MANAGER) {
            if (mOrientation == -1) {
                getOrientation(parent);
            }
            if (mSpanCount == -1) {
                mSpanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            }
            final int position = parent.getChildLayoutPosition(view);
            final int count = parent.getAdapter().getItemCount();
            if (mShowFirstDivider && position < mSpanCount || position >= mSpanCount) {
                outRect.top = mSpace;
            }
            outRect.left = mHalfSpace;
            outRect.right = mHalfSpace;
            if (mShowLastDivider && (count % mSpanCount == 0 && position >= count - mSpanCount
                    || position >= count - count % mSpanCount)) {
                outRect.bottom = mSpace;
            }
        } else if (mLayoutManager == LINEAR_LAYOUT_MANAGER) {
            if (mOrientation == -1) {
                getOrientation(parent);
            }
            final int position = parent.getChildLayoutPosition(view);
            if (position == RecyclerView.NO_POSITION || (position == 0 && !mShowFirstDivider)) {
                return;
            }
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                outRect.top = mSpace;
                if (mShowLastDivider && position == (state.getItemCount() - 1)) {
                    outRect.bottom = mSpace;
                }
            } else {
                outRect.left = mSpace;
                if (mShowLastDivider && position == (state.getItemCount() - 1)) {
                    outRect.right = outRect.left;
                }
            }
        }
    }

    private int getLayoutManager(RecyclerView parent) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            return GRID_LAYOUT_MANAGER;
        } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            return LINEAR_LAYOUT_MANAGER;
        } else {
            throw new IllegalStateException(
                    "DividerItemDecoration can only be used with a LinearLayoutManager.");
        }
    }

    private int getOrientation(RecyclerView parent) {
        if (mOrientation == -1) {
            if (parent.getLayoutManager() instanceof GridLayoutManager) {
                GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
                mOrientation = layoutManager.getOrientation();
            } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
                mOrientation = layoutManager.getOrientation();
            } else {
                throw new IllegalStateException("DividerItemDecoration can only be used with a " +
                        "LinearLayoutManager and GridLayoutManager.");
            }
        }
        return mOrientation;
    }

}
