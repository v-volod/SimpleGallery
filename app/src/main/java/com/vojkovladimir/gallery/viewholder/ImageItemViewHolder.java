package com.vojkovladimir.gallery.viewholder;

import android.database.Cursor;
import android.provider.MediaStore.MediaColumns;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.vojkovladimir.gallery.R;
import com.vojkovladimir.gallery.interfaces.ObjectsReceiver;

import java.lang.ref.WeakReference;

/**
 * @author vojkovladimir.
 */
public class ImageItemViewHolder extends BaseViewHolder<Cursor>
        implements OnClickListener {

    @IdRes
    public static final int ID = R.id.image_item;

    private final ImageView mIvImage;
    private final RequestManager mRequestManager;

    private String mPath;

    public ImageItemViewHolder(@NonNull ViewGroup parent,
                               @NonNull WeakReference<ObjectsReceiver> receiver) {
        super(parent, R.layout.view_image_grid_item, receiver);

        mIvImage = (ImageView) findViewById(R.id.iv_image);
        mRequestManager = Glide.with(getContext());

        itemView.setOnClickListener(this);
    }

    @Override
    public void bind(@NonNull Cursor item) {
        mPath = item.getString(item.getColumnIndex(MediaColumns.DATA));
        mRequestManager.load(mPath).centerCrop().into(mIvImage);
    }

    @Override
    public void onClick(View v) {
        receiveObjects(ID, getAdapterPosition());
    }

}
