package net.sarangnamu.nvapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import net.sarangnamu.libcore.Json;
import net.sarangnamu.nvapp.BuildConfig;
import net.sarangnamu.nvapp.callback.MainCallback;
import net.sarangnamu.nvapp.callback.NvLoginCallback;
import net.sarangnamu.nvapp.model.local.nvlogin.NvLogin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 20. <p/>
 */
public class NvLoginViewModel extends AndroidViewModel {
    private static final Logger mLog = LoggerFactory.getLogger(NvLoginViewModel.class);

    private static final String API_URL             = "https://openapi.naver.com/v1/nid/me";
    private static String OAUTH_CLIENT_ID;
    private static String OAUTH_CLIENT_SECRET;
    private static String OAUTH_CLIENT_NAME;

    private OAuthLogin mLoginInstance = OAuthLogin.getInstance();

    public MainCallback mMainCallback;
    public NvLoginCallback mNvLoginCallback;
    public CompositeDisposable mDisposable;

    private MutableLiveData<String> mAccessToken  = new MutableLiveData<>();
    private MutableLiveData<String> mRefreshToken = new MutableLiveData<>();
    private MutableLiveData<String> mTokenType    = new MutableLiveData<>();
    private MutableLiveData<Long> mExpiredTime    = new MutableLiveData<>();
    private MutableLiveData<String> errorCode     = new MutableLiveData<>();
    private MutableLiveData<String> errorMsg      = new MutableLiveData<>();

    // nv 에서 제공하는 샘플은 메모리 닉 발생 가능성 존재. 다행이 루퍼는 있네
    private OAuthLoginHandler mHandler = new OAuthLoginHandler(Looper.getMainLooper()) {
        @Override
        public void run(boolean result) {
            if (result) {
                final String accessToken  = mLoginInstance.getAccessToken(getApplication());
                final String refreshToken = mLoginInstance.getRefreshToken(getApplication());
                final String tokenType    = mLoginInstance.getTokenType(getApplication());
                final Long expiredTime    = mLoginInstance.getExpiresAt(getApplication());

                mAccessToken.setValue(accessToken);
                mRefreshToken.setValue(refreshToken);
                mTokenType.setValue(tokenType);
                mExpiredTime.setValue(expiredTime);

                if (mLog.isDebugEnabled()) {
                    mLog.debug("ACCESS TOKEN  : " + accessToken + "\n" +
                               "REFRESH TOKEN : " + refreshToken + "\n" +
                               "TOKEN TYPE    : " + tokenType + "\n" +
                               "EXPIRED TIME  : " + expiredTime);
                }
            } else {
                String code = mLoginInstance.getLastErrorCode(getApplication()).getCode();
                String msg  = mLoginInstance.getLastErrorDesc(getApplication());

                errorCode.setValue(code);
                errorMsg.setValue(msg);

                mLog.error("LOGIN ERROR\n" +
                           "====\n" +
                           "CODE : " + code + "\n" +
                           "MSG  : " + msg);
            }
        }
    };

    public NvLoginViewModel(@NonNull Application application) {
        super(application);

        try {
            // assets 의 경우 debug 와 release 를 구분하여 파일을 정의할 수 있는데
            // 데이터를 숨겨야할 필요가 존재한 github 와 같은데 사용하기 유리하다.
            // debug 쪽에 실제 데이터를 넣어두고 release 쪽에는 빈 데이터를 나두면 개발하는 동안
            // 데이터 유출 문제가 없음
            NvLogin nvlogin = Json.parse(application.getAssets().open("nvlogin.json"), NvLogin.class);

            OAUTH_CLIENT_ID     = nvlogin.id;
            OAUTH_CLIENT_SECRET = nvlogin.secret;
            OAUTH_CLIENT_NAME   = nvlogin.name;

            if (mLog.isDebugEnabled()) {
                mLog.debug("\nID : " + nvlogin.id + "\nSECRET : " + nvlogin.secret + "\nNAME: " + nvlogin.name);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mLog.error("ERROR: " + e.getMessage());
        }

        if (BuildConfig.DEBUG) {
            mLoginInstance.showDevelopersLog(true);
        }

        if (mLog.isDebugEnabled()) {
            mLog.debug("INIT LOGIN : " + mLoginInstance.getVersion());
        }

        mLoginInstance.init(getApplication(), OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
    }

    public void login() {
        if (mNvLoginCallback == null) {
            mLog.error("ERROR: mMainCallback == null");
            return ;
        }

        mNvLoginCallback.login(mLoginInstance, mHandler);
    }

    public void logout() {
        mDisposable.add(Observable.just(mLoginInstance)
            .subscribeOn(Schedulers.io())
            .subscribe(nv -> {
                boolean res = nv.logoutAndDeleteToken(getApplication());
                if (!res) {
                    String code = mLoginInstance.getLastErrorCode(getApplication()).getCode();
                    String msg  = mLoginInstance.getLastErrorDesc(getApplication());

                    mLog.error("LOGOUT ERROR\n" +
                        "====\n" +
                        "CODE : " + code + "\n" +
                        "MSG  : " + msg);
                }
            }));
    }

    public void api() {
        final String url         = API_URL;
        final String accessToken = mAccessToken.getValue();

        mDisposable.add(Observable.just(mLoginInstance)
            .subscribeOn(Schedulers.io())
            .subscribe(nv -> {
                String response = nv.requestApi(getApplication(), accessToken, url);

                if (mLog.isDebugEnabled()) {
                    mLog.debug("RESPONSE\n" + response);
                }

            }));
    }
}
