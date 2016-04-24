package com.vojkovladimir.gallery.adapter;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.vojkovladimir.gallery.interfaces.ObjectsReceiver;
import com.vojkovladimir.gallery.viewholder.BaseViewHolder;

import java.lang.ref.WeakReference;

/**
 * @author vojkovladimir.
 */
public abstract class CursorRecyclerViewAdapter
        extends RecyclerView.Adapter<BaseViewHolder<Cursor>> {

    private Cursor mCursor;
    private boolean mDataValid;
    private int mRowIdColumn;

    private DataSetObserver mDataSetObserver;
    protected final WeakReference<ObjectsReceiver> mReceiver;

    public CursorRecyclerViewAdapter(@Nullable Cursor cursor,
                                     @NonNull ObjectsReceiver receiver) {
        mReceiver = new WeakReference<>(receiver);
        mCursor = cursor;
        mDataValid = cursor != null;
        mRowIdColumn = mDataValid ? mCursor.getColumnIndex(BaseColumns._ID) : -1;
        mDataSetObserver = new NotifyingDataSetObserver();
        setHasStableIds(true);
        if (mCursor != null) {
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
    }

    public CursorRecyclerViewAdapter(@NonNull ObjectsReceiver receiver) {
        this(null, receiver);
    }

    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    public int getItemCount() {
        return mDataValid && mCursor != null ? mCursor.getCount() : 0;
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null && mCursor.moveToPosition(position)) {
            return mCursor.getLong(mRowIdColumn);
        }

        return super.getItemId(position);
    }

    public void onBindViewHolder(@NonNull BaseViewHolder<Cursor> viewHolder, @NonNull Cursor cursor) {
        viewHolder.bind(cursor);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<Cursor> viewHolder, int position) {
        moveCursorToPosition(position);
        onBindViewHolder(viewHolder, mCursor);
    }

    protected void moveCursorToPosition(int position) throws IllegalStateException {
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
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

        if (mCursor == null) {
            mRowIdColumn = -1;
            mDataValid = false;

            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        } else {
            if (mDataSetObserver != null) {
                mCursor.registerDataSetObserver(mDataSetObserver);
            }
            mRowIdColumn = newCursor.getColumnIndexOrThrow(BaseColumns._ID);
            mDataValid = true;

            notifyDataSetChanged();
        }

        return oldCursor;
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
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
            notifyItemRangeRemoved(0, getItemCount());
        }
    }

}
