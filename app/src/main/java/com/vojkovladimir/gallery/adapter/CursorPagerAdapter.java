package com.vojkovladimir.gallery.adapter;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author vojkovladimir.
 */
public abstract class CursorPagerAdapter extends PagerAdapter {

    private Cursor mCursor;
    private boolean mDataValid;

    public CursorPagerAdapter() {
        this(null);
    }

    public CursorPagerAdapter(@Nullable Cursor cursor) {
        mCursor = cursor;
        if (mCursor != null) {
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
    }

    public long getItemId(int position) {
        try {
            moveCursorToPosition(position);

            return mCursor.getInt(mCursor.getColumnIndexOrThrow(BaseColumns._ID));
        } catch (Exception e) {
            return -1;
        }
    }

    public Cursor getCursor() {
        return mCursor;
    }

    private void moveCursorToPosition(int position) {
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        moveCursorToPosition(position);
        return instantiateItem(container, mCursor, position);
    }

    @NonNull
    public abstract Object instantiateItem(ViewGroup container, Cursor cursor, int position);

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mDataValid && mCursor != null ? mCursor.getCount() : 0;
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     */
    public void changeCursor(@Nullable Cursor cursor) {
        final Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     */
    public Cursor swapCursor(@Nullable Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }

        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }

        mCursor = newCursor;
        if (mCursor != null) {
            if (mDataSetObserver != null) {
                mCursor.registerDataSetObserver(mDataSetObserver);
            }
            mDataValid = true;

        } else {
            mDataValid = false;
        }

        notifyDataSetChanged();

        return oldCursor;
    }

    private final DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
        }
    };

}
