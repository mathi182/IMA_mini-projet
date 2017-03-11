package ca.ulaval.ima.mp.alarmedeluxe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private static final String[] TABS_HEADER = {"Home", "Settings", "YouTube", "Geolocation", "Shaking"};

    public PagerAdapter(FragmentManager fm) {
        super(fm);
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
        return TABS_HEADER.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return TABS_HEADER[position];
    }
}
