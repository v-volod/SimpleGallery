package com.vojkovladimir.gallery.adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import uk.co.senab.photoview.PhotoView;

/**
 * @author vojkovladimir.
 */
public class ImagesPagesAdapter extends CursorPagerAdapter {

    private final RequestManager mRequestManager;

    public ImagesPagesAdapter(@NonNull Context context) {
        mRequestManager = Glide.with(context.getApplicationContext());
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, Cursor cursor, int position) {
        final PhotoView view = new PhotoView(container.getContext());
        mRequestManager.load(cursor.getString(cursor.getColumnIndex(Media.DATA))).into(view);
        container.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        return view;
    }

}
