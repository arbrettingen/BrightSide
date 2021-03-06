package com.soapbox.brightside;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import com.soapbox.brightside.data.AffirmationContract.AffirmationEntry;

import com.soapbox.brightside.data.AffirmationContract;
import com.soapbox.brightside.data.AffirmationDbHelper;
import com.soapbox.brightside.data.PlaylistContract.PlaylistEntry;

/**
 * Created by Alex on 2/28/2017.
 */

public class DisplayAffirmationActivity extends AppCompatActivity implements
        android.app.LoaderManager.LoaderCallbacks<Cursor>{

    private String[] mNavChoices;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView mAffirmationText;
    private TextView mBottomRightText;
    private LinearLayout mBottomRightButton;
    private LinearLayout mFavoriteButton;
    private LinearLayout mBottomLeftButton;
    private ImageView mBottomLeftImage;
    private TextView mBottomLeftText;
    private int mAffirmationListPosition;
    private ImageView mFavImage;
    private ImageView mBottomRightImage;
    public static ArrayAdapter<Affirmation> mCurrListAdapter;

    public static final int AFFIRMATION_LOADER = 0;
    public static final int PLAYLIST_LOADER = 1;
    public static final int PLAYLIST_FAVORITE_UPDATE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_affirmation);
        setTitle("Random Affirmations");

        //initializing
        mAffirmationText = (TextView) findViewById(R.id.display_affirmation_text);
        mBottomRightButton = (LinearLayout) findViewById(R.id.btn_display_bottom_right);
        mBottomRightImage = (ImageView) findViewById(R.id.btn_display_bottom_right_image);
        mBottomRightText = (TextView) findViewById(R.id.display_bottom_right_text);

        mBottomLeftButton = (LinearLayout) findViewById(R.id.display_btn_bottom_left);
        mBottomLeftImage = (ImageView) findViewById(R.id.display_image_bottom_left);
        mBottomLeftText = (TextView) findViewById(R.id.display_text_bottom_left);


        //intent extra checking

        Intent i = getIntent();
        if (i.hasExtra("Selected From List")){

            int pos = i.getIntExtra("Selected From List", -1);

            //affirmation text setup below
            mAffirmationListPosition = pos;
            mAffirmationText.setText(mCurrListAdapter.getItem(mAffirmationListPosition).getAffirmationBody());

            //Next button code below
            mBottomRightImage.setImageResource(R.drawable.next);
            mBottomRightImage.setBackgroundResource(R.color.colorAccent);
            mBottomRightText.setText("NEXT");
            mBottomRightText.setBackgroundResource(R.color.colorAccent);
            mBottomRightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAffirmationListPosition != mCurrListAdapter.getCount()-1){
                        mAffirmationListPosition++;
                        mAffirmationText.setText(mCurrListAdapter.getItem(mAffirmationListPosition).getAffirmationBody());
                    } else if (mAffirmationListPosition == mCurrListAdapter.getCount()-1){
                        mAffirmationListPosition = 0;
                        mAffirmationText.setText(mCurrListAdapter.getItem(mAffirmationListPosition).getAffirmationBody());
                    }
                    setResources();
                }
            });

            //Prev Button code below
            mBottomLeftImage.setImageResource(R.drawable.prev);
            mBottomLeftImage.setBackgroundResource(R.color.colorAccent);
            mBottomLeftText.setText("PREV");
            mBottomLeftText.setBackgroundResource(R.color.colorAccent);
            mBottomLeftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAffirmationListPosition != 0){
                        mAffirmationListPosition--;
                        mAffirmationText.setText(mCurrListAdapter.getItem(mAffirmationListPosition).getAffirmationBody());
                    } else if (mAffirmationListPosition == 0){
                        mAffirmationListPosition = mCurrListAdapter.getCount()-1;
                        mAffirmationText.setText(mCurrListAdapter.getItem(mAffirmationListPosition).getAffirmationBody());

                    }
                    setResources();
                }
            });



        }
        else{
            //affirmation text setup below
            mAffirmationListPosition = randomWithRange(0,MainMenuActivity.masterAffirmationList.size()-1);
            mAffirmationText.setText(MainMenuActivity.masterAffirmationList.get(mAffirmationListPosition).getAffirmationBody());

            //Random button code below
            mBottomRightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAffirmationListPosition = randomWithRange(0,MainMenuActivity.masterAffirmationList.size()-1);
                    mAffirmationText.setText(MainMenuActivity.masterAffirmationList.get(mAffirmationListPosition).getAffirmationBody());
                    setResources();
                }
            });

            //other button
            mBottomLeftButton.setVisibility(View.GONE);

        }




        //favorite button code below
        mFavImage = (ImageView) findViewById(R.id.img_favorite);

        if (MainMenuActivity.masterAffirmationList.get(mAffirmationListPosition).isFavorited()){
            mFavImage.setImageResource(R.drawable.favorite);
        }else{
            mFavImage.setImageResource(R.drawable.unfavorite);
        }

        mFavoriteButton = (LinearLayout) findViewById(R.id.btn_favorite);
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                if (i.hasExtra("Selected From List")){

                    if (mCurrListAdapter.getItem(mAffirmationListPosition).isFavorited()) {
                        mFavImage.setImageResource(R.drawable.unfavorite);
                        mCurrListAdapter.getItem(mAffirmationListPosition).setFavorited(false);

                        if (CustomPlaylistActivity.masterAffirmationPlaylistList != null) {
                            if (!CustomPlaylistActivity.masterAffirmationPlaylistList.isEmpty()) {
                                CustomPlaylistActivity.masterAffirmationPlaylistList.get(0).getAffirmationList()
                                        .remove(mCurrListAdapter.getItem(mAffirmationListPosition));
                            }
                        }

                        removeFromFavoritesPlaylist(mCurrListAdapter.getItem(mAffirmationListPosition));

                        Toast.makeText(getApplicationContext(), "Affirmation removed from favorites.", Toast.LENGTH_LONG).show();
                    } else {
                        mFavImage.setImageResource(R.drawable.favorite);
                        mCurrListAdapter.getItem(mAffirmationListPosition).setFavorited(true);

                        if (CustomPlaylistActivity.masterAffirmationPlaylistList != null) {
                            if (!CustomPlaylistActivity.masterAffirmationPlaylistList.isEmpty()) {
                                CustomPlaylistActivity.masterAffirmationPlaylistList.get(0).getAffirmationList()
                                        .add(mCurrListAdapter.getItem(mAffirmationListPosition));
                            }
                        }

                        addToFavoritesPlaylistDb(mCurrListAdapter.getItem(mAffirmationListPosition));

                        Toast.makeText(getApplicationContext(), "Affirmation added to favorites.", Toast.LENGTH_LONG).show();
                    }

                }else {
                    if (MainMenuActivity.masterAffirmationList.get(mAffirmationListPosition).isFavorited()) {
                        mFavImage.setImageResource(R.drawable.unfavorite);
                        MainMenuActivity.masterAffirmationList.get(mAffirmationListPosition).setFavorited(false);

                        if (CustomPlaylistActivity.masterAffirmationPlaylistList != null) {
                            if (!CustomPlaylistActivity.masterAffirmationPlaylistList.isEmpty()) {
                                CustomPlaylistActivity.masterAffirmationPlaylistList.get(0).getAffirmationList()
                                        .remove(MainMenuActivity.masterAffirmationList.get(mAffirmationListPosition));
                            }
                        }

                        removeFromFavoritesPlaylist(mCurrListAdapter.getItem(mAffirmationListPosition));

                        Toast.makeText(getApplicationContext(), "Affirmation removed from favorites.", Toast.LENGTH_LONG).show();
                    } else {
                        mFavImage.setImageResource(R.drawable.favorite);
                        MainMenuActivity.masterAffirmationList.get(mAffirmationListPosition).setFavorited(true);

                        if (CustomPlaylistActivity.masterAffirmationPlaylistList != null) {
                            if (!CustomPlaylistActivity.masterAffirmationPlaylistList.isEmpty()) {
                                CustomPlaylistActivity.masterAffirmationPlaylistList.get(0).getAffirmationList()
                                        .add(MainMenuActivity.masterAffirmationList.get(mAffirmationListPosition));
                            }
                        }

                        addToFavoritesPlaylistDb(mCurrListAdapter.getItem(mAffirmationListPosition));

                        Toast.makeText(getApplicationContext(), "Affirmation added to favorites.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //drawer code below
        mNavChoices = getResources().getStringArray(R.array.array_nav_options);
        mDrawerList = (ListView) findViewById(R.id.display_left_drawer);

        NavListAdapter mDrawerListAdapter = new NavListAdapter(getApplicationContext(), R.layout.row_nav_drawer, mNavChoices);

        mDrawerList.setAdapter(mDrawerListAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.display_drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close_random){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setTitle("Random Affirmations");
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

        setResources();

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
        mDrawerLayout.closeDrawer(findViewById(R.id.display_drawer_cont));

        Intent i;
        switch (position){
            case 0:
                i = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(i);
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

    private void setResources(){
        Intent i = getIntent();
        if (i.hasExtra("Selected From List")){

            if (mCurrListAdapter.getItem(mAffirmationListPosition).isFavorited()) {
                mFavImage.setImageResource(R.drawable.favorite);
            } else {
                mFavImage.setImageResource(R.drawable.unfavorite);
            }
        }else {
            if (MainMenuActivity.masterAffirmationList.get(mAffirmationListPosition).isFavorited()) {
                mFavImage.setImageResource(R.drawable.favorite);
            } else {
                mFavImage.setImageResource(R.drawable.unfavorite);
            }
        }
    }

    private int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    private void addToFavoritesPlaylistDb(Affirmation a){
        // Create a ContentValues object where column names are the keys,
        // and playlist attributes are the values.

        ContentValues values = new ContentValues();
        values.put(PlaylistEntry.COLUMN_PLAYLIST_NAME, "Favorites");
        values.put(PlaylistEntry.COLUMN_PLAYLIST_AFFIRMATION_ID, a.getM_ID());

        // Use the {@link PlaylistEntry#CONTENT_URI} to indicate that we want to insert
        // into the playlists database table.
        // Receive the new content URI that will allow us to access row's data in the future.
        Uri newUri = getContentResolver().insert(PlaylistEntry.CONTENT_URI, values);
    }

    private void removeFromFavoritesPlaylist(Affirmation a){

        Bundle playlist_update_bundle = new Bundle(getClassLoader());
        playlist_update_bundle.putInt("Affirmation ID", a.getM_ID());

        getLoaderManager().initLoader(PLAYLIST_FAVORITE_UPDATE, playlist_update_bundle, this);

    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == PLAYLIST_FAVORITE_UPDATE){ //remove given affirmation from favorites playlist
            AffirmationDbHelper mDbHelper = new AffirmationDbHelper(getApplicationContext());
            SQLiteDatabase database = mDbHelper.getReadableDatabase();
            int affirmation_ID = args.getInt("Affirmation ID");

            String whereClause = PlaylistEntry.COLUMN_PLAYLIST_NAME + "=? and " + PlaylistEntry.COLUMN_PLAYLIST_AFFIRMATION_ID + "=?";
            String[] whereArgs = { "Favorites" , String.valueOf(affirmation_ID)};

            database.delete(PlaylistEntry.TABLE_NAME, whereClause, whereArgs);
        }
        return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }
}
