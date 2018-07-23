package net.sarangnamu.nvapp.callback;

import android.support.annotation.NonNull;

import net.sarangnamu.libfragment.FragmentParams;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 20. <p/>
 */
public interface FragmentCallback {
    void showFragment(@NonNull FragmentParams params);
    void hideFragment();
}
