package com.example.jayson.resolvecitycode.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;


public class CityFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    private ArrayList<String> titles;
    private int currentPosition = 0;



    public CityFragmentAdapter(FragmentManager supportFragmentManager, ArrayList<Fragment> mFragments, ArrayList<String> mTitles) {
        super(supportFragmentManager);
        this.fragments = mFragments;
        this.titles = mTitles;
    }


    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currentPosition = position;
    }



    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
