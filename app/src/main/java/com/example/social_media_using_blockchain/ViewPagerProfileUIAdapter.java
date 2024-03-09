package com.example.social_media_using_blockchain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerProfileUIAdapter extends FragmentPagerAdapter {

    public ViewPagerProfileUIAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return new ReelsFragment();
        } else  {
            return new FavoriteFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;//no of tabs
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
            return null;
    }

}
