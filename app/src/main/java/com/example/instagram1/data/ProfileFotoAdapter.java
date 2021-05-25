package com.example.instagram1.data;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.instagram1.fragments.ChatFragment;
import com.example.instagram1.fragments.MyFotoFragment;
import com.example.instagram1.fragments.SaveProfileFragment;
import com.example.instagram1.fragments.UserFragment;

public class ProfileFotoAdapter  extends FragmentPagerAdapter {
    private int numOfTabs;

    public ProfileFotoAdapter (FragmentManager fm, int numOfTabs){

        super (fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return new MyFotoFragment();
            case 1:
                return new SaveProfileFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount(){
        return numOfTabs;
    }
}
