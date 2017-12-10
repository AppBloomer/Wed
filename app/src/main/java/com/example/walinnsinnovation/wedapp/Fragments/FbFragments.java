package com.example.walinnsinnovation.wedapp.Fragments;


import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.example.walinnsinnovation.wedapp.Libraries.ObservableWebView;
import com.example.walinnsinnovation.wedapp.R;
import com.example.walinnsinnovation.wedapp.ServiceClass.MediaService;
import com.example.walinnsinnovation.wedapp.Utils.Network;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by walinnsinnovation on 05/12/17.
 */

public class FbFragments extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DownloadListener, View.OnKeyListener {
    SwipeRefreshLayout mSwipeView;
    ObservableWebView mWebview;
    View FacebookBackgroundView;
    ImageView facebookLogo;
    private Bundle webViewBundle;
    public static int back = 1;
    private Map<String, Runnable> mCallbackMap ;
    RelativeLayout rel_web;
    protected SharedPreferences sharedPref;
    protected ValueCallback<Uri[]> mUploadMessageA;
    protected ValueCallback<Uri> mUploadMessage;
    protected String mCameraM;
    Network network;
    private ViewTreeObserver.OnScrollChangedListener f4394c;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fb_layout, container, false);
        network = new Network(getActivity());
        mSwipeView = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mWebview =(ObservableWebView) view.findViewById(R.id.FacebookWebView);
        rel_web = (RelativeLayout) view.findViewById(R.id.linear_web);
        FacebookBackgroundView = (View) view.findViewById(R.id.FacebookBackgroundView);
        facebookLogo = (ImageView) view.findViewById(R.id.facebookLogo);
        FacebookBackgroundView.setVisibility(0);
        facebookLogo.setVisibility(0);
        this.mSwipeView.setOnRefreshListener(this);
        this.mCallbackMap = new HashMap<>();

        if(appInstalledOrNot(getString(R.string.facebook_url))){
               // Intent intent = new Intent(getActivity(), MediaService.class);
               // intent.putExtra("media", "fb");
               // getActivity().startService(intent);
            getActivity().startService(new Intent(getActivity(), MediaService.class).setFlags(1));


        }else {
           // loadUrl();
            System.out.println("Sevice Created" + "Service started");
           // getActivity().finish();
            getActivity().startService(new Intent(getActivity(), MediaService.class).setFlags(1));
        }




        return view;
    }
    private void loadUrl(){

        mWebview.setWebViewClient(new webviewClient(FbFragments.this));
        mWebview.setWebChromeClient(new webChromeView(FbFragments.this));
        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.setScrollBarStyle(33554432);
        mWebview.setScrollbarFadingEnabled(true);
        WVSettings();
        String string = getString(R.string.client_identity);
        CharSequence charSequence = Build.MODEL;
        if (charSequence != null) {
            string = string.replace("Nexus 5", charSequence);
        } else {
            string = string.replace("Nexus 5", "Android");
        }
        mWebview.getSettings().setUserAgentString(string);
        mWebview.addJavascriptInterface(this, "WedDownloader");
        if(network.isNetworkAvailable()){
            mWebview.loadUrl(getString(R.string.facebook_url));

            mWebview.loadUrl(getString(R.string.facebook_url));

            mWebview.setOnScrollChangedCallback(new ObservableWebView.OnScrollChangedCallback(){
                public void onScroll(int l, int t){
                    //Do stuff
                    Log.d(TAG,"We Scrolled etc...");
                    System.out.println("Observe scroll main:" + l +"....new"+ t);
                }
            });
        }


    }
    private final Map<String, Runnable> getCallbackMap() {
        return this.mCallbackMap;
    }

    public void onStop() {

        this.mSwipeView.getViewTreeObserver().removeOnScrollChangedListener(this.f4394c);
        System.out.println("Last_url_onstop"+ mWebview.getUrl());
        super.onStop();
    }
    public void onPause() {
        this.mWebview.onPause();
        System.out.println("Last_url_onPause"+ mWebview.getUrl());
        super.onPause();
    }

    public void onResume() {
        mWebview.onResume();
        if(mWebview.getUrl()!=null){
            System.out.println("Last_url_onResume"+ mWebview.getUrl());

            // fb_utl=mWebView.getUrl();
            // mWebView.loadUrl(fb_utl);

        }
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("Observe scroll main:" +"....."+ outState);
        mWebview.saveState(outState);

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        System.out.println("Observe scroll main:" +"<<<<<<<<"+ savedInstanceState);

        mWebview.saveState(savedInstanceState);
    }

    @Override
    public void onRefresh() {
        mWebview.goBack();
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        String guessFileName = URLUtil.guessFileName(url, contentDisposition, mimetype);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(1);
        request.setDestinationInExternalPublicDir("Wed", guessFileName);
        ((DownloadManager)getActivity().getSystemService("download")).enqueue(request);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getAction() != 1 || !mWebview.canGoBack()) {
            System.out.println("REFRESH_STATUS"+"******");

            return false;
        }
        mWebview.goBack();
        return true;
    }

    private class webChromeView extends WebChromeClient{
        /* synthetic */ FbFragments fb = null;

        class clientRun implements Runnable {
            final  webChromeView wb;

            clientRun(webChromeView c1291a) {
                wb = c1291a;
            }

            public void run() {
                FacebookBackgroundView.setVisibility(8);
                facebookLogo.setVisibility(8);


            }
        }
        private webChromeView(FbFragments facebookFragment) {
             fb = facebookFragment;
        }
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            facebookLogo.animate().alpha(0.0f).setDuration(600).start();
            FacebookBackgroundView.animate().alpha(0.0f).setDuration(600).start();
            facebookLogo.postDelayed(new clientRun(this), 600);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
             super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            Throwable e;
            Parcelable intent;
            Parcelable[] parcelableArr;
            Intent intent2;
            if (mUploadMessageA != null) {
                mUploadMessageA.onReceiveValue(null);
            }
            mUploadMessageA = filePathCallback;
            Intent intent3 = new Intent("android.media.action.IMAGE_CAPTURE");
            if (intent3.resolveActivity(getActivity().getPackageManager()) != null) {
                File createImageFile;
                try {
                    createImageFile =createImageFile();
                    try {
                        intent3.putExtra("PhotoPath",mCameraM);
                    } catch (Exception e2) {
                        e = e2;
                        Log.e("Facebook", "Unable to create Image File", e);
                        if (createImageFile != null) {
                            mCameraM = "file:" + createImageFile.getAbsolutePath();
                            intent3.putExtra("output", Uri.fromFile(createImageFile));
                        } else {
                            intent3 = null;
                        }

                        intent = new Intent("android.intent.action.GET_CONTENT");
                        ((Intent) intent).addCategory("android.intent.category.OPENABLE");
                        ((Intent) intent).setType("*/*");
                        if (intent3 != null) {
                            parcelableArr = new Intent[]{intent3};
                        } else {
                            parcelableArr = new Intent[0];
                        }
                        intent2 = new Intent("android.intent.action.CHOOSER");
                        intent2.putExtra("android.intent.extra.INTENT", intent);
                        intent2.putExtra("android.intent.extra.TITLE", "Choose a file for upload");
                        intent2.putExtra("android.intent.extra.INITIAL_INTENTS", parcelableArr);
                        fb.startActivityForResult(intent2, 1);
                        return true;
                    }
                } catch (IOException e3) {
                    e = e3;
                    createImageFile = null;
                    Log.e("Facebook", "Unable to create Image File", e);
                    if (createImageFile != null) {
                        intent3 = null;
                    } else {
                        fb.mCameraM = "file:" + createImageFile.getAbsolutePath();
                        intent3.putExtra("output", Uri.fromFile(createImageFile));
                    }
                    intent = new Intent("android.intent.action.GET_CONTENT");
                    ((Intent) intent).addCategory("android.intent.category.OPENABLE");
                    ((Intent) intent).setType("*/*");
                    if (intent3 != null) {
                        parcelableArr = new Intent[0];
                    } else {
                        parcelableArr = new Intent[]{intent3};
                    }
                    intent2 = new Intent("android.intent.action.CHOOSER");
                    intent2.putExtra("android.intent.extra.INTENT", intent);
                    intent2.putExtra("android.intent.extra.TITLE", "Choose a file for upload");
                    intent2.putExtra("android.intent.extra.INITIAL_INTENTS", parcelableArr);
                    fb.startActivityForResult(intent2, 1);
                    return true;
                }
                if (createImageFile != null) {
                    fb.mCameraM = "file:" + createImageFile.getAbsolutePath();
                    intent3.putExtra("output", Uri.fromFile(createImageFile));
                } else {
                    intent3 = null;
                }
            }
            intent = new Intent("android.intent.action.GET_CONTENT");
            ((Intent) intent).addCategory("android.intent.category.OPENABLE");
            ((Intent) intent).setType("*/*");
            if (intent3 != null) {
                parcelableArr = new Intent[]{intent3};
            } else {
                parcelableArr = new Intent[0];
            }
            intent2 = new Intent("android.intent.action.CHOOSER");
            intent2.putExtra("android.intent.extra.INTENT", intent);
            intent2.putExtra("android.intent.extra.TITLE", "Choose a file for upload");
            intent2.putExtra("android.intent.extra.INITIAL_INTENTS", parcelableArr);
            fb.startActivityForResult(intent2, 1);
            return true;
        }
    }
    private class webviewClient extends WebViewClient{
         FbFragments fb;

        private webviewClient(FbFragments facebookFragment) {
            this.fb = facebookFragment;
        }
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
        {
            // Handle the error
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            if (url.contains("facebook.com")) {
                return false;
            }
            if (url.contains("com.facebook.orca")) {
                try {
                     startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.facebook.orca")));
                } catch (ActivityNotFoundException e) {
                     startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=com.facebook.orca")));
                }
            } else if (url.contains("com.facebook.katana")) {
                try {
                     startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.facebook.katana")));
                } catch (ActivityNotFoundException e2) {
                     startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=com.facebook.katana")));
                }
            }else {
                 startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(url.equals(view.getOriginalUrl())) {
                // Fetch the Runnable for the OriginalUrl.
                final Runnable lRunnable = getCallbackMap().get(view.getOriginalUrl());
                // Is it valid?
                if(lRunnable != null) { lRunnable.run(); }
            }
             mWebview.loadUrl("javascript:(function() { var el = document.querySelectorAll('div[data-sigil]');for(var i=0;i<el.length; i++){var sigil = el[i].dataset.sigil;if(sigil.indexOf('inlineVideo') > -1){delete el[i].dataset.sigil;var jsonData = JSON.parse(el[i].dataset.store);el[i].setAttribute('onClick', 'FlySoDownloader.processVideo(\"'+jsonData['src']+'\");');}}})()");
             mSwipeView.setRefreshing(false);
             System.out.println("Observe scroll Load url data finished:" + url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
             mWebview.loadUrl("javascript:(function prepareVideo() { var el = document.querySelectorAll('div[data-sigil]');for(var i=0;i<el.length; i++){var sigil = el[i].dataset.sigil;if(sigil.indexOf('inlineVideo') > -1){delete el[i].dataset.sigil;console.log(i);var jsonData = JSON.parse(el[i].dataset.store);el[i].setAttribute('onClick', 'FlySoDownloader.processVideo(\"'+jsonData['src']+'\",\"'+jsonData['videoID']+'\");');}}})()");
             mWebview.loadUrl("javascript:( window.onload=prepareVideo;)()");
        }
    }
    protected File createImageFile() throws IOException {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Wed");
        if (!file.exists()) {
            file.mkdir();
        }
        return File.createTempFile("JPEG_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + "_", ".jpg", file);
    }
    protected void WVSettings() {
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

            mWebview.getSettings().setLoadsImagesAutomatically(true);
            mWebview.getSettings().setBlockNetworkImage(false);

        mWebview.getSettings().setAllowFileAccess(true);
        mWebview.getSettings().setAllowContentAccess(true);
        mWebview.getSettings().setGeolocationEnabled(true);
        mWebview.getSettings().setAppCachePath(getActivity().getCacheDir().getAbsolutePath());
        mWebview.getSettings().setAppCacheEnabled(true);
        mWebview.getSettings().setDomStorageEnabled(true);
        CookieManager.getInstance().setAcceptCookie(true);
        mWebview.getSettings().setTextZoom(100);
        //mWebview.requestFocus(TransportMediator.KEYCODE_MEDIA_RECORD);
        getActivity().getWindow().setSoftInputMode(16);
        mWebview.setDownloadListener(this);
        mWebview.setOnKeyListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        ViewTreeObserver viewTreeObserver = this.mSwipeView.getViewTreeObserver();
        ViewTreeObserver.OnScrollChangedListener c12872 = new C12872(this);
        this.f4394c = c12872;
        viewTreeObserver.addOnScrollChangedListener(c12872);
    }
    class C12872 implements ViewTreeObserver.OnScrollChangedListener {
        final /* synthetic */ FbFragments f4382a;

        C12872(FbFragments facebookFragment) {
            this.f4382a = facebookFragment;
        }

        public void onScrollChanged() {

                if (mWebview.getScrollY() == 0) {
                    this.f4382a.mSwipeView.setEnabled(true);
                } else {
                    this.f4382a.mSwipeView.setEnabled(false);
                }
            }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Object data;
        if (Build.VERSION.SDK_INT < 21) {
            if (requestCode == 1 && this.mUploadMessage != null) {
                data = (intent == null || resultCode != -1) ? null : intent.getData();
                this.mUploadMessage.onReceiveValue((Uri) data);
                this.mUploadMessage = null;
            }
        } else if (requestCode != 1 || this.mUploadMessageA == null) {
            super.onActivityResult(requestCode, resultCode, intent);
        } else {
            if (resultCode == -1) {
                if (intent != null) {
                    if (intent.getDataString() != null) {
                        data = new Uri[]{Uri.parse(intent.getDataString())};
                        this.mUploadMessageA.onReceiveValue((Uri[]) data);
                        this.mUploadMessageA = null;
                    }
                } else if (this.mCameraM != null) {
                    data = new Uri[]{Uri.parse(this.mCameraM)};
                    this.mUploadMessageA.onReceiveValue((Uri[]) data);
                    this.mUploadMessageA = null;
                }
            }
            data = null;
            this.mUploadMessageA.onReceiveValue((Uri[]) data);
            this.mUploadMessageA = null;
        }
    }
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }
}
