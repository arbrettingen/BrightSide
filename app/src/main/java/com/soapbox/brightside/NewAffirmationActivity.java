package com.soapbox.brightside;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.soapbox.brightside.data.AffirmationContract.AffirmationEntry;

/**
 * Created by Alex on 4/12/2017.
 */

public class NewAffirmationActivity extends AppCompatActivity {


    private String[] mNavChoices;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private Button mAddNewAffirmation;
    private EditText mAffirmationEdit;

    private String mAffirmationExtra;
    private int mAffirmationPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_affirmation);
        setTitle(R.string.drawer_close_new_affirmation);

        Intent i = getIntent();
        if (i.hasExtra("Affirmation Body")) {
            mAffirmationExtra = i.getStringExtra("Affirmation Body");
            mAffirmationPosition = i.getIntExtra("Affirmation Position", -1);
            setTitle("Edit Affirmation");
        }


        //drawer code below
        mNavChoices = getResources().getStringArray(R.array.array_nav_options);
        mDrawerList = (ListView) findViewById(R.id.new_affirmation_left_drawer);

        NavListAdapter mDrawerListAdapter = new NavListAdapter(getApplicationContext(), R.layout.row_nav_drawer, mNavChoices);

        mDrawerList.setAdapter(mDrawerListAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.new_affirmation_drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close_new_affirmation) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setTitle(R.string.drawer_close_new_affirmation);
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

        //add new button and edit text setup below
        mAffirmationEdit = (EditText) findViewById(R.id.new_affirmation_edit);
        int maxLength = 250;
        mAffirmationEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        mAddNewAffirmation = (Button) findViewById(R.id.btn_add_affirmation_confirm);

        if (mAffirmationExtra != null) {
            mAffirmationEdit.setText(mAffirmationExtra);
            mAddNewAffirmation.setText("SAVE");
        }

        mAddNewAffirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAffirmationEdit.getText().toString().equals("") || mAffirmationEdit.getText().toString().equals("Enter affirmation text here")) {
                    Toast.makeText(getApplicationContext(), "Affirmation text is invalid.", Toast.LENGTH_LONG).show();
                    return;
                }

                for (int i = 0; i < MainMenuActivity.masterAffirmationList.size(); i++){
                    if(MainMenuActivity.masterAffirmationList.get(i).getAffirmationBody().equals(mAffirmationEdit.getText().toString())){
                        if (mAffirmationPosition == -1){
                            Toast.makeText(getApplicationContext(), "Affirmation text has already been created.", Toast.LENGTH_LONG).show();
                            return;
                        } else{
                            if (i != mAffirmationPosition){
                                Toast.makeText(getApplicationContext(), "Affirmation text has already been created.", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    }
                }
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                if (mAffirmationExtra == null) {
                                    Affirmation newEntry = new Affirmation(mAffirmationEdit.getText().toString());
                                    MainMenuActivity.masterAffirmationList.add(newEntry);
                                    insertAffirmationToDb(newEntry);
                                } else {
                                    MainMenuActivity.masterAffirmationList.get(mAffirmationPosition).setAffirmationBody(mAffirmationEdit.getText().toString());
                                }

                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Affirmation successfully added", Toast.LENGTH_LONG).show();
                                mAffirmationEdit.setText("");

                                Intent i = new Intent(getApplicationContext(), BrowseActivity.class);
                                startActivity(i);

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };


                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(NewAffirmationActivity.this, R.style.myDialog));
                if (mAffirmationExtra == null) {
                    builder.setMessage("Are you sure you'd like to add this affirmation?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
                } else {
                    builder.setMessage("Are you sure you'd like to save the changes to this affirmation?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

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

    /**
     * Handles navigation clicks in nav drawer, switches screens
     */
    private void selectItemDrawer(int position) {

        //mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(findViewById(R.id.new_affirmation_drawer_cont));

        Intent i;
        switch (position) {
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

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemDrawer(position);
        }
    }

    private void insertAffirmationToDb(Affirmation a) {
        // Create a ContentValues object where column names are the keys,
        // and affirmation attributes are the values.
        ContentValues values = new ContentValues();
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_BODY, a.getAffirmationBody());
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_CREDIT, a.getCredit());
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_FAVORITED, a.isFavorited());
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_IMAGE, a.getImgSrc());
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_SATISFACTION_FLAG, a.isSatisfactionFlag());
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_MOTIVATION_FLAG, a.isMotivationFlag());
        values.put(AffirmationEntry.COLUMN_AFFIRMATION_CONFIDENCE_FLAG, a.isConfidenceFlag());

        // Use the {@link AffirmationEntry#CONTENT_URI} to indicate that we want to insert
        // into the affirmations database table.
        // Receive the new content URI that will allow us to access row's data in the future.
        Uri newUri = getContentResolver().insert(AffirmationEntry.CONTENT_URI, values);

        

    }

}



