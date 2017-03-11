package ca.ulaval.ima.mp.alarmedeluxe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int tabsCounts;

    public PagerAdapter(FragmentManager fm, int tabsCounts) {
        super(fm);
        this.tabsCounts = tabsCounts;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeFragment tab1 = new HomeFragment();
                return tab1;
            case 1:
                SettingsFragment tab2 = new SettingsFragment();
                return tab2;
            case 2:
                YoutubeFragment tab3 = new YoutubeFragment();
                return tab3;
            case 3:
                GeolocationFragment tab4 = new GeolocationFragment();
                return tab4;
            case 4:
                AccelerometerFragment tab5 = new AccelerometerFragment();
                return tab5;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabsCounts;
    }
}
