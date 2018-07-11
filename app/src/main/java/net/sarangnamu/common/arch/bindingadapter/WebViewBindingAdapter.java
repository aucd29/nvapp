package net.sarangnamu.common.arch.bindingadapter;

import android.annotation.SuppressLint;
import android.databinding.BindingAdapter;
import android.os.Build;
import android.support.annotation.NonNull;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 10. <p/>
 */
public class WebViewBindingAdapter {
    private static final Logger mLog = LoggerFactory.getLogger(WebViewBindingAdapter.class);

    @SuppressLint("SetJavaScriptEnabled")
    @BindingAdapter({"bindUrl", "bindClient", "bindChrome"})
    public static void webviewSetting(@NonNull WebView view, @NonNull String url,
                                      WebViewClient client, WebChromeClient chromeClient) {
        if (mLog.isDebugEnabled()) {
            mLog.debug("WEBVIEW URL : " + url);
        }

        view.setWebViewClient(client);
        view.setWebChromeClient(chromeClient);

        view.getSettings().setTextZoom(100);
        view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        view.getSettings().setAppCacheEnabled(false);
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setDomStorageEnabled(true);
        view.getSettings().setAllowFileAccessFromFileURLs(true);
        view.getSettings().setAllowUniversalAccessFromFileURLs(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            WebView.startSafeBrowsing(view.getContext(), success -> {
                if (!success) {
                    mLog.error("ERROR: Unable to initialize Safe Browsing!");
                }
            });
        }

        view.loadUrl(url);
    }
}
