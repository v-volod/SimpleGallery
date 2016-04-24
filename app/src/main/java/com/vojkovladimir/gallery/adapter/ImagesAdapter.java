package com.vojkovladimir.gallery.adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.vojkovladimir.gallery.interfaces.ObjectsReceiver;
import com.vojkovladimir.gallery.viewholder.ImageItemViewHolder;

/**
 * @author vojkovladimir.
 */
public class ImagesAdapter extends CursorRecyclerViewAdapter {

    public ImagesAdapter(@NonNull ObjectsReceiver receiver) {
        super(receiver);
    }

    @Override
    public ImageItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageItemViewHolder(parent, mReceiver);
    }
}
