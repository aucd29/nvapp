package net.sarangnamu.nvapp;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import net.sarangnamu.nvapp.model.DataManager;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 6. 29. <p/>
 */
public class MainApp extends Application {
    public static Context context;

    public static int screenX;
    public static int screenY;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        screenSize();
        DataManager.get();
    }

    private void screenSize() {
        Point pt = new Point();
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getSize(pt);

        screenX = pt.x;
        screenY = pt.y;
    }
}
