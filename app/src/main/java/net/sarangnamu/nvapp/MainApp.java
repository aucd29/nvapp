package net.sarangnamu.nvapp;

import android.app.Application;
import android.content.Context;

import net.sarangnamu.nvapp.model.Model;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 6. 29. <p/>
 */
public class MainApp extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        Model.get();
    }
}
