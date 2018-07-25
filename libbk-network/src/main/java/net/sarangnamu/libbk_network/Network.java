package net.sarangnamu.libbk_network;

import net.sarangnamu.libcore.Json;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 24. <p/>
 */
public class Network {
    private static final long TIMEOUT = 10;

    public void request() {
        Retrofit rf = new Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(Json.mapper()))
            .client(http())
            .build();
    }

    public OkHttpClient http() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        // FIXME https 관련 trust manager 코드 추가해야 함
        return new OkHttpClient().newBuilder()
            .retryOnConnectionFailure(false)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .hostnameVerifier((host, session) -> true)
            .addInterceptor(logging)
            .build();
    }
}
