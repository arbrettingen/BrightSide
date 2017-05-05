package com.soapbox.brightside;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Alex on 4/11/2017.
 */

public class BrowseActivity extends AppCompatActivity {

    private ListView mAffirmationList;
    private Button mAddAffirmation;
    private TextView mBarText;

    private String[] mNavChoices;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<Affirmation> mSelectedAffirmations = new ArrayList<>();
    private ArrayList<Affirmation> mAddableAffirmations;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        //actionBar code below

        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.action_bar_browse);

        actionBar.setDisplayOptions(android.app.ActionBar.DISPLAY_SHOW_CUSTOM | android.app.ActionBar.DISPLAY_SHOW_HOME);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mBarText = (TextView) findViewById(R.id.bar_browse_txt);
        mBarText.setText("Browse Affirmations");

        //drawer code below
        mNavChoices = getResources().getStringArray(R.array.nav_options_array);
        mDrawerList = (ListView) findViewById(R.id.browse_left_drawer);

        NavListAdapter mDrawerListAdapter = new NavListAdapter(getApplicationContext(), R.layout.row_nav_drawer, mNavChoices);

        mDrawerList.setAdapter(mDrawerListAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.browse_drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close_browse) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mBarText.setText("Browse Affirmations");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mBarText.setText("Navigate");
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);





        //affirmation listing below

        mAffirmationList = (ListView) findViewById(R.id.affirmation_master_list);

        ArrayAdapter<Affirmation> mAffirmationAdapter;
        Intent i = getIntent();
        if (i.hasExtra("Browse Playlist")) {
            int pos = i.getIntExtra("Browse Playlist", -1);
            mAffirmationAdapter = new ArrayAdapter<Affirmation>(getApplicationContext(), R.layout.playlist_list_row, CustomPlaylistActivity.masterAffirmationPlaylistList.get(pos).getAffirmationList());
            mBarText.setText(CustomPlaylistActivity.masterAffirmationPlaylistList.get(pos).getPlaylistName());
        } else if (i.hasExtra("Add to Playlist")){
            int pos = i.getIntExtra("Add to Playlist", -1);
            ArrayList<Affirmation> mExcludedAffirmations = CustomPlaylistActivity.masterAffirmationPlaylistList.get(pos).getAffirmationList();
            mAddableAffirmations = (ArrayList<Affirmation>) MainMenuActivity.masterAffirmationList.clone();
            for (Affirmation a : mExcludedAffirmations){
                mAddableAffirmations.remove(a);
            }
            mAffirmationAdapter = new ArrayAdapter<Affirmation>(getApplicationContext(), R.layout.playlist_list_row, mAddableAffirmations);
            mBarText.setText("Add to Playlist");
        } else {
            mAffirmationAdapter = new ArrayAdapter<Affirmation>(getApplicationContext(), R.layout.playlist_list_row, MainMenuActivity.masterAffirmationList);
        }

        mAffirmationList.setAdapter(mAffirmationAdapter);

        mAffirmationList.setOnItemLongClickListener(new AffirmationListItemLongListener());
        mAffirmationList.setOnItemClickListener(new AffirmationListItemListener());

        //add new button setup
        mAddAffirmation = (Button) findViewById(R.id.btn_add_affirmation);

        if (i.hasExtra("Add to Playlist")){
            mAddAffirmation.setText("+Add Selected to Playlist");
        } else if (i.hasExtra("Browse Playlist")){
            mAddAffirmation.setText("+ Add Affirmation");
        }

        mAddAffirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                Intent j;
                if (i.hasExtra("Add to Playlist")){
                    addSelectedToPlaylist();

                } else{
                    if (i.hasExtra("Browse Playlist")){
                        j = new Intent(getApplicationContext(), BrowseActivity.class);
                        int pos = i.getIntExtra("Browse Playlist", -1);
                        j.putExtra("Add to Playlist", pos);
                    } else{
                        j = new Intent(getApplicationContext(), NewAffirmationActivity.class);
                    }

                    startActivity(j);
                    updateAffirmationList();
                }
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
        mDrawerLayout.closeDrawer(findViewById(R.id.browse_drawer_cont));

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

    private class AffirmationListItemLongListener implements ListView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int affirmationPosition, long id) {


            Intent i = getIntent();

            if (!i.hasExtra("Add to Playlist")) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                dialog.dismiss();
                                editItemAffirmationList(affirmationPosition);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                deleteItemAffirmationList(affirmationPosition);
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(BrowseActivity.this, R.style.myDialog));
                if (i.hasExtra("Browse Playlist")){
                    builder.setMessage(MainMenuActivity.masterAffirmationList.get(affirmationPosition).getShortenedBody()).setPositiveButton("Edit Affirmation Text", dialogClickListener).setNegativeButton("Delete Affirmation From Playlist", dialogClickListener).show();
                }else {
                    builder.setMessage(MainMenuActivity.masterAffirmationList.get(affirmationPosition).getShortenedBody()).setPositiveButton("Edit Affirmation Text", dialogClickListener).setNegativeButton("Delete Affirmation", dialogClickListener).show();
                }
            }
            return true;
        }
    }

    private class AffirmationListItemListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent i = getIntent();
            if (i.hasExtra("Add to Playlist")){
                if (mSelectedAffirmations.contains(mAddableAffirmations.get(position))){
                    mSelectedAffirmations.remove(mAddableAffirmations.get(position));
                    view.setBackgroundResource(R.color.colorWhite);
                }else{
                    mSelectedAffirmations.add(mAddableAffirmations.get(position));
                    view.setBackgroundResource(R.color.colorAccent);
                }
            }
        }
    }

    private void editItemAffirmationList(int position){
        Intent i = new Intent(getApplicationContext(), NewAffirmationActivity.class);
        i.putExtra("Affirmation Body", MainMenuActivity.masterAffirmationList.get(position).getAffirmationBody());
        i.putExtra("Affirmation Position", position);
        startActivity(i);
        updateAffirmationList();
    }

    private void deleteItemAffirmationList(final int position){
        Intent i = getIntent();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = getIntent();
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (i.hasExtra("Browse Playlist")){
                            int pos = i.getIntExtra("Browse Playlist", -1);
                            CustomPlaylistActivity.masterAffirmationPlaylistList.get(pos).getAffirmationList().remove(position);
                        }else {
                            MainMenuActivity.masterAffirmationList.remove(position);
                        }
                        updateAffirmationList();
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Affirmation successfully deleted.", Toast.LENGTH_LONG).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(BrowseActivity.this, R.style.myDialog));
        if (i.hasExtra("Browse Playlist")){
            builder.setMessage("Are you sure you'd like to delete " + MainMenuActivity.masterAffirmationList.get(position).getShortenedBody() + "From Playlist?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
        }else {
            builder.setMessage("Are you sure you'd like to delete " + MainMenuActivity.masterAffirmationList.get(position).getShortenedBody() + "?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
        }
    }

    private void updateAffirmationList(){
        Intent i = getIntent();
        mAffirmationList = (ListView) findViewById(R.id.affirmation_master_list);
        ArrayAdapter<Affirmation> mAffirmationAdapter;
        if (i.hasExtra("Browse Playlist")){
            int pos = i.getIntExtra("Browse Playlist", -1);
            mAffirmationAdapter = new ArrayAdapter<Affirmation>(getApplicationContext(), R.layout.playlist_list_row, CustomPlaylistActivity.masterAffirmationPlaylistList.get(pos).getAffirmationList());
        } else{
            mAffirmationList = (ListView) findViewById(R.id.affirmation_master_list);
            mAffirmationAdapter = new ArrayAdapter<Affirmation>(getApplicationContext(), R.layout.playlist_list_row, MainMenuActivity.masterAffirmationList);
        }
        mAffirmationList.setAdapter(mAffirmationAdapter);
    }

    private void addSelectedToPlaylist(){
        if (mSelectedAffirmations.isEmpty()){
            Toast.makeText(getApplicationContext(), "Nothing Selected.", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            Intent i = getIntent();
            int pos = i.getIntExtra("Add to Playlist", -1);
            for (Affirmation a : mSelectedAffirmations){
                if (!CustomPlaylistActivity.masterAffirmationPlaylistList.get(pos).getAffirmationList().contains(a)){ //if affirmation is not yet in playlist
                    CustomPlaylistActivity.masterAffirmationPlaylistList.get(pos).getAffirmationList().add(a);
                }
                Toast.makeText(getApplicationContext(), "All selected affirmations added to playlist.", Toast.LENGTH_LONG).show();

                i = new Intent(getApplicationContext(), BrowseActivity.class);
                i.putExtra("Browse Playlist",pos);
                startActivity(i);

            }
        }




    }

}
