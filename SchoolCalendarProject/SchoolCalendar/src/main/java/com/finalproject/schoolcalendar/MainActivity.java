package com.finalproject.schoolcalendar;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.finalproject.schoolcalendar.helpers.NavigationDrawerHelper;

public class MainActivity extends FragmentActivity
        implements ListView.OnItemClickListener{

    //SectionsPagerAdapter mSectionsPagerAdapter;
    //CoursePagerAdapter mCoursePagerAdapter;
    NavigationDrawerHelper mNavigationDrawHelper;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        // mCoursePagerAdapter = new CoursePagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        //mViewPager.setAdapter(mSectionsPagerAdapter);
        //mViewPager.setAdapter(mCoursePagerAdapter);

        mNavigationDrawHelper = new NavigationDrawerHelper();
        mNavigationDrawHelper.init(this, this );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int optionLib, long id) {
        //mCoursePagerAdapter.setCourseLib(optionLib);
        mNavigationDrawHelper.handleSelect(optionLib);
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