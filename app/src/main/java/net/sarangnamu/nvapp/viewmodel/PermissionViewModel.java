package net.sarangnamu.nvapp.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.hanwha.libhsp_permission.PermissionParams;
import com.hanwha.libhsp_permission.RxPermissionResult;
import com.hanwha.libhsp_permission.RxPermissions;

import net.sarangnamu.libcore.dialog.Dialog;
import net.sarangnamu.libcore.dialog.DialogParams;
import net.sarangnamu.libtutorial.viewmodel.TutorialViewModel;
import net.sarangnamu.nvapp.R;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 8. 1. <p/>
 */
public class PermissionViewModel extends AndroidViewModel {
    private static final Logger mLog = LoggerFactory.getLogger(PermissionViewModel.class);

    public CompositeDisposable disposable;

    public PermissionViewModel(@NonNull Application application) {
        super(application);
    }

    public void confirm(Activity activity, TutorialViewModel vmodel) {
        Dialog.show(DialogParams.builder(activity)
            .message(R.string.tutorial_permission_description)
            .confirm()
            .listener((res, dlgInterface) -> {
                if (res) {
                    disposable.add(RxPermissions.create(PermissionParams.builder(activity)
                        .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .ignoreInfoDialog()
                        .build())
                        .map((Function<RxPermissionResult, Object>) rxPermissionResult -> rxPermissionResult.result)
                        .subscribe(result -> {
                            if (mLog.isDebugEnabled()) {
                                mLog.debug("PERMISSION RES : " + result);
                            }

                            vmodel.next();
                        }));
                } else {
                    vmodel.next();
                }
            })
            .build());
    }
}
