package ca.ulaval.ima.mp.alarmedeluxe.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;

import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.fragment.AccelerometerFragment;
import ca.ulaval.ima.mp.alarmedeluxe.fragment.GeolocationFragment;
import ca.ulaval.ima.mp.alarmedeluxe.fragment.HomeFragment;
import ca.ulaval.ima.mp.alarmedeluxe.fragment.MathsFragment;
import ca.ulaval.ima.mp.alarmedeluxe.fragment.SettingsFragment;
import ca.ulaval.ima.mp.alarmedeluxe.fragment.YoutubeFragment;

public class PagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

    private static final String[] TABS_HEADER = {"Home", "YouTube", "Geolocation", "Shaking", "Maths", "Settings"};
    private static final int[] TABS_ICONS = {R.drawable.ic_tabs_home,
            R.drawable.ic_tabs_youtube,
            R.drawable.ic_tabs_geolocation,
            R.drawable.ic_tabs_accelerometer,
            R.drawable.ic_tabs_maths,
            R.drawable.ic_tabs_settings};

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
                MathsFragment tab5 = new MathsFragment();
                return tab5;
            case 5:
                SettingsFragment tab6 = new SettingsFragment();
                return tab6;
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
