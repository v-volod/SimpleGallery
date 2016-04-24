package com.vojkovladimir.gallery.fragment;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.vojkovladimir.gallery.interfaces.ObjectsReceiver;

/**
 * @author vojkovladimir.
 */
public class BaseFragment extends Fragment
        implements ObjectsReceiver {

    private ObjectsReceiver mReceiver;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mReceiver = activity instanceof ObjectsReceiver ? (ObjectsReceiver) activity : null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mReceiver = null;
    }

    @Override
    public void onObjectsReceive(@IdRes int id, @NonNull Object... objects) {
        if (mReceiver != null) {
            mReceiver.onObjectsReceive(id, objects);
        }
    }
}
