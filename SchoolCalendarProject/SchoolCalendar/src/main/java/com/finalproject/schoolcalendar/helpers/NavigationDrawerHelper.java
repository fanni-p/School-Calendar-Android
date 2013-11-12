package com.finalproject.schoolcalendar.helpers;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.finalproject.schoolcalendar.R;

/**
 * Created by Fani on 11/12/13.
 */
public class NavigationDrawerHelper {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private ActionBarDrawerToggle mDrawerToggle;

    public void init(Activity theActivity, ListView.OnItemClickListener listener) {
        mDrawerLayout = (DrawerLayout) theActivity.findViewById(R.id.drawer_layout);
        mDrawerListView = (ListView) theActivity.findViewById(R.id.navigation_drawer);

        String[] navigationDrawerOptions =
                theActivity.getResources().getStringArray(R.array.navigation_drawer_options);
        ArrayAdapter<String> navigationDrawerAdapter =
                new ArrayAdapter<String>(theActivity, R.layout.drawer_option_item, navigationDrawerOptions);

        mDrawerListView.setAdapter(navigationDrawerAdapter);
        mDrawerListView.setOnItemClickListener(listener);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerListView.setItemChecked(0, true);
        setupActionBar(theActivity);
    }

    private void setupActionBar(Activity currentActivity) {
        final Activity activity = currentActivity;
        ActionBar actionBar = currentActivity.getActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                currentActivity,
                mDrawerLayout,
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

    public void handleSelect(int option) {
        mDrawerListView.setItemChecked(option, true);
        mDrawerLayout.closeDrawer(mDrawerListView);
    }

    public void handleOnPrepareOptionsMenu(Menu menu) {
        boolean itemVisible = !mDrawerLayout.isDrawerOpen(mDrawerListView);

        for (int index = 0; index < menu.size(); index++) {
            MenuItem item = menu.getItem(index);
            item.setEnabled(itemVisible);
//      item.setVisible(itemVisible);
        }
    }

    public void handleOnOptionsItemSelected(MenuItem item) {
        mDrawerToggle.onOptionsItemSelected(item);
    }

    public void syncState() {
        mDrawerToggle.syncState();
    }

    public void setSelection(int option) {
        mDrawerListView.setItemChecked(option, true);
    }
}