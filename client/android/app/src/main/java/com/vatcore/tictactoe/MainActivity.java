package com.vatcore.tictactoe;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final List<Fragment> fragmentList = new ArrayList<>();
    private ViewPager mViewPager;

    static {
        fragmentList.add(0, ComputerFragment.newInstance());
        fragmentList.add(1, MatchFragment.newInstance());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.activity_main_view_pager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
//                Crime crime = mCrimes.get(position);
//                return CrimeFragment.newInstance(crime.getId());
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
//                return mCrimes.size();
                return fragmentList.size();
            }
        });

        mViewPager.setCurrentItem(0);

    }
}
