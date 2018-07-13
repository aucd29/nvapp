package net.sarangnamu.common.util;

import android.arch.lifecycle.ViewModel;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 13. <p/>
 */
public class Invoke {
    private static final Logger mLog = LoggerFactory.getLogger(Invoke.class);

    public static void method(@NonNull ViewDataBinding binding, @NonNull String name, @NonNull ViewModel vmodel) {
        try {
            Method method = binding.getClass().getDeclaredMethod(name,
                new Class<?>[]{ vmodel.getClass() });
            method.invoke(binding, new Object[]{ vmodel });
        } catch (Exception e) {
            e.printStackTrace();
            mLog.error("ERROR: " + e.getMessage());
        }
    }
}
