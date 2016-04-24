package com.vojkovladimir.gallery.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vojkovladimir.gallery.R;

/**
 * @author vojkovladimir.
 */
public class EmptyView extends RelativeLayout {

    private final ImageView mIvIcon;
    private final TextView mTvText;

    public EmptyView(@NonNull Context context) {
        this(context, null);
    }

    public EmptyView(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_empty, this);

        mIvIcon = (ImageView) findViewById(R.id.iv_icon);
        mTvText = (TextView) findViewById(R.id.tv_text);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EmptyView);

        mIvIcon.setImageDrawable(a.getDrawable(R.styleable.EmptyView_image));
        mIvIcon.setColorFilter(a.getColor(R.styleable.EmptyView_imageTint, Color.BLACK));
        mTvText.setText(a.getText(R.styleable.EmptyView_text));
        mTvText.setTextColor(a.getColor(R.styleable.EmptyView_textColor, Color.BLACK));

        a.recycle();
    }

    public void setIcon(@DrawableRes int resId) {
        mIvIcon.setImageResource(resId);
    }

    public void setText(@StringRes int resId) {
        mTvText.setText(resId);
    }

}
