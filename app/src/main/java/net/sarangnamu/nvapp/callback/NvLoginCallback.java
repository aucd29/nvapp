package net.sarangnamu.nvapp.callback;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 24. <p/>
 */
public interface NvLoginCallback {
    void login(@NonNull final OAuthLogin oauth, @NonNull final OAuthLoginHandler handler);

}
