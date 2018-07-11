package net.sarangnamu.libcore;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 11. <p/>
 */
public interface OnResultListener<T> {
    void onResult(boolean result, T obj);
}
