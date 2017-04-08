package com.iappstreat.ixigohackathon.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.iappstreat.ixigohackathon.HotelsFragment;

/**
 * Created by verma on 17-02-2017.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    @Override
    public int getItemPosition(Object object) {

        return POSITION_NONE;
    }
    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                HotelsFragment tab1 = new HotelsFragment();
                Bundle b=new Bundle();
                b.putString("parameter","Hotel");
                tab1.setArguments(b);
                return tab1;
            case 1:
                HotelsFragment tab11 = new HotelsFragment();
                Bundle b1=new Bundle();
                b1.putString("parameter","Restaurant");
                tab11.setArguments(b1);
                return tab11;
            case 2:
                HotelsFragment tab2 = new HotelsFragment();
                Bundle b2=new Bundle();
                b2.putString("parameter","Hotel");
                tab2.setArguments(b2);
                return tab2;

            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
