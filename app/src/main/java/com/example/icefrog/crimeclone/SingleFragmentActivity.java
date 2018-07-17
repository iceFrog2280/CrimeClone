package com.example.icefrog.crimeclone;

import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by icefrog on 2017/8/14 0014.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {
    public static String TAG = "SingleFragmentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById( R.id.fragment_container);

        if( fragment == null){
            fragment = createrFragment();
            fm.beginTransaction()
                    .add( R.id.fragment_container, fragment)
                    .commit();
        }
    }
    protected abstract Fragment createrFragment();
    @LayoutRes
    protected int getLayoutResId(){
        return R.layout.activity_fragment;
    }
}
