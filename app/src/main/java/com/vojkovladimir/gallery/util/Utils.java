package com.vojkovladimir.gallery.util;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vojkovladimir.gallery.interfaces.ObjectsReceiver;

import java.lang.ref.WeakReference;

/**
 * @author vojkovladimir.
 */
public class Utils {

    public static boolean receiveObjects(@Nullable WeakReference<ObjectsReceiver> weakReceiver,
                                         @IdRes int id, @NonNull Object... objects) {
        final ObjectsReceiver receiver = weakReceiver == null ? null : weakReceiver.get();
        final boolean received = receiver != null;
        if (received) {
            receiver.onObjectsReceive(id, objects);
        }
        return received;
    }

}
