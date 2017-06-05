package com.soapbox.brightside;

import android.animation.TimeInterpolator;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alex on 2/28/2017.
 */

public class RemindersActivity extends AppCompatActivity {

    private String[] mNavChoices;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private Switch mNotificationsSwitch;
    private String[] mReminderChoices;
    private Spinner mReminderList;
    private EditText mReminderMessgageText;
    private Button mReminderMessageButton;
    private TextView mReminderTimeText;
    private ImageView mReminderTimeButton;
    private TextView mReminderTypeText;

    static final int PLAYLIST_LIST_REQUEST = 1;  // The request code




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        setTitle("Reminders");

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        //initialize views
        mNotificationsSwitch = (Switch) findViewById(R.id.reminders_switch_notifications);
        mReminderList = (Spinner) findViewById(R.id.reminders_spinner_reminder_type);
        mReminderMessgageText = (EditText) findViewById(R.id.reminders_message_edittext);
        mReminderMessageButton = (Button) findViewById(R.id.reminders_message_btn);
        mReminderTimeText = (TextView) findViewById(R.id.reminders_time_text);
        mReminderTimeButton = (ImageView) findViewById(R.id.reminders_timechange_btn);
        mReminderTypeText = (TextView) findViewById(R.id.reminders_type_text);

        //preferences init



        //notification reminder switch below

        if (sharedPref.getBoolean("Notifications", false)){
            mReminderList.setVisibility(View.VISIBLE);
            mReminderMessgageText.setVisibility(View.VISIBLE);
            mReminderMessageButton.setVisibility(View.VISIBLE);
            mReminderTimeText.setVisibility(View.VISIBLE);
            mReminderTimeButton.setVisibility(View.VISIBLE);
            mReminderTypeText.setVisibility(View.VISIBLE);

            mNotificationsSwitch.setChecked(true);
        }
        else{
            mReminderList.setVisibility(View.GONE);
            mReminderMessgageText.setVisibility(View.GONE);
            mReminderMessageButton.setVisibility(View.GONE);
            mReminderTimeText.setVisibility(View.GONE);
            mReminderTimeButton.setVisibility(View.GONE);
            mReminderTypeText.setVisibility(View.GONE);

            mNotificationsSwitch.setChecked(false);
        }

        mNotificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    mReminderList.setVisibility(View.VISIBLE);
                    mReminderMessgageText.setVisibility(View.VISIBLE);
                    mReminderMessageButton.setVisibility(View.VISIBLE);
                    mReminderTimeText.setVisibility(View.VISIBLE);
                    mReminderTimeButton.setVisibility(View.VISIBLE);
                    mReminderTypeText.setVisibility(View.VISIBLE);

                    editor.putBoolean("Notifications", true);

                    Toast.makeText(getApplicationContext(), "Notifications set for " + mReminderTimeText.getText() + ".", Toast.LENGTH_LONG).show();
                } else{
                    mReminderList.setVisibility(View.GONE);
                    mReminderMessgageText.setVisibility(View.GONE);
                    mReminderMessageButton.setVisibility(View.GONE);
                    mReminderTimeText.setVisibility(View.GONE);
                    mReminderTimeButton.setVisibility(View.GONE);
                    mReminderTypeText.setVisibility(View.GONE);

                    editor.putBoolean("Notifications", false);

                    Toast.makeText(getApplicationContext(), "Notifications turned off.", Toast.LENGTH_LONG).show();
                }

                editor.apply(); //editor.commit();?
            }
        });

        //reminder choices list below
        mReminderChoices = getResources().getStringArray(R.array.array_reminder_options);
        SpinnerAdapter mReminderAdapter = new MySpinnerAdapter();
        mReminderList.setAdapter(mReminderAdapter);

        //if there is a shared preference, get it and set it to the selected value
        int initValue = sharedPref.getInt("Reminder Type", 0);
        mReminderList.setSelection(initValue);

        mReminderList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt("Reminder Type", position);
                editor.apply();

                mReminderMessgageText.setText(setMessageText(mReminderList.getItemAtPosition(position).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //message text handling below

        String messageText = setMessageText(mReminderList.getSelectedItem().toString());

        mReminderMessgageText.setText(messageText);

        //time pick setup below

        mReminderTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                TimePickerDialog tp1 = new TimePickerDialog(RemindersActivity.this, new MyTimePickerListener(), cal.get(java.util.Calendar.HOUR_OF_DAY), cal.get(java.util.Calendar.MINUTE), false);
                tp1.show();
            }
        });

        //time text setup below

        if (!sharedPref.getString("Reminder Time", "").equals("")){
            mReminderTimeText.setText(sharedPref.getString("Reminder Time", ""));
        }

        //drawer code below
        mNavChoices = getResources().getStringArray(R.array.array_nav_options);
        mDrawerList = (ListView) findViewById(R.id.reminders_left_drawer);

        NavListAdapter mDrawerListAdapter = new NavListAdapter(getApplicationContext(), R.layout.row_nav_drawer, mNavChoices);

        mDrawerList.setAdapter(mDrawerListAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.reminders_drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close_reminders){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setTitle("Reminders");
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
        mDrawerLayout.closeDrawer(findViewById(R.id.reminders_drawer_cont));

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

    private String setMessageText(String reminderType){
        //todo

        String ret = "";
        int randomPosition;
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        switch (reminderType) {
            case "Random Affirmation":

                Toast.makeText(getApplicationContext(), reminderType, Toast.LENGTH_SHORT).show();

                randomPosition = randomWithRange(0,MainMenuActivity.masterAffirmationList.size()-1);
                ret = MainMenuActivity.masterAffirmationList.get(randomPosition).getAffirmationBody();

                break;
            case "Specific Affirmation":

                if (sharedPref.getString("Specific Affirmation", "").equals("")){
                    randomPosition = randomWithRange(0,MainMenuActivity.masterAffirmationList.size()-1);
                    ret = MainMenuActivity.masterAffirmationList.get(randomPosition).getAffirmationBody();
                    editor.putString("Specific Affirmation", ret);
                    editor.apply();
                }else{
                    ret = sharedPref.getString("Specific Affirmation", "");
                }

                break;
            case "From Playlist":

                if (sharedPref.getString("From Playlist", "").equals("")){
                    if (CustomPlaylistActivity.masterAffirmationPlaylistList == null) {
                        CustomPlaylistActivity.masterAffirmationPlaylistList = new ArrayList<>();
                        CustomPlaylistActivity.masterAffirmationPlaylistList.add(new AffirmationPlaylist("Favorites"));
                        for (Affirmation a : MainMenuActivity.masterAffirmationList){
                            if (a.isFavorited()){
                                CustomPlaylistActivity.masterAffirmationPlaylistList.get(0).getAffirmationList().add(a);
                            }
                        }
                    }
                    randomPosition = randomWithRange(0, CustomPlaylistActivity.masterAffirmationPlaylistList.size()-1);
                    ret = CustomPlaylistActivity.masterAffirmationPlaylistList.get(randomPosition).getPlaylistName();

                    editor.putString("From Playlist", ret);
                    editor.apply();
                }else{
                    ret = sharedPref.getString("From Playlist", "");
                }

                break;
            case "Custom Message":

                ret = sharedPref.getString("Custom Message", "");

                break;
            default:
                break;
        }

        return ret;
    }


    private class MySpinnerAdapter extends BaseAdapter implements SpinnerAdapter{
        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object getItem(int position) {
            return mReminderChoices[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView mSimpleText = new TextView(getApplicationContext());
            mSimpleText.setText(mReminderChoices[position]);
            mSimpleText.setTextColor(getColor(R.color.colorPrimary));
            mSimpleText.setTextSize(18);
            return mSimpleText;
        }
    }

    private class MyTimePickerListener implements TimePickerDialog.OnTimeSetListener{

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPref.edit();

            try {
                String _24HourTime = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                Date _24HourDt = _24HourSDF.parse(_24HourTime);
                String clockTime = _12HourSDF.format(_24HourDt);
                mReminderTimeText.setText(clockTime);


                editor.putString("Reminder Time", clockTime);
                editor.apply();

                Toast.makeText(getApplicationContext(), "Notifications set for " + mReminderTimeText.getText() + ".", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

}
