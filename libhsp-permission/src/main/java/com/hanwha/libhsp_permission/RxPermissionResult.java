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

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 5. 31.. <p/>
 */
public final class RxPermissionResult {
    public final int code;
    public final boolean result;

    public RxPermissionResult(int code, boolean result) {
        this.code   = code;
        this.result = result;
    }
}
