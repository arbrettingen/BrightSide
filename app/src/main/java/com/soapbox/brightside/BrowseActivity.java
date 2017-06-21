package com.soapbox.brightside;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.soapbox.brightside.data.AffirmationContract.AffirmationEntry;

import java.util.ArrayList;

/**
 * Created by Alex on 4/11/2017.
 */

public class BrowseActivity extends AppCompatActivity{



    private ListView mAffirmationList;
    private Button mAddAffirmation;
    private TextView mBarText;
    private ImageView mSearchBtn;
    private ArrayAdapter<Affirmation> mAffirmationAdapter;
    private ArrayAdapter<Affirmation> mSearchAffirmationAdapter;
    private static ActionBar mActionBar;

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
        mActionBar = getSupportActionBar();
        mActionBar.setCustomView(R.layout.action_bar_browse);

        mActionBar.setDisplayOptions(android.app.ActionBar.DISPLAY_SHOW_CUSTOM | android.app.ActionBar.DISPLAY_SHOW_HOME);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mBarText = (TextView) findViewById(R.id.bar_browse_txt);
        mBarText.setText("Browse Affirmations");

        //drawer code below
        mNavChoices = getResources().getStringArray(R.array.array_nav_options);
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
            mAddAffirmation.setText("+Add Affirmation");
            if (CustomPlaylistActivity.masterAffirmationPlaylistList.get(i.getIntExtra("Browse Playlist", 0)).getPlaylistName().equals("Favorites")){
                mAddAffirmation.setVisibility(View.GONE);
            }
            else{
                mAddAffirmation.setVisibility(View.VISIBLE);
            }
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

        //search button code below

        mSearchBtn = (ImageView) findViewById(R.id.btn_action_search);

        mSearchBtn.setOnClickListener(new searchButtonClickListener());

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

    private class searchButtonClickListener implements ImageView.OnClickListener {
        @Override
        public void onClick(View v) {
            mActionBar.setCustomView(R.layout.action_bar_search);

            mActionBar.setDisplayOptions(android.app.ActionBar.DISPLAY_SHOW_CUSTOM | android.app.ActionBar.DISPLAY_SHOW_HOME);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            final EditText mSearchEditText = (EditText) findViewById(R.id.action_search_txt);

            mSearchEditText.requestFocus();

            InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mSearchEditText, InputMethodManager.SHOW_IMPLICIT);

            mSearchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ArrayList<Affirmation> mSearchedAffirmations = new ArrayList<Affirmation>();
                    for (int i = 0; i < mAffirmationAdapter.getCount(); i++){
                        if (mAffirmationAdapter.getItem(i).getAffirmationBody().toLowerCase().contains(s.toString().toLowerCase())){
                            mSearchedAffirmations.add(mAffirmationAdapter.getItem(i));
                        }
                    }
                    mSearchAffirmationAdapter = new ArrayAdapter<Affirmation>(getApplicationContext(), R.layout.playlist_list_row, mSearchedAffirmations);
                    mAffirmationList = (ListView) findViewById(R.id.affirmation_master_list);
                    mAffirmationList.setAdapter(mSearchAffirmationAdapter);


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            ImageView mCancelBtn = (ImageView) findViewById(R.id.btn_action_search_cancel);
            mCancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
                    mActionBar.setCustomView(R.layout.action_bar_browse);
                    updateAffirmationList();

                }
            });
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
            else{
                Intent j = new Intent(getApplicationContext(), DisplayAffirmationActivity.class);
                j.putExtra("Selected From List", position);
                DisplayAffirmationActivity.mCurrListAdapter = (ArrayAdapter) mAffirmationList.getAdapter();
                startActivity(j);
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
        mBarText = (TextView) findViewById(R.id.bar_browse_txt);

        mSearchBtn = (ImageView) findViewById(R.id.btn_action_search);
        mSearchBtn.setOnClickListener(new searchButtonClickListener());

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
            mBarText.setText("Browse Affirmations");
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
