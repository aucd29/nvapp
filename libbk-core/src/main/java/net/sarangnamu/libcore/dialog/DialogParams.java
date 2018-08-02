package net.sarangnamu.libcore.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import net.sarangnamu.libcore.OnResultListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 8. 1. <p/>
 */

public final class DialogParams {
    private static final Logger mLog = LoggerFactory.getLogger(DialogParams.class);

    public final String positiveText;
    public final String negativeText;
    public final String message;
    public final String title;
    public final boolean confirm;
    public final Activity activity;
    public final OnResultListener<DialogInterface> listener;

    private DialogParams(final Builder builder) {
        this.activity     = builder.activity;
        this.listener     = builder.listener;
        this.positiveText = builder.positiveText;
        this.negativeText = builder.negativeText;
        this.message      = builder.message;
        this.title        = builder.title;
        this.confirm      = builder.confirm;
    }

    public static Builder builder(@NonNull Activity context) {
        return new Builder(context);
    }

    public final static class Builder {
        //private int no;
        private OnResultListener<DialogInterface> listener = null;
        private String positiveText;
        private String negativeText;
        private String message;
        private String title;
        private boolean confirm = false;
        private final Activity activity;

        private Builder(@NonNull Activity activity) {
            this.activity     = activity;
            this.positiveText = activity.getString(android.R.string.ok);
        }

        public Builder listener(OnResultListener<DialogInterface> listener) {
            this.listener = listener;
            return this;
        }

        public Builder positiveText(@NonNull String positiveText) {
            this.positiveText = positiveText;
            return this;
        }

        public Builder positiveText(@StringRes int resid) {
            this.positiveText = activity.getString(resid);
            return this;
        }

        public Builder negativeText(@NonNull String negativeText) {
            this.negativeText = negativeText;
            this.confirm = true;
            return this;
        }

        public Builder negativeText(@StringRes int resid) {
            this.negativeText = activity.getString(resid);
            this.confirm = true;
            return this;
        }

        public Builder message(@NonNull String message) {
            this.message = message;
            return this;
        }

        public Builder message(@StringRes int resid) {
            this.message = activity.getString(resid);
            return this;
        }

        public Builder title(@NonNull String title) {
            this.title = title;
            return this;
        }

        public Builder title(@StringRes int resid) {
            this.title = activity.getString(resid);
            return this;
        }

        public Builder confirm() {
            this.confirm      = true;
            this.negativeText = activity.getString(android.R.string.cancel);

            return this;
        }

        public DialogParams build() {
            return new DialogParams(this);
        }
    }
}

