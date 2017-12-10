package com.example.walinnsinnovation.wedapp.Libraries;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.example.walinnsinnovation.wedapp.Fragments.TwitterFragment;

/**
 * Created by walinnsinnovation on 05/12/17.
 */

public class ObservableWebView extends WebView {
    private  OnScrollChangedCallback mOnScrollChangedCallback;

    public ObservableWebView(Context context) {
        super(context);
    }
    public ObservableWebView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ObservableWebView(final Context context, final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        System.out.println("Observe scroll :" + oldl +"....new"+ oldt);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public void onScreenStateChanged(int screenState) {
        System.out.println("Observe scroll screenState :" + screenState);

        super.onScreenStateChanged(screenState);
    }
    public  ObservableWebView.OnScrollChangedCallback getOnScrollChangedCallback()
    {
        System.out.println("Observe scroll screenState :" + "callback");

        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback)
    {
        System.out.println("Observe scroll screenState :" + "Change call back");

        mOnScrollChangedCallback = onScrollChangedCallback;
    }


    public static interface OnScrollChangedCallback
    {
        public void onScroll(int l, int t);
    }

}
