package com.vojkovladimir.gallery.content;

import android.content.Context;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;

/**
 * @author vojkovladimir.
 */
public class ImagesLoader extends CursorLoader {

    private static final String[] PROJECTION = {Media._ID, Media.DATA};

    public ImagesLoader(@NonNull Context context) {
        super(context, Media.EXTERNAL_CONTENT_URI, PROJECTION, null, null, Media.DATE_ADDED + " DESC");
    }

}
