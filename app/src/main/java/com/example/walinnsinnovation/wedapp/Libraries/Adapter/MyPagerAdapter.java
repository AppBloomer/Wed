package com.example.walinnsinnovation.wedapp.Libraries.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.walinnsinnovation.wedapp.Main2Activity;
import com.example.walinnsinnovation.wedapp.R;

/**
 * Created by vj on 9/12/17.
 */

public class MyPagerAdapter extends PagerAdapter {
    private static int NUM_ITEMS = 3;
    Context mContext;

    public MyPagerAdapter(Context context) {
        mContext = context;
    }


    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.sample_layout,
                collection, false);
       Button btn1 = (Button)layout.findViewById(R.id.btn1);
         switch (position) {
            case 0:
                System.out.println("View pager intent :"+ "position 1");
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent LaunchIntent = new Intent(mContext, Main2Activity.class);
                        mContext.startActivity(LaunchIntent);
                    }
                });
                break;
            case 1:
                System.out.println("View pager intent :"+ "position 2");

                break;
            case 2:
                System.out.println("View pager intent :"+ "position 3");

                 break;
        }

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
