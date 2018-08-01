/*
 * Copyright (C) Hanwha Systems Corp. 2018. All rights reserved.
 *
 * This software is covered by the license agreement between
 * the end user and Hanwha Systems Corp. and may be
 * used and copied only in accordance with the terms of the
 * said agreement.
 *
 * Hanwha Systems Corp. assumes no responsibility or
 * liability for any errors or inaccuracies in this software,
 * or any consequential, incidental or indirect damage arising
 * out of the use of the software.
 */

package com.hanwha.libhsp_permission;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 5. 31.. <p/>
 */
public final class PermissionParams {
    /** request code */
    final int reqCode;

    /** 타깃이 되는 activity */
    final FragmentActivity activity;

    /** 허용해야할 퍼미션 목록 */
    final List<String> permissions;

    /** 결과를 전달할 리스너 */
    final OnPermissionListener listener;

    private PermissionParams(final Builder builder) {
        this.reqCode     = builder.reqCode;
        this.activity    = builder.activity;
        this.permissions = builder.permissions;
        this.listener    = builder.listener;
    }

    public String[] toArrayPermissions() {
        return permissions.toArray(new String[permissions.size()]);
    }

    public int reqCode() {
        return reqCode;
    }

    public FragmentActivity activity() {
        return activity;
    }

    public static Builder builder() {
        return new Builder();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // Builder
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public static final class Builder {
        private int reqCode = 0;
        private FragmentActivity activity;
        private List<String> permissions = new ArrayList<>();
        private OnPermissionListener listener;

        public Builder reqCode(final int reqCode) {
            this.reqCode = reqCode;
            return this;
        }

        public Builder activity(@NonNull final FragmentActivity activity) {
            this.activity = activity;
            return this;
        }

        public Builder activity(@NonNull final Activity activity) {
            this.activity = (FragmentActivity) activity;
            return this;
        }

        public Builder permission(@NonNull final String permission) {
            permissions.add(permission);
            return this;
        }

        public Builder permissions(@NonNull final String[] permissions) {
            this.permissions.addAll(Arrays.asList(permissions));
            return this;
        }

        public Builder listener(@NonNull final OnPermissionListener listener) {
            this.listener = listener;
            return this;
        }

        public PermissionParams build() {
            return new PermissionParams(this);
        }
    }
}
