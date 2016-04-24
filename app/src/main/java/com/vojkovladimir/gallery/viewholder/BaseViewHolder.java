package com.vojkovladimir.gallery.viewholder;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vojkovladimir.gallery.interfaces.ObjectsReceiver;
import com.vojkovladimir.gallery.util.Utils;

import java.lang.ref.WeakReference;

/**
 * @author vojkovladimir.
 */
public abstract class BaseViewHolder<ITEM_TYPE> extends RecyclerView.ViewHolder {

    @Nullable
    protected final WeakReference<ObjectsReceiver> mReceiver;

    public BaseViewHolder(@NonNull ViewGroup parent, @LayoutRes int layoutResId,
                          @Nullable WeakReference<ObjectsReceiver> receiver) {
        this(inflate(parent, layoutResId), receiver);
    }

    public BaseViewHolder(@NonNull View view, @Nullable WeakReference<ObjectsReceiver> receiver) {
        super(view);
        mReceiver = receiver;
    }

    public abstract void bind(@NonNull ITEM_TYPE item);

    @NonNull
    protected Context getContext() {
        return itemView.getContext();
    }

    @Nullable
    protected View findViewById(@IdRes int id) {
        return itemView == null ? null : itemView.findViewById(id);
    }

    protected static View inflate(@NonNull ViewGroup parent, @LayoutRes int resId) {
        return LayoutInflater.from(parent.getContext()).inflate(resId, parent, false);
    }

    protected boolean receiveObjects(@IdRes int id, @NonNull Object... objects) {
        return Utils.receiveObjects(mReceiver, id, objects);
    }
}
