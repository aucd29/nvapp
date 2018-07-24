package net.sarangnamu.nvapp.model.remote;

import net.sarangnamu.common.net.SocketFactoryProxy;
import net.sarangnamu.libcore.Json;
import net.sarangnamu.nvapp.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 24. <p/>
 */
public class Network {
    private static final String BASE_URL = "http://";

    private static final long TIMEOUT = 10;

    public void request() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        OkHttpClient okhttp = new OkHttpClient().newBuilder()
            .retryOnConnectionFailure(false)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .hostnameVerifier((host, session) -> true)
            .addInterceptor(logging)
            .build();

        Retrofit rf = new Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(Json.mapper()))
            .client(okhttp)
            .build();
    }
}
