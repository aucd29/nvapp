package net.sarangnamu.nvapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 10. <p/>
 */
public class MainViewModel extends AndroidViewModel {
    public ObservableField<String> url = new ObservableField<>("https://m.naver.com");
    public ObservableField<WebViewClient> webviewClient = new ObservableField<>();
    public ObservableField<WebChromeClient> webviewChromeClient = new ObservableField<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        webviewClient.set(new WebViewClient() {

        });
        webviewChromeClient.set(new WebChromeClient() {

        });
    }
}
