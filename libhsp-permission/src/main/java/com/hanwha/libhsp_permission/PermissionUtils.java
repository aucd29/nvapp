/*
 * Copyright (C) Hanwha Systems Corp. 2016. All rights reserved.
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
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2016. 5. 26.. <p/>
 *
 * <pre>
 * {@code
    String[] permissions = new String[] { android.Manifest.permission.READ_PHONE_STATE };

    PermissionUtils.checkPermissions(HoneMobileActivity.this, permissions, 0,
        (reqCode, result) -> {
            if (result) {
                // TODO
            } else {
                mLog.error("PERMISSION ERROR");
            }
        }
    });

    or

    RxPermissions.params(PermissionParams.builder()
        .activity(HoneMobileActivity.this)
        .permissions(permissions)
        .reqCode(PermissionUtils.REQ_MAIN).build())
        .observeOn(Schedulers.computation())
        .subscribe(consumer -> {
            if (mLog.isDebugEnabled()) {
                mLog.debug("PERMISSION RESULT " + consumer.result);
            }

            // TODO
        });
 * }
 * </pre>
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class PermissionUtils {
    public static final int REQ_MAIN = 1;

    /**
     * 다수개의 퍼미션이 허락 된 상태인지 확인 한다
     * @param context application context
     * @param permissions 검사할 퍼미션들
     * @return 퍼미션이 허락된 상태이면 true 아니면 false
     */
    public static boolean checkSelfPermission(@NonNull final Context context,
                                              @NonNull final String[] permissions) {
        boolean permissionResult = true;
        for (String permission : permissions) {
            if (!checkSelfPermission(context, permission)) {
                permissionResult = false;
                break;
            }
        }

        return permissionResult;
    }

    /**
     * 다수개의 퍼미션이 허락 된 상태인지 확인 한다
     * @param params permission parameter
     * @return 퍼미션이 허락된 상태이면 true 아니면 false
     */
    public static boolean checkSelfPermission(@NonNull final PermissionParams params) {
        return checkSelfPermission(params.activity,
            params.permissions.toArray(new String[params.permissions.size()]));
    }

    /**
     * 퍼미션이 허락 된 상태인지 확인 한다
     * @param context application context
     * @param permission 검사할 퍼미션
     * @return 퍼미션이 허락된 상태이면 true 아니면 false
     */
    static boolean checkSelfPermission(@NonNull final Context context, @NonNull final String permission) {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1 ||
                ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * checkPermissions(@NonNull PermissionParams params) 을 이용하세요
     *
     * @param activity application activity
     * @param permissions 검사할 퍼미션들
     * @param reqCode 요청 코드
     * @param listener 퍼미션 결과를 전달 받을 리스너
     */
    @Deprecated
    public static void checkPermissions(@NonNull final Activity activity,
                                        @NonNull final String[] permissions,
                                        final int reqCode,
                                        @NonNull final OnPermissionListener listener) {

        checkPermissions(PermissionParams.builder().reqCode(reqCode)
            .activity(activity).permissions(permissions).listener(listener).build());
    }

    /**
     * 외부에서 퍼미션이 허락되어 있는지 확인하기 위한 메소드, 사용자는 이 메소드를 이용해서 퍼미션에 대한 코드를 처리할 수 있다
     *
     * @param params 퍼미션 파라메터
     */
    public static void checkPermissions(@NonNull final PermissionParams params) {
        if (!checkSelfPermission(params.activity, params.toArrayPermissions())) {
            final HspPermissionFragment frgmt = permissionFragment(params.activity);
            frgmt.setObserver(null);
            frgmt.requestPermissions(params);
            return ;
        }

        if (params.listener != null) {
            params.listener.onResult(params.reqCode, true);
        }
    }

    /**
     * 퍼미션을 처리해야할 fragment 를 반환 한다. 이미 등록되어 있으면 생성된 내용을 반환 하고 없으면 신규로 생성한다.
     * @param activity application activity
     * @return HspPermissionFragment value
     */
    static @NonNull HspPermissionFragment permissionFragment(@NonNull final FragmentActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        HspPermissionFragment frgmt = (HspPermissionFragment) manager
                .findFragmentByTag(HspPermissionFragment.class.getName());

        if (frgmt != null) {
            return frgmt;
        }

        frgmt = new HspPermissionFragment();

        // https://developer.android.com/guide/components/fragments?hl=ko
        // commitAllowingStateLoss : 커밋을 잃어버려도 상관하지 않음
        manager.beginTransaction()
                .add(frgmt, HspPermissionFragment.class.getName())
                .commitAllowingStateLoss();

        // executePendingTransactions : 즉시 실행 시킨다.
        manager.executePendingTransactions();

        return frgmt;
    }
}
