package com.egemen.dinle.holder;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TextTabAdapter  extends FragmentStatePagerAdapter {

    List<Fragment> fragmentListesi=new ArrayList<>();
    List<String> tabBaslikListesi=new ArrayList<>();


    public TextTabAdapter(FragmentManager fm, List<Fragment> fraList, List<String> baslikList) {
        super(fm);

        this.fragmentListesi=fraList;
        this.tabBaslikListesi=baslikList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentListesi.get(position);
    }

    @Override
    public int getCount() {
        return fragmentListesi.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabBaslikListesi.get(position);
    }
}
