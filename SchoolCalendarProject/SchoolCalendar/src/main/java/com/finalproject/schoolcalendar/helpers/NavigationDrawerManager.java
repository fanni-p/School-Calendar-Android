package com.finalproject.schoolcalendar.helpers;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.finalproject.schoolcalendar.activities.AllHomeworkActivity;
import com.finalproject.schoolcalendar.activities.AllSubjectsActivity;
import com.finalproject.schoolcalendar.R;
import com.finalproject.schoolcalendar.activities.WeekScheduleActivity;

/**
 * Created by Fani on 11/12/13.
 */
public class NavigationDrawerManager {
    private static final int WEEK_SCHEDULE = 0;
    private static final int ALL_SUBJECTS = 1;
    private static final int ALL_HOMEWORK = 2;

    private Context mContext;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private ActionBarDrawerToggle mDrawerToggle;

    public void init(Activity activity, ListView.OnItemClickListener listener) {
        this.mContext = activity;
        this.mDrawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        this.mDrawerListView = (ListView) activity.findViewById(R.id.navigation_drawer);

        String[] navigationDrawerOptions =
                activity.getResources().getStringArray(R.array.navigation_drawer_options);
        ArrayAdapter<String> navigationDrawerAdapter =
                new ArrayAdapter<String>(activity, R.layout.drawer_option_item, navigationDrawerOptions);

        this.mDrawerListView.setAdapter(navigationDrawerAdapter);
        this.mDrawerListView.setOnItemClickListener(listener);
        this.mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        this.mDrawerListView.setItemChecked(0, true);

        this.setupActionBar(activity);
    }

    public void handleSelect(int option) {
        this.mDrawerListView.setItemChecked(option, true);
        this.setSelection(option);
        this.mDrawerLayout.closeDrawer(mDrawerListView);
    }

    public void handleOnPrepareOptionsMenu(Menu menu) {
        boolean itemVisible = !this.mDrawerLayout.isDrawerOpen(this.mDrawerListView);

        for (int index = 0; index < menu.size(); index++) {
            MenuItem item = menu.getItem(index);
            item.setEnabled(itemVisible);
        }
    }

    public void handleOnOptionsItemSelected(MenuItem item) {
        this.mDrawerToggle.onOptionsItemSelected(item);
    }

    public void syncState() {
        this.mDrawerToggle.syncState();
    }

    public void setSelection(int option) {
        switch (option){
            case WEEK_SCHEDULE:
                this.startWeekScheduleActivity();
                break;
            case ALL_SUBJECTS:
                this.startAllSubjectActivity();
                break;
            case ALL_HOMEWORK:
                this.startAllHomeworkActivity();
                break;
        }
    }

    private void startWeekScheduleActivity() {
        Intent intent = new Intent(this.mContext, WeekScheduleActivity.class);
        this.mContext.startActivity(intent);
    }

    private void startAllSubjectActivity() {
        Intent intent = new Intent(this.mContext, AllSubjectsActivity.class);
        this.mContext.startActivity(intent);
    }

    private void startAllHomeworkActivity() {
        Intent intent = new Intent(this.mContext, AllHomeworkActivity.class);
        this.mContext.startActivity(intent);
    }

    private void setupActionBar(Activity currentActivity) {
        final Activity activity = currentActivity;
        ActionBar actionBar = currentActivity.getActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        this.mDrawerToggle = new ActionBarDrawerToggle(
                currentActivity,
                this.mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.open_drawer_message,
                R.string.close_drawer_message
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                activity.invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                activity.invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }
        };
    }
}
