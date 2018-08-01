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


import android.os.Looper;
import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 5. 31.. <p/>
 */
public final class RxPermissions extends Observable<RxPermissionResult> {
    private static final Logger mLog = LoggerFactory.getLogger(RxPermissions.class);

    private final PermissionParams mParams;

    /**
     * 퍼미션을 체크 하기 위해 클래스를 인스턴스 한다.
     * @param params 퍼미션 체크에 필요한 파라메터
     * @return RxPermissions 객체
     */
    public static RxPermissions params(@NonNull final PermissionParams params) {
        if (mLog.isDebugEnabled()) {
            mLog.debug("CHECK PERMISSION (" + params.permissions.size() + ")");
        }

        return new RxPermissions(params);
    }

    private RxPermissions(@NonNull final PermissionParams params) {
        mParams = params;
    }

    @Override
    protected void subscribeActual(Observer<? super RxPermissionResult> observer) {
        if (!checkLooperThread(observer)) {
            return ;
        }

        final RxPermissionsDisposable disposable = new RxPermissionsDisposable(observer);
        observer.onSubscribe(disposable);

        if (!PermissionUtils.checkSelfPermission(mParams.activity(), mParams.toArrayPermissions())) {
            HspPermissionFragment frgmt = PermissionUtils.permissionFragment(mParams.activity());
            frgmt.setObserver(observer);
            frgmt.requestPermissions(mParams);

            return ;
        }

        observer.onNext(new RxPermissionResult(mParams.reqCode(), true));
    }

    private boolean checkLooperThread(final Observer observer) {
        if (Looper.myLooper() == null) {
            observer.onSubscribe(Disposables.empty());
            observer.onError(new IllegalStateException("Calling thread is not associated with Looper"));
            return false;
        }

        return true;
    }

    private final class RxPermissionsDisposable implements Disposable {
        private final AtomicBoolean disposed = new AtomicBoolean(false);
        private final Observer<? super RxPermissionResult> observer;

        RxPermissionsDisposable(final Observer<? super RxPermissionResult> observer) {
            this.observer = observer;
        }

        @Override
        public void dispose() {
            if (disposed.compareAndSet(false, true)) {
                observer.onComplete();
            }
        }

        @Override
        public boolean isDisposed() {
            return disposed.get();
        }
    }
}
