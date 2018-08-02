package net.sarangnamu.nvapp.view.tutorial;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import net.sarangnamu.libcore.dialog.Dialog;
import net.sarangnamu.libcore.dialog.DialogParams;
import net.sarangnamu.nvapp.MainActivity;
import net.sarangnamu.nvapp.databinding.TutorialPermissionBinding;
import net.sarangnamu.nvapp.viewmodel.PermissionViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 8. 1. <p/>
 */
public class TutorialPermission {
    private static final Logger mLog = LoggerFactory.getLogger(TutorialPermission.class);

    public static void event(@NonNull MainActivity activity, TutorialPermissionBinding binding) {
        if (mLog.isDebugEnabled()) {
            mLog.debug("TUTORIAL PERMISSION");
        }

        PermissionViewModel permissionModel = ViewModelProviders.of(activity).get(PermissionViewModel.class);
        permissionModel.disposable = activity.disposable();

        binding.setPermissionModel(permissionModel);
    }
}
