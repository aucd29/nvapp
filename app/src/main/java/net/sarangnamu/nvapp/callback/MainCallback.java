package net.sarangnamu.nvapp.callback;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 20. <p/>
 */
public interface MainCallback {
    void showNavigation();
    void hideNavigation();

    void login();
    void logout();

    void permission(List<String> permissions);
}
