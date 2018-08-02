package net.sarangnamu.libcore.dialog;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 8. 1. <p/>
 */
public class BaseDialog implements IDialog {
    @Override
    public android.app.Dialog show(@NonNull DialogParams params) {
        AlertDialog.Builder bd = new AlertDialog.Builder(params.activity);

        if (!TextUtils.isEmpty(params.title)) {
            bd.setTitle(params.title);
        }

        if (!TextUtils.isEmpty(params.message)) {
            bd.setMessage(params.message);
        }

        if (!TextUtils.isEmpty(params.positiveText)) {
            bd.setPositiveButton(params.positiveText, (dialogInterface, i) -> {
                if (params.listener != null) {
                    params.listener.onResult(true, dialogInterface);
                }

                dialogInterface.dismiss();
            });
        }

        if (!TextUtils.isEmpty(params.negativeText) && params.confirm) {
            bd.setNegativeButton(params.negativeText, (dialogInterface, i) -> {
                if (params.listener != null) {
                    params.listener.onResult(false, dialogInterface);
                }

                dialogInterface.dismiss();
            });
        }

        AlertDialog dlg = bd.create();
        dlg.show();

        return dlg;
    }
}
