package net.sarangnamu.libcore.dialog;

import android.app.AlertDialog;
import android.support.annotation.NonNull;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 8. 1. <p/>
 */
public interface IDialog {
    android.app.Dialog show(@NonNull DialogParams params);
}
