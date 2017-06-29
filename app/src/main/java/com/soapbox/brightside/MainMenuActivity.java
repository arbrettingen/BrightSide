package com.soapbox.brightside;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import java.util.Arrays;

import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.soapbox.brightside.data.AffirmationContract.AffirmationEntry;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity  implements
        LoaderManager.LoaderCallbacks<Cursor>{

    //// TODO: 6/13/2017 sqlite database for  playlists
    /** Identifier for the affirmation data loader */
    private static final int AFFIRMATION_LOADER = 0;


    public static ArrayList<Affirmation> masterAffirmationList = new ArrayList<Affirmation>();  


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

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        if (sharedPref.getBoolean("First Launch", true)){
            initialAffirmationDbInsert();
        }

        if (CustomPlaylistActivity.masterAffirmationPlaylistList == null) {
            CustomPlaylistActivity.masterAffirmationPlaylistList = new ArrayList<>();
            CustomPlaylistActivity.masterAffirmationPlaylistList.add(new AffirmationPlaylist("Favorites"));
            for (Affirmation a : MainMenuActivity.masterAffirmationList){
                if (a.isFavorited()){
                    CustomPlaylistActivity.masterAffirmationPlaylistList.get(0).getAffirmationList().add(a);
                }
            }
        }

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

        //loader setup below

        getLoaderManager().initLoader(AFFIRMATION_LOADER, null, this);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                AffirmationEntry._ID,
                AffirmationEntry.COLUMN_AFFIRMATION_BODY,
                AffirmationEntry.COLUMN_AFFIRMATION_FAVORITED,
                AffirmationEntry.COLUMN_AFFIRMATION_IMAGE,
                AffirmationEntry.COLUMN_AFFIRMATION_CREDIT,
                AffirmationEntry.COLUMN_AFFIRMATION_SATISFACTION_FLAG,
                AffirmationEntry.COLUMN_AFFIRMATION_MOTIVATION_FLAG,
                AffirmationEntry.COLUMN_AFFIRMATION_CONFIDENCE_FLAG};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                AffirmationEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        MainMenuActivity.masterAffirmationList.clear();

        if (data == null){
            return;
        }
        if (data.getCount() == 0){
            return;
        }

        data.moveToFirst();
        while (!data.isAfterLast()){

            int  affirmationBodyIndex = data.getColumnIndex(AffirmationEntry.COLUMN_AFFIRMATION_BODY);
            int  affirmationCreditIndex = data.getColumnIndex(AffirmationEntry.COLUMN_AFFIRMATION_BODY);
            int  affirmationFavoriteIndex = data.getColumnIndex(AffirmationEntry.COLUMN_AFFIRMATION_BODY);
            int  affirmationImageIndex = data.getColumnIndex(AffirmationEntry.COLUMN_AFFIRMATION_BODY);
            int  affirmationSatisfactionIndex = data.getColumnIndex(AffirmationEntry.COLUMN_AFFIRMATION_BODY);
            int  affirmationMotivationIndex = data.getColumnIndex(AffirmationEntry.COLUMN_AFFIRMATION_BODY);
            int  affirmationConfidenceIndex = data.getColumnIndex(AffirmationEntry.COLUMN_AFFIRMATION_BODY);
            int  affirmationID = data.getColumnIndex(AffirmationEntry._ID);

            boolean dFavorited = data.getInt(affirmationFavoriteIndex) == 1;
            boolean dSatisfaction = data.getInt(affirmationSatisfactionIndex) == 1;
            boolean dMotivation = data.getInt(affirmationMotivationIndex) == 1;
            boolean dConfidence = data.getInt(affirmationConfidenceIndex) == 1;

            MainMenuActivity.masterAffirmationList.add(new Affirmation(data.getString(affirmationBodyIndex), data.getString(affirmationImageIndex), data.getString(affirmationCreditIndex), dFavorited, dSatisfaction, dConfidence, dMotivation, data.getInt(affirmationID)));

            data.moveToNext();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void initialAffirmationDbInsert(){

        // Create a ContentValues object where column names are the keys,
        // and Toto's affirmation attributes are the values.
        ContentValues values = new ContentValues();
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_BODY, "Affirmation One Text Here");
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_CREDIT, "");
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_FAVORITED, 0);
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_IMAGE, "");
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_SATISFACTION_FLAG, 0);
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_MOTIVATION_FLAG, 0);
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_CONFIDENCE_FLAG, 0);

        // Use the {@link AffirmationEntry#CONTENT_URI} to indicate that we want to insert
        // into the affirmations database table.
        // Receive the new content URI that will allow us to access row's data in the future.
        Uri newUri = getContentResolver().insert(AffirmationEntry.CONTENT_URI, values);

        values.clear();
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_BODY, "Affirmation Two Text Here");
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_CREDIT, "");
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_FAVORITED, 0);
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_IMAGE, "");
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_SATISFACTION_FLAG, 0);
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_MOTIVATION_FLAG, 0);
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_CONFIDENCE_FLAG, 0);
        newUri = getContentResolver().insert(AffirmationEntry.CONTENT_URI, values);

        values.clear();
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_BODY, "Affirmation Three Text Here");
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_CREDIT, "");
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_FAVORITED, 0);
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_IMAGE, "");
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_SATISFACTION_FLAG, 0);
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_MOTIVATION_FLAG, 0);
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_CONFIDENCE_FLAG, 0);
        newUri = getContentResolver().insert(AffirmationEntry.CONTENT_URI, values);

        values.clear();
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_BODY, "Affirmation Four Text Here");
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_CREDIT, "");
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_FAVORITED, 0);
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_IMAGE, "");
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_SATISFACTION_FLAG, 0);
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_MOTIVATION_FLAG, 0);
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_CONFIDENCE_FLAG, 0);
        newUri = getContentResolver().insert(AffirmationEntry.CONTENT_URI, values);

        values.clear();
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_BODY, "Affirmation Five Text Here");
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_CREDIT, "");
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_FAVORITED, 0);
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_IMAGE, "");
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_SATISFACTION_FLAG, 0);
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_MOTIVATION_FLAG, 0);
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_CONFIDENCE_FLAG, 0);
        newUri = getContentResolver().insert(AffirmationEntry.CONTENT_URI, values);



        //end by setting the shared preference to true
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("First Launch", false);
        editor.apply();
    }
}


