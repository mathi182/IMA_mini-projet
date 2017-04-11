package ca.ulaval.ima.mp.alarmedeluxe;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.astuetz.PagerSlidingTabStrip;

import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.adapter.PagerAdapter;
import ca.ulaval.ima.mp.alarmedeluxe.domain.Alarm;
import ca.ulaval.ima.mp.alarmedeluxe.persistence.DBHelper;
import ca.ulaval.ima.mp.alarmedeluxe.types.AlarmType;

import static ca.ulaval.ima.mp.alarmedeluxe.MyAlarmManager.createAlarmManager;

public class MainActivity extends AppCompatActivity {

    private DBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabsStrip.setViewPager(viewPager);
        database = new DBHelper(this);

        createAlarmManager(this);
    }

    public boolean deleteAlarm(Alarm alarm) {
        return database.deleteAlarm(alarm) >= 0;
    }
}