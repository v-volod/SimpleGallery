package com.vojkovladimir.gallery.activity;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.vojkovladimir.gallery.BuildConfig;
import com.vojkovladimir.gallery.R;
import com.vojkovladimir.gallery.adapter.ImagesPagesAdapter;
import com.vojkovladimir.gallery.content.ImagesLoader;
import com.vojkovladimir.gallery.view.DepthPageTransformer;

public class ViewImageActivity extends AppCompatActivity
        implements LoaderCallbacks<Cursor> {

    public static final String KEY_CURRENT_ITEM = BuildConfig.APPLICATION_ID + "KEY_CURRENT_ITEM";

    private static final int NO_POSITION = -1;

    private ViewPager mViewPager;
    private ImagesPagesAdapter mAdapter;

    private MenuItem mMiDelete;

    private int mPage;
    private boolean mSmoothScroll;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_images);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        mAdapter = new ImagesPagesAdapter(this);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        if (savedInstanceState == null) {
            mPage = getIntent().getIntExtra(KEY_CURRENT_ITEM, NO_POSITION);
        } else {
            mPage = savedInstanceState.getInt(KEY_CURRENT_ITEM, NO_POSITION);
        }
        mSmoothScroll = false;

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_ITEM, mViewPager.getCurrentItem());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_image, menu);

        mMiDelete = menu.findItem(R.id.action_delete);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mMiDelete.setVisible(mAdapter.getCount() > 0);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_delete:
                new AlertDialog.Builder(this).setTitle(R.string.question_delete)
                        .setMessage(R.string.question_delete_description)
                        .setPositiveButton(R.string.delete, new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteCurrent();
                            }
                        }).setNegativeButton(R.string.cancel, null).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteCurrent() {
        mPage = mViewPager.getCurrentItem();
        mSmoothScroll = true;

        final long id = mAdapter.getItemId(mViewPager.getCurrentItem());
        final Uri uri = ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, id);

        getContentResolver().delete(uri, null, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new ImagesLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        if (mPage != NO_POSITION) {
            mViewPager.setAdapter(mAdapter);
            mViewPager.setCurrentItem(mPage, mSmoothScroll);
            mPage = NO_POSITION;
            mSmoothScroll = false;
        }

        invalidateOptionsMenu();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
        finish();
    }

}
