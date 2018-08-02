package net.sarangnamu.libcore;

import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 8. 1. <p/>
 */
public class Invoke {
    private static final Logger mLog = LoggerFactory.getLogger(Invoke.class);

    public static <T> T clazz(@NonNull final String classpath,
                              final Class<?>[] argList, final Object... args) throws Exception {
        try {
            final Class<T> clazz = (Class<T>) Class.forName(classpath);
            if (clazz.isInterface()) {
                throw new Exception("isInterface");
            }

            if (args.length > 0) {
                Constructor<T> constructor = clazz.getDeclaredConstructor(argList);
                return constructor.newInstance(args);
            } else {
                return clazz.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mLog.error("ERROR: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public static void method(final Object obj, @NonNull String name,
                              Class<?>[] types, Object... args) {
        try {
            Method method = obj.getClass().getDeclaredMethod(name, types);
            method.invoke(obj, args);
        } catch (Exception e) {
            e.printStackTrace();
            mLog.error("ERROR: " + e.getMessage());
        }
    }

    public static void method(final Object obj, @NonNull String name,
                              Object argv) {
        try {
            Method method = obj.getClass().getDeclaredMethod(name,
                new Class<?>[] { argv.getClass() });
            method.invoke(obj, new Object[] { argv });
        } catch (Exception e) {
            e.printStackTrace();
            mLog.error("ERROR: " + e.getMessage());
        }
    }
}
