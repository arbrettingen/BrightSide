package com.soapbox.brightside;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Alex on 2/28/2017.
 * <p>
 * Initial screen is just a list of all of the added playlists with an add button at the top somewhere and also at the bottom item of the list
 * <p>
 * when user clicks add, prompt for name of playlist and then create/add it to the list instantly
 * <p>
 * when user clicks playlist that has already been created, switch to new activity/fragment where they can add affirmations from the app database with
 * a similar list structure to the playlist list
 */

public class CustomPlaylistActivity extends AppCompatActivity {

    private String[] mNavChoices;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Button mNewPlaylistButton;
    private ArrayList<AffirmationPlaylist> mAffirmationPlaylistList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_playlist);
        setTitle("Custom Playlists");

        //FOR TEST
        if (mAffirmationPlaylistList != null) {
            if (!mAffirmationPlaylistList.isEmpty()) {
                Log.e("New Playlist Name", mAffirmationPlaylistList.get(mAffirmationPlaylistList.size() - 1).getPlaylistName());
            }
            Log.e("New Playlist Count", String.valueOf(mAffirmationPlaylistList.size()));

        }

        //affirmation playlists setup from intent
        Intent thisIntent = getIntent();
        if (thisIntent.hasExtra("Custom Playlists")) {
            mAffirmationPlaylistList = thisIntent.getParcelableArrayListExtra("Custom Playlists"); //WORKS!!
            Log.e("Playlist count PL", String.valueOf(mAffirmationPlaylistList.size()));
        } else {
            mAffirmationPlaylistList = new ArrayList<AffirmationPlaylist>();
        }

        //drawer code below
        mNavChoices = getResources().getStringArray(R.array.nav_options_array);
        mDrawerList = (ListView) findViewById(R.id.playlist_left_drawer);

        NavListAdapter mDrawerListAdapter = new NavListAdapter(getApplicationContext(), R.layout.row_nav_drawer, mNavChoices);

        mDrawerList.setAdapter(mDrawerListAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.playlist_drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close_playlist) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setTitle(R.string.drawer_close_playlist);
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

        //add new playlist item button and listener below


        mNewPlaylistButton = (Button) findViewById(R.id.btn_playlist_add);

        mNewPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prompt user for the name of the new playlist
                String newName;
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CustomPlaylistActivity.this, R.style.myDialog));
                builder.setTitle("New Playlist Name: ");

                final EditText input = new EditText(getApplicationContext());

                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (input.getText().toString() != null) {
                            input.setTag(input.getText().toString());
                            dialog.dismiss();

                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Playlist Name!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                //add new playlist to member variable
                if (input.getTag() != null)
                    mAffirmationPlaylistList.add(new AffirmationPlaylist((String) input.getTag())); //TODO
                input.setTag(null);
            }
        });

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

    /**
     * Handles navigation clicks in nav drawer, switches screens
     */
    private void selectItemDrawer(int position) {

        //mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(findViewById(R.id.playlist_drawer_cont));

        Intent i;
        switch (position) {
            case 0:
                i = new Intent(getApplicationContext(), MainMenuActivity.class);
                i.putExtra("Custom Playlists", mAffirmationPlaylistList);
                startActivity(i);
                break;
            case 1:
                i = new Intent(getApplicationContext(), RecommendedActivity.class);
                startActivity(i);
                break;
            case 2:
                i = new Intent(getApplicationContext(), RandomActivity.class);
                startActivity(i);
                break;
            case 3:
                i = new Intent(getApplicationContext(), CustomPlaylistActivity.class);
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


    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemDrawer(position);
        }
    }


}
