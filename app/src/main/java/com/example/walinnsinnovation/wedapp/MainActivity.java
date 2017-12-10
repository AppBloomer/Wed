package com.example.walinnsinnovation.wedapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.walinnsinnovation.wedapp.Fragments.FbFragments;
import com.example.walinnsinnovation.wedapp.Fragments.InstagramFragment;
import com.example.walinnsinnovation.wedapp.Fragments.LinkedinFragment;
import com.example.walinnsinnovation.wedapp.Fragments.TwitterFragment;
import com.example.walinnsinnovation.wedapp.Libraries.Adapter.MyPagerAdapter;
import com.example.walinnsinnovation.wedapp.Libraries.Adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_fb,img_twit,img_ins,linkedin;
    FrameLayout fragment_container,frameLayout_twit,frameLayout_ins,framlay_link;
    private Boolean exit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        img_fb=(ImageView)findViewById(R.id.img_fb);
        img_fb.setOnClickListener(this);
        img_twit=(ImageView)findViewById(R.id.img_twit);
        img_twit.setOnClickListener(this);
        img_ins=(ImageView)findViewById(R.id.img_ins);
        img_ins.setOnClickListener(this);
        linkedin=(ImageView)findViewById(R.id.linked_in);
        linkedin.setOnClickListener(this);
        fragment_container=(FrameLayout)findViewById(R.id.framlay);
        frameLayout_twit=(FrameLayout)findViewById(R.id.framlay1);
        frameLayout_ins=(FrameLayout)findViewById(R.id.framlay2);
        framlay_link=(FrameLayout)findViewById(R.id.framlay_link);
        fragment_container.setVisibility(View.VISIBLE);
        frameLayout_twit.setVisibility(View.GONE);
        frameLayout_ins.setVisibility(View.GONE);
        framlay_link.setVisibility(View.GONE);
//        Fragment newFragment = new FbFragments();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.add(R.id.framlay, newFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(4);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new FbFragments(), "fb");
        adapter.addFragment(new TwitterFragment(), "twit");
        adapter.addFragment(new LinkedinFragment(), "Linked in");
        adapter.addFragment(new InstagramFragment(), "insta");
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        //tabLayout.setVisibility(View.GONE);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if(i==0){
                tabLayout.getTabAt(i).setIcon(getResources().getDrawable(R.drawable.fb_select_unselect));
            }else if(i==1){
                tabLayout.getTabAt(i).setIcon(getResources().getDrawable(R.drawable.twit_select_unselect));
            }else if(i==2){
                tabLayout.getTabAt(i).setIcon(getResources().getDrawable(R.drawable.linked_select_unselct));
            }else {
                tabLayout.getTabAt(i).setIcon(getResources().getDrawable(R.drawable.insta_select_unselect));
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_fb:

                fragment_container.setVisibility(View.VISIBLE);
                frameLayout_twit.setVisibility(View.GONE);
                frameLayout_ins.setVisibility(View.GONE);
                framlay_link.setVisibility(View.GONE);

//                Fragment newFragment = new FbFragments();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.add(R.id.framlay, newFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();

                break;
            case R.id.img_twit:

                fragment_container.setVisibility(View.GONE);
                frameLayout_twit.setVisibility(View.VISIBLE);
                frameLayout_ins.setVisibility(View.GONE);
                framlay_link.setVisibility(View.GONE);

//                newFragment = new TwitterFragment();
//                transaction = getFragmentManager().beginTransaction();
//                transaction.add(R.id.framlay1, newFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();

                break;
        }

    }
}
