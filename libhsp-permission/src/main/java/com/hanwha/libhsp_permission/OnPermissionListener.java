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

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2016. 5. 26.. <p/>
 *
 * 퍼미션 처리 결과를 전달 받는다
 */
public interface OnPermissionListener {
    /**
     * 퍼미션 처리 결과를 전달 받는다
     * @param reqCode 퍼미션 확인 요청 시 전달 받았던 request code
     * @param result 퍼미션이 허락된 상태이면 true 아니면 false 를 전달 한다
     */
    void onResult(final int reqCode, final boolean result);
}
