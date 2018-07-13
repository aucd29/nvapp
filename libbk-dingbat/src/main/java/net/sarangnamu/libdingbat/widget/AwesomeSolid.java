package net.sarangnamu.libdingbat.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import net.sarangnamu.libdingbat.AwesomeSolidFontLoader;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 4. <p/>
 */
public class AwesomeSolid extends AppCompatTextView {
    public AwesomeSolid(Context context) {
        super(context);
        initLayout();
    }

    public AwesomeSolid(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public AwesomeSolid(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout();
    }

    protected void initLayout() {
        setTypeface(AwesomeSolidFontLoader.create(getContext()));
    }
}
