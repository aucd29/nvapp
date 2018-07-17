package net.sarangnamu.common.util;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 17. <p/>
 */
public class DimUtils {
    public static float dpToPixel(@NonNull Context context, float value) {
        return value * context.getResources().getDisplayMetrics().density;
    }

    public static float pixelToDp(@NonNull Context context, float value) {
        return value / context.getResources().getDisplayMetrics().density;
    }

    public static int dpToPixelInt(@NonNull Context context, float value) {
        return (int) dpToPixel(context, value);
    }

    public static int pixelToDpInt(@NonNull Context context, float value) {
        return (int) pixelToDp(context, value);
    }
}
