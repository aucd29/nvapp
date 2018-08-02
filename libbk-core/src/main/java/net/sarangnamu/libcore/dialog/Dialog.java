package net.sarangnamu.libcore.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import net.sarangnamu.libcore.Invoke;
import net.sarangnamu.libcore.SharedPref;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 8. 1. <p/>
 */
public class Dialog {
    private static final Logger mLog = LoggerFactory.getLogger(Dialog.class);

    private static final String DIALOG_PATH = "BK-DIALOG-PATH";

    public static void classPath(@NonNull Context context, @NonNull String path) {
        SharedPref pref = SharedPref.create(context);
        pref.set(DIALOG_PATH, path);
    }

    public static android.app.Dialog show(@NonNull DialogParams params) {
        SharedPref pref  = SharedPref.create(params.activity);
        String classpath = pref.get(DIALOG_PATH, null);
        IDialog dialog;

        if (TextUtils.isEmpty(classpath)) {
            dialog = new BaseDialog();
        } else {
            try {
                dialog = Invoke.clazz(classpath, new Class[]{});

                if (mLog.isDebugEnabled()) {
                    mLog.debug("DIALOG CLASS PATH : " + classpath);
                }
            } catch (Exception e) {
                e.printStackTrace();

                mLog.error("ERROR: " + e.getMessage());
                dialog = new BaseDialog();
            }
        }

        return dialog.show(params);
    }
}
