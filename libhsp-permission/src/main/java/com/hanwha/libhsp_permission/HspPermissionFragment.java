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

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import io.reactivex.Observer;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 6. 1.. <p/>
 */
public final class HspPermissionFragment extends Fragment {
    private static final Logger mLog = LoggerFactory.getLogger(HspPermissionFragment.class);

    private static final int RESULT_ACTION_PERMISSION = 19791212;

    /** 퍼미션 처리에 필요한 파라메터 값 */
    private PermissionParams mParams;
    private Observer<? super RxPermissionResult> mObserver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // http://aroundck.tistory.com/2163
        // true 시 생명주기를 따르지 않게되며 RECREATION 시 onCreate 없이 onAttach, onActivityCreated 순으로
        // 호출된다.
        setRetainInstance(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean grantResult = true;

        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                grantResult = false;
                break;
            }
        }

        onPermissionResult(grantResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_ACTION_PERMISSION:
                boolean res = PermissionUtils.checkSelfPermission(mParams);
                if (res) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        Objects.requireNonNull(mParams.listener).onResult(mParams.reqCode, true);
                    } else {
                        if (mParams.listener == null) {
                            throw new NullPointerException();
                        }

                        mParams.listener.onResult(mParams.reqCode, true);
                    }
                }
                break;
        }
    }

    /**
     * 런타임 퍼미션 결과를 처리 한다.
     * @param result 처리 결과
     */
    protected void onPermissionResult(final boolean result) {
        if (mLog.isDebugEnabled()) {
            mLog.debug("PERMISSION RESULT : " + result);
        }

        if (result) {
            if (mObserver == null) {
                mParams.listener.onResult(mParams.reqCode, true);
            } else {
                mObserver.onNext(new RxPermissionResult(mParams.reqCode(), true));
            }

            mObserver = null;
        } else {
            showPermissionDialog();
        }
    }

    public void setObserver(@NonNull final Observer<? super RxPermissionResult> observer) {
        mObserver = observer;
    }

    /**
     * 런타임 퍼미션을 요청 한다.
     * @param params 런타임 퍼미션에 필요한 파라메터 정보
     */
    public void requestPermissions(@NonNull final PermissionParams params) {
        mParams = params;

        if (mLog.isDebugEnabled()) {
            mLog.debug("REQUEST PERMISSION");
        }

        requestPermissions(params.toArrayPermissions(), params.reqCode);
    }

    /**
     * 퍼미션 결과가 negative 일 경우 팝업창을 띄워 퍼미션을 승낙 하도록 유도 한다.
     */
    protected void showPermissionDialog() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("PARAMS : " + mParams);
        }

        if (mParams.reqCode == PermissionUtils.REQ_MAIN) {
            eventNegative();
            return ;
        }

        final String msg = getString(R.string.permission_message);

        new AlertDialog.Builder(mParams.activity)
            .setTitle(R.string.permission_title)
            .setMessage(msg)
            .setNegativeButton(R.string.permission_cancel, (dialog, which) -> {
                dialog.dismiss();
                eventNegative();
            })
            .setPositiveButton(R.string.permission_set, (dialog, which) -> {
                dialog.dismiss();
                eventPositive();
            })
            .setCancelable(false)
            .show();
    }

    /**
     * showPermissionDialog 에서 negative 버튼을 선택 시 발생 시켜야할 이벤트
     */
    private void eventNegative() {
        if (mObserver == null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                Objects.requireNonNull(mParams.listener).onResult(mParams.reqCode, false);
            } else {
                if (mParams.listener == null) {
                    throw new NullPointerException();
                }

                mParams.listener.onResult(mParams.reqCode, false);
            }
        } else {
            mObserver.onNext(new RxPermissionResult(mParams.reqCode(), false));
        }

        mObserver = null;
    }

    /**
     * showPermissionDialog 에서 positive 버튼을 선택 시 발생 시켜야할 이벤트
     */
    private void eventPositive() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mParams.activity.getPackageName()));

        startActivityForResult(intent, RESULT_ACTION_PERMISSION);
    }

    /**
     * 문자열을 반환 한다.
     * @param key 문자열에 해당하는 키 값
     * @return 문자열 값
     */
    protected @Nullable
    String string(@NonNull final String key) {
        if (getContext() == null) {
            return null;
        }

        return getString(getResources().getIdentifier(key, "string", getContext().getPackageName()));
    }
}
