package ca.ulaval.ima.mp.alarmedeluxe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;

public class PagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

    private static final String[] TABS_HEADER = {"Home", "YouTube", "Geolocation", "Shaking", "Settings"};
    private static final int[] TABS_ICONS = {R.drawable.ic_tabs_home, R.drawable.ic_tabs_youtube, R.drawable.ic_tabs_geolocation, R.drawable.ic_tabs_accelerometer, R.drawable.ic_tabs_settings};

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
                YoutubeFragment tab2 = new YoutubeFragment();
                return tab2;
            case 2:
                GeolocationFragment tab3 = new GeolocationFragment();
                return tab3;
            case 3:
                AccelerometerFragment tab4 = new AccelerometerFragment();
                return tab4;
            case 4:
                SettingsFragment tab5 = new SettingsFragment();
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
        return TABS_HEADER[position];
    }

    @Override
    public int getPageIconResId(int position) {
        return TABS_ICONS[position];
    }
}
