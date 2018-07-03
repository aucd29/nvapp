package net.sarangnamu.nvapp.model.remote.user;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 6. 29. <p/>
 */
public final class UserInfo implements Serializable {
    public String id;
    public String email;
    public Drawable pic;
}
