package com.smartbutler.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.LinearLayout;

/**
 * 事件分发
 */

public class DispatchLinearLayout extends LinearLayout {

    public DispatchEventListener getDispatchEventListener() {
        return mDispatchEventListener;
    }

    public void setDispatchEventListener(DispatchEventListener dispatchEventListener) {
        mDispatchEventListener = dispatchEventListener;
    }

    private DispatchEventListener mDispatchEventListener;

    public DispatchLinearLayout(Context context) {
        super(context);
    }

    public DispatchLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DispatchLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //
    public static interface DispatchEventListener {
        boolean dispatchEvent(KeyEvent event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // 如果不为空，说明调用了
        if (mDispatchEventListener != null) {
            return mDispatchEventListener.dispatchEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }
}
