package com.example.walinnsinnovation.wedapp.Fragments;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.walinnsinnovation.wedapp.Libraries.ObservableWebView;
import com.example.walinnsinnovation.wedapp.R;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static com.example.walinnsinnovation.wedapp.Fragments.FbFragments.back;

/**
 * Created by walinnsinnovation on 05/12/17.
 */

public class TwitterFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DownloadListener, View.OnKeyListener {
    ObservableWebView mWebview;
    private String f4450a = null;
    private View f4451b;
    private ImageView f4452c;
    private ViewTreeObserver.OnScrollChangedListener f4453d;
    SwipeRefreshLayout mSwipeView;
    protected SharedPreferences sharedPref;
     protected ValueCallback<Uri> mUploadMessage;
    protected String mCameraM;

    protected ValueCallback<Uri[]> mUploadMessageA;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view;
            view = inflater.inflate(R.layout.twit_layout, container, false);

            this.f4451b =(View)view.findViewById(R.id.TwitterBackgroundView);
            this.f4451b.setVisibility(0);
            this.f4452c = (ImageView)view.findViewById(R.id.twitterLogo);
            this.f4452c.setVisibility(0);
            this.mSwipeView = (SwipeRefreshLayout)view.findViewById(R.id.swiperefresh);
            this.mSwipeView.setOnRefreshListener(this);
            this.sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            this.f4451b.setBackgroundColor(ContextCompat.getColor(getActivity(), getColor(this.sharedPref.getBoolean("DarkMode", false))));
            this.mSwipeView.setColorSchemeColors(ContextCompat.getColor(getActivity(), getColor(this.sharedPref.getBoolean("DarkMode", false))));
            this.mWebview = (ObservableWebView) view.findViewById(R.id.TwitterWebView);
            this.mWebview.setWebViewClient(new C1320b(TwitterFragment.this));
            this.mWebview.setWebChromeClient(new C1317a(TwitterFragment.this));
            WVSettings();
            registerForContextMenu(this.mWebview);
            if (Build.VERSION.SDK_INT == 19) {
                String string = getString(R.string.client_identity2);
                CharSequence charSequence = Build.MODEL;
                if (charSequence != null) {
                    string = string.replace("Nexus 5", charSequence);
                } else {
                    string = string.replace("Nexus 5", "Android");
                }
                this.mWebview.getSettings().setUserAgentString(string);
            }
            if (isNetworkAvailable()) {
                this.mSwipeView.setRefreshing(true);
                this.mWebview.loadUrl(getString(R.string.twitter_url));

            }
        

        return view;
    }



    public void onRefresh() {
        mWebview.reload();
    }
    public int getColor(boolean darkmode) {
        if (darkmode) {
            return R.color.colorPrimaryDark;
        }
        return R.color.twitterColor;
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
            return false;
        }
        mWebview.goBack();
        return true;
    }

    private class C1320b extends WebViewClient {
        final /* synthetic */ TwitterFragment f4449a;

        private C1320b(TwitterFragment twitterFragment) {
            this.f4449a = twitterFragment;
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            this.f4449a.mSwipeView.setRefreshing(false);

        }

        public void onLoadResource(WebView view, final String text) {
            Thread thread = new Thread(new Runnable() {
                // final /* synthetic */ C1320b f4448b=null;

                public void run() {
                    try {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(text).openConnection();
                        httpURLConnection.setRequestMethod("HEAD");
                        httpURLConnection.connect();
                        String contentType = httpURLConnection.getContentType();
                        if (contentType != null && contentType.startsWith("video")) {
                            contentType = text;
                            Log.d("FoundVideo", text);
                            final String finalContentType = contentType;
                            getActivity(). runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   // Intent intent = new Intent(getActivity(), StreamVideo.class);
                                   // intent.putExtra("VideoUrl", finalContentType);
                                   // intent.putExtra("VideoName", mGetVideoName(finalContentType));
                                   // getActivity().startActivity(intent);
                                }
                            } );
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            if (!text.contains("youtube") && !text.contains("googlevideo")) {
                thread.start();
            }
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("twitter.com")) {
                return false;
            }
            if (url.contains("com.twitter.android") || url.contains("play.google")) {
                try {
                    this.f4449a.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.twitter.android")));
                } catch (ActivityNotFoundException e) {
                    this.f4449a.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=com.twitter.android")));
                }
            } else if (url.contains("twitter://timeline")) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("twitter://timeline"));
                intent.setPackage("com.twitter.android");
                try {
                    this.f4449a.startActivity(intent);
                } catch (ActivityNotFoundException e2) {
                    Toast.makeText(getActivity(),"twitter_not_installe",Toast.LENGTH_LONG).show();
                    //Toast.makeText(this.f4449a.getActivity().getApplicationContext(), this.f4449a.getString(C1278R.string.twitter_not_installed), 1).show();
                }
            } else if (this.f4449a.sharedPref.getBoolean("BuildInBrowser", false)) {
                //Intent intent2 = new Intent(getActivity(), WebBrowser.class);
               // intent2.putExtra("mUrl", url);
               // this.f4449a.startActivity(intent2);
            } else {
                this.f4449a.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
            }
            return true;
        }
    }
    protected String mGetVideoName(String mUrl) {
        String[] split = mUrl.split("/");
        String str = split[split.length - 1];
        if (str.indexOf(".") > 0) {
            str = str.substring(0, str.lastIndexOf("."));
        }
        return str.substring(0, 8);
    }
    private class C1317a extends WebChromeClient {
        final /* synthetic */ TwitterFragment f4444a;

        class C13163 implements Runnable {
            final /* synthetic */ C1317a f4443a;

            C13163(C1317a c1317a) {
                this.f4443a = c1317a;
            }

            public void run() {
                this.f4443a.f4444a.f4451b.setVisibility(8);
                this.f4443a.f4444a.f4452c.setVisibility(8);

            }
        }

        private C1317a(TwitterFragment twitterFragment) {
            this.f4444a = twitterFragment;
        }



        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress > 30) {

                    this.f4444a.f4452c.animate().alpha(0.0f).setDuration(600).start();
                    this.f4444a.f4451b.animate().alpha(0.0f).setDuration(600).start();
                    this.f4444a.f4452c.postDelayed(new C13163(this), 600);

            }
            super.onProgressChanged(view, newProgress);
        }

        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
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
                    createImageFile = createImageFile();
                    try {
                        intent3.putExtra("PhotoPath", mCameraM);
                    } catch (Exception e2) {
                        e = e2;
                        Log.e("Twitter", "Unable to create Image File", e);
                        if (createImageFile != null) {
                            mCameraM = "file:" + createImageFile.getAbsolutePath();
                            intent3.putExtra("output", Uri.fromFile(createImageFile));
                        } else {
                            intent3 = null;
                        }
                        intent = new Intent("android.intent.action.GET_CONTENT");
                        ((Intent)intent).addCategory("android.intent.category.OPENABLE");
                        ((Intent)intent).setType("*/*");
                        if (intent3 != null) {
                            parcelableArr = new Intent[]{intent3};
                        } else {
                            parcelableArr = new Intent[0];
                        }
                        intent2 = new Intent("android.intent.action.CHOOSER");
                        intent2.putExtra("android.intent.extra.INTENT", intent);
                        intent2.putExtra("android.intent.extra.TITLE", "Choose a file for upload");
                        intent2.putExtra("android.intent.extra.INITIAL_INTENTS", parcelableArr);
                        this.f4444a.startActivityForResult(intent2, 1);
                        return true;
                    }
                } catch (IOException e3) {
                    e = e3;
                    createImageFile = null;
                    Log.e("Twitter", "Unable to create Image File", e);
                    if (createImageFile != null) {
                        intent3 = null;
                    } else {
                        this.f4444a.mCameraM = "file:" + createImageFile.getAbsolutePath();
                        intent3.putExtra("output", Uri.fromFile(createImageFile));
                    }
                    intent = new Intent("android.intent.action.GET_CONTENT");
                    ((Intent)intent).addCategory("android.intent.category.OPENABLE");
                    ((Intent)intent).setType("*/*");
                    if (intent3 != null) {
                        parcelableArr = new Intent[0];
                    } else {
                        parcelableArr = new Intent[]{intent3};
                    }
                    intent2 = new Intent("android.intent.action.CHOOSER");
                    intent2.putExtra("android.intent.extra.INTENT", intent);
                    intent2.putExtra("android.intent.extra.TITLE", "Choose a file for upload");
                    intent2.putExtra("android.intent.extra.INITIAL_INTENTS", parcelableArr);
                    getActivity().startActivityForResult(intent2, 1);
                    return true;
                }
                if (createImageFile != null) {
                    mCameraM = "file:" + createImageFile.getAbsolutePath();
                    intent3.putExtra("output", Uri.fromFile(createImageFile));
                } else {
                    intent3 = null;
                }
            }
            intent = new Intent("android.intent.action.GET_CONTENT");
            ((Intent)intent).addCategory("android.intent.category.OPENABLE");
            ((Intent)intent).setType("*/*");
            if (intent3 != null) {
                parcelableArr = new Intent[]{intent3};
            } else {
                parcelableArr = new Intent[0];
            }
            intent2 = new Intent("android.intent.action.CHOOSER");
            intent2.putExtra("android.intent.extra.INTENT", intent);
            intent2.putExtra("android.intent.extra.TITLE", "Choose a file for upload");
            intent2.putExtra("android.intent.extra.INITIAL_INTENTS", parcelableArr);
            this.f4444a.startActivityForResult(intent2, 1);
            return true;
        }
    }

    protected String mGetPictureName(String mUrl) {
        String[] split = mUrl.split("/");
        String str = split[split.length - 1];
        if (str.contains(".png")) {
            return "FlySo-IMG-" + str.substring(0, 8) + ".png";
        }
        if (str.contains(".gif")) {
            return "FlySo-IMG-" + str.substring(0, 8) + ".gif";
        }
        if (str.contains(".jpg")) {
            return "FlySo-IMG-" + str.substring(0, 8) + ".jpg";
        }
        return "FlySo-IMG-" + str.substring(0, 8) + ".jpg";
    }

    protected File createImageFile() throws IOException {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "wed");
        if (!file.exists()) {
            file.mkdir();
        }
        return File.createTempFile("JPEG_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + "_", ".jpg", file);
    }

    protected boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager)getActivity().getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
    public void onPause() {
        this.mWebview.onPause();
        super.onPause();
    }

    public void onResume() {
        this.mWebview.onResume();
        super.onResume();
    }

    public void onStart() {
        super.onStart();

            ViewTreeObserver viewTreeObserver = this.mSwipeView.getViewTreeObserver();
            ViewTreeObserver.OnScrollChangedListener c13132 = new C13132(this);
            this.f4453d = c13132;
            viewTreeObserver.addOnScrollChangedListener(c13132);

    }

    public void onStop() {
        this.mSwipeView.getViewTreeObserver().removeOnScrollChangedListener(this.f4453d);
        super.onStop();
    }

    class C13132 implements ViewTreeObserver.OnScrollChangedListener {
        final /* synthetic */ TwitterFragment f4436a;

        C13132(TwitterFragment twitterFragment) {
            this.f4436a = twitterFragment;
        }

        public void onScrollChanged() {

                if (mWebview.getScrollY() == 0) {
                    mSwipeView.setEnabled(true);
                } else {
                    mSwipeView.setEnabled(false);
                }

        }
    }





    private void m6433a(String str, String str2) {
        if (str == null) {
            Toast.makeText(getActivity(), "Cant Open Photo Viewer Activity", 0).show();
        } else {
//            Intent intent = new Intent(getActivity(), PhotoViewer.class);
//            intent.putExtra("PictureUrl", str);
//            intent.putExtra("PictureName", str2);
//            startActivity(intent);
        }
        this.f4450a = null;
    }

    protected void WVSettings() {
        this.mWebview.getSettings().setJavaScriptEnabled(true);
        this.mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        if (this.sharedPref.getBoolean("SaveData", false)) {
            this.mWebview.getSettings().setLoadsImagesAutomatically(false);
            this.mWebview.getSettings().setBlockNetworkImage(true);
        } else {
            this.mWebview.getSettings().setLoadsImagesAutomatically(true);
            this.mWebview.getSettings().setBlockNetworkImage(false);
        }
        this.mWebview.getSettings().setAllowFileAccess(true);
        this.mWebview.getSettings().setAllowContentAccess(true);
        this.mWebview.getSettings().setGeolocationEnabled(true);
        this.mWebview.getSettings().setAppCachePath(getActivity().getCacheDir().getAbsolutePath());
        this.mWebview.getSettings().setAppCacheEnabled(true);
        this.mWebview.getSettings().setDomStorageEnabled(true);
        CookieManager.getInstance().setAcceptCookie(true);
        this.mWebview.getSettings().setTextZoom(Integer.parseInt(this.sharedPref.getString("TextSize", "100")));
       // this.mWebview.requestFocus(TransportMediator.KEYCODE_MEDIA_RECORD);
        getActivity().getWindow().setSoftInputMode(16);
        this.mWebview.setDownloadListener(this);
        this.mWebview.setOnKeyListener(this);
    }
}
