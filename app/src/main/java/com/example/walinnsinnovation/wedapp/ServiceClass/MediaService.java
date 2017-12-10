package com.example.walinnsinnovation.wedapp.ServiceClass;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.walinnsinnovation.wedapp.Libraries.Adapter.MyPagerAdapter;
import com.example.walinnsinnovation.wedapp.MainActivity;
import com.example.walinnsinnovation.wedapp.R;

/**
 * Created by vj on 9/12/17.
 */

public class MediaService extends Service  implements View.OnClickListener {
    private String TAG = "TestService";
    RelativeLayout mButton;
    RelativeLayout layout;
    public MediaService() {
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate called");
//        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE );
//        final View view1 = inflater.inflate( R.layout.footer, null );
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
//                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
//                PixelFormat.TRANSLUCENT);
//
//        params.gravity = Gravity.BOTTOM ;
//        final WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
//        view1.setOnTouchListener(this);
//        wm.addView(view1, params);
        // ImageView fb= (ImageView)view1.findViewById(R.id.img_fb) ;
//        fb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(getApplicationContext(),"it's clicked",Toast.LENGTH_SHORT).show();
//
//                Intent dialogIntent = new Intent(getApplicationContext(), MainActivity.class);
//                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                dialogIntent.putExtra("come_from","service_fb");
//                startActivity(dialogIntent);
//            }
//        });
//        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
//        layout = (RelativeLayout) inflater.inflate(R.layout.footer,
//                null, false);
        // LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        // View inflatedLayout= inflater.inflate(R.layout.footer, null, false);
        mButton = new RelativeLayout(this);
        RelativeLayout.LayoutParams lprams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        Button tv1 = new Button(this);
        tv1.setText("Hello_fb");
        tv1.setLayoutParams(lprams);
        tv1.setId(1);
        mButton.addView(tv1);
        RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        Button tv2 = new Button(this);
        tv2.setText("Hello2_twit");
        newParams.addRule(RelativeLayout.RIGHT_OF, 1);
        tv2.setLayoutParams(newParams);
        tv2.setId(2);
        mButton.addView(tv2);
        // mButton.setText("Overlay button");
        // ImageView b=(ImageView) layout .findViewById(R.id.img_fb);
        tv1.setOnClickListener(this);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        params.setTitle("Load Average");
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        wm.addView(mButton, params);
    }
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand executed"+ intent.getStringExtra("media"));
        if(intent.getStringExtra("media").equals("fb")){
            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
            startActivity(LaunchIntent);

        }

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG, "onStartCommand executed/+++"+ intent.getData());
        return null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case 1:
                break;
            case 2:
                break;
        }


        System.out.println("Button Clicked");
    }
}
