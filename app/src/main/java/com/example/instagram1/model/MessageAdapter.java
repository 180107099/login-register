package com.example.instagram1.model;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.instagram1.fragments.ChatFragment;
import com.example.instagram1.fragments.UserFragment;

public class MessageAdapter  extends FragmentPagerAdapter {
    private int numOfTabs;

    public MessageAdapter (FragmentManager fm,int numOfTabs){

        super (fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return new ChatFragment();
            case 1:
                return new UserFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount(){
        return numOfTabs;
    }
}
