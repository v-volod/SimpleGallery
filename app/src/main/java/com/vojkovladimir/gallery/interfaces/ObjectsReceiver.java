package com.vojkovladimir.gallery.interfaces;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

/**
 * @author vojkovladimir.
 */
public interface ObjectsReceiver {
    void onObjectsReceive(@IdRes int id, @NonNull Object... objects);
}
