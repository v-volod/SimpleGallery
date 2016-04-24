package com.vojkovladimir.gallery.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vojkovladimir.gallery.R;
import com.vojkovladimir.gallery.activity.ViewImageActivity;
import com.vojkovladimir.gallery.adapter.ImagesAdapter;
import com.vojkovladimir.gallery.content.ImagesLoader;
import com.vojkovladimir.gallery.view.EmptyView;
import com.vojkovladimir.gallery.viewholder.ImageItemViewHolder;
import com.vojkovladimir.gallery.widget.SpaceItemDecoration;

public class GalleryGridFragment extends BaseFragment
        implements LoaderCallbacks<Cursor> {

    private EmptyView mEmptyView;
    private RecyclerView mRecyclerView;

    private ImagesAdapter mAdapter;

    private int mSpanCount;

    @NonNull
    public static GalleryGridFragment newInstance() {
        return new GalleryGridFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSpanCount = getResources().getInteger(R.integer.span_count_gallery_grid);
        mAdapter = new ImagesAdapter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery_grid, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEmptyView = (EmptyView) view.findViewById(R.id.empty);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        final int spacing = getResources().getDimensionPixelOffset(R.dimen.grid_list_spacing);
        final int halfSpacing = spacing / 2;

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mSpanCount));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(spacing, true, true));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setPadding(halfSpacing, 0, halfSpacing, 0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new ImagesLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        updateAdapter(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        updateAdapter(null);
    }

    private void updateAdapter(@Nullable Cursor data) {
        mAdapter.swapCursor(data);

        final boolean hasData = mAdapter.getItemCount() > 0;
        mEmptyView.setVisibility(hasData ? View.GONE : View.VISIBLE);
        mRecyclerView.setVisibility(hasData ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onObjectsReceive(@IdRes int id, @NonNull Object... objects) {
        switch (id) {
            case ImageItemViewHolder.ID:
                final int position = (int) objects[0];

                final Intent intent = new Intent(getActivity(), ViewImageActivity.class);
                intent.putExtra(ViewImageActivity.KEY_CURRENT_ITEM, position);

                getActivity().startActivity(intent);
                break;

            default:
                super.onObjectsReceive(id, objects);
        }
    }
}
