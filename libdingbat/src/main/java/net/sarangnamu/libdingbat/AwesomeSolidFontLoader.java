package net.sarangnamu.libdingbat;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 4. <p/>
 */
public class AwesomeSolidFontLoader {
    private static final String FONT_PATH = "fonts/";
    private static final String FONT_NAME = "awesome_free_solid.otf";

    public static Typeface create(@NonNull Context context) {
        return Typeface.createFromAsset(context.getAssets(), FONT_PATH + FONT_NAME);
    }
}
