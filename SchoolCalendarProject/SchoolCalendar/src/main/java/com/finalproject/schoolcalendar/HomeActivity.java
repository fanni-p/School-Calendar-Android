package com.finalproject.schoolcalendar;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.finalproject.schoolcalendar.helpers.NavigationDrawerHelper;

/**
 * Created by Fani on 11/13/13.
 */
public class HomeActivity extends FragmentActivity
        implements ListView.OnItemClickListener{
    NavigationDrawerHelper mNavigationDrawHelper;
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        //mCoursePagerAdapter = new CoursePagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        //mViewPager = (ViewPager) findViewById(R.id.pager);
        //mViewPager.setAdapter(mSectionsPagerAdapter);
        //mViewPager.setAdapter(mCoursePagerAdapter);

        mNavigationDrawHelper = new NavigationDrawerHelper();
        mNavigationDrawHelper.init(this, this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //mCoursePagerAdapter.setCourseLib(optionLib);
        mNavigationDrawHelper.handleSelect(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mNavigationDrawHelper.syncState();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mNavigationDrawHelper.handleOnPrepareOptionsMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mNavigationDrawHelper.handleOnOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mNavigationDrawHelper.syncState();
        super.onConfigurationChanged(newConfig);
    }
}