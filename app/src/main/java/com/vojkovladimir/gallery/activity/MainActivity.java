package com.vojkovladimir.gallery.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vojkovladimir.gallery.R;
import com.vojkovladimir.gallery.fragment.GalleryGridFragment;
import com.vojkovladimir.gallery.interfaces.ObjectsReceiver;

public class MainActivity extends AppCompatActivity
        implements ObjectsReceiver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        final Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_frame);
        if (fragment == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, GalleryGridFragment.newInstance())
                    .commit();
        }
    }

    public void startRootFragment(@NonNull Fragment fragment,
                                  @Nullable FragmentTransactionCallback callback) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.root_frame, fragment);
        if (callback != null) {
            callback.onTransaction(transaction);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onObjectsReceive(@IdRes int id, @NonNull Object... objects) {

    }

    public interface FragmentTransactionCallback {
        void onTransaction(@NonNull FragmentTransaction transaction);
    }
}
