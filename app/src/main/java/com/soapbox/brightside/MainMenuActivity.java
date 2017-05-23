package com.soapbox.brightside;

import android.content.Intent;
import java.util.Arrays;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {

    public static ArrayList<Affirmation> masterAffirmationList = new ArrayList<Affirmation>(Arrays.asList(new Affirmation[]{
            new Affirmation("Affirmation One Text Here"),
            new Affirmation("Affirmation Two Text Here"),
            new Affirmation("Affirmation Three Text Here"),
            new Affirmation("Affirmation Four Text Here"),
            new Affirmation("Affirmation Five Text Here")
    }));


    private String[] mNavChoices;
    private ListView mDrawerList;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private ListView mHomeListView;
    private NavItemAdapter mNavItemAdapter;

    private ArrayList<AffirmationPlaylist> mAffirmationPlaylistList;


    private NavItem[] mNavItems = new NavItem[]{
            new NavItem("Daily Recommendations", "Tell us how you are feeling today and we'll suggest custom affirmations just for you!", "recommended"),
            new NavItem("Random Affirmations", "Scroll through randomly selected affirmations, or search/filter for something specific.", "random"),
            new NavItem("Custom Playlists", "Create or access your own personal affirmation lists.", "custom"),
            new NavItem("Browse All Affirmations", "Look through or search specifically for any affirmation, including any custom additions.", "browse"),
            new NavItem("History and Stats", "View your affirmation history, including streaks and graphs.", "history"),
            new NavItem("Set Reminders", "Set up phone notifications to ensure you get your affirmation fix.", "reminder"),
            new NavItem("Rewards", "Unlock and view special features by staying consistent!", "rewards"),
            new NavItem("About", "Information about this app.", "about"),
            new NavItem("Settings", "Customize your experience.", "settings")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setTitle("Home");

        //main list code below
        mHomeListView = (ListView) findViewById(R.id.home_list_main);
        mNavItemAdapter = new NavItemAdapter(getApplicationContext(), R.layout.home_row, mNavItems);

        mHomeListView.setAdapter(mNavItemAdapter);
        mHomeListView.setOnItemClickListener(new HomeListItemListener());

        //drawer code below
        mNavChoices = getResources().getStringArray(R.array.array_nav_options);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        NavListAdapter mNavListAdapter = new NavListAdapter(getApplicationContext(),
                R.layout.row_nav_drawer, mNavChoices);

        mDrawerList.setAdapter(mNavListAdapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close_main) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setTitle(R.string.drawer_close_main);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle(R.string.drawer_open);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class HomeListItemListener implements  ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemHome(position);
        }
    }

    /**
     * Handles clicks on home nav list
     */
    private void selectItemHome(int position){

        Intent i;
        switch (position){
            case 0:
                i = new Intent(getApplicationContext(), RecommendedActivity.class);
                startActivity(i);
                break;
            case 1:
                i = new Intent(getApplicationContext(), DisplayAffirmationActivity.class);
                startActivity(i);
                break;
            case 2:
                i = new Intent(getApplicationContext(), CustomPlaylistActivity.class);
                startActivity(i);
                break;
            case 3:
                i = new Intent(getApplicationContext(), BrowseActivity.class);
                startActivity(i);
                break;
            case 4:
                i = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(i);
                break;
            case 5:
                i = new Intent(getApplicationContext(), RemindersActivity.class);
                startActivity(i);
                break;
            case 6:
                i = new Intent(getApplicationContext(), RewardsActivity.class);
                startActivity(i);
                break;
            case 7:
                i = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(i);
                break;
            case 8:
                i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
        //mHomeListView.setItemChecked(position, true);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemDrawer(position);
        }
    }

    /**
     * Handles navigation clicks in nav drawer, switches screens
     */
    private void selectItemDrawer(int position) {

        //mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer_cont));

        Intent i;
        switch (position){
            case 0:
                break;
            case 1:
                i = new Intent(getApplicationContext(), RecommendedActivity.class);
                startActivity(i);
                break;
            case 2:
                i = new Intent(getApplicationContext(), DisplayAffirmationActivity.class);
                startActivity(i);
                break;
            case 3:
                i = new Intent(getApplicationContext(), CustomPlaylistActivity.class);
                startActivity(i);
                break;
            case 4:
                i = new Intent(getApplicationContext(), BrowseActivity.class);
                startActivity(i);
                break;
            case 5:
                i = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(i);
                break;
            case 6:
                i = new Intent(getApplicationContext(), RemindersActivity.class);
                startActivity(i);
                break;
            case 7:
                i = new Intent(getApplicationContext(), RewardsActivity.class);
                startActivity(i);
                break;
            case 8:
                i = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(i);
                break;
            case 9:
                i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }


    }


}


