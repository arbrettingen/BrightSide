package com.soapbox.brightside;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.DataSetObserver;
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
import android.widget.Toast;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        setTitle("Reminders");

        //initialize views
        mNotificationsSwitch = (Switch) findViewById(R.id.reminders_switch_notifications);
        mReminderList = (Spinner) findViewById(R.id.reminders_spinner_reminder_type);
        mReminderMessgageText = (EditText) findViewById(R.id.reminders_message_edittext);
        mReminderMessageButton = (Button) findViewById(R.id.reminders_message_btn);
        mReminderTimeText = (TextView) findViewById(R.id.reminders_time_text);
        mReminderTimeButton = (ImageView) findViewById(R.id.reminders_timechange_btn);
        mReminderTypeText = (TextView) findViewById(R.id.reminders_type_text);

        //preferences init

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

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
                } else{
                    mReminderList.setVisibility(View.GONE);
                    mReminderMessgageText.setVisibility(View.GONE);
                    mReminderMessageButton.setVisibility(View.GONE);
                    mReminderTimeText.setVisibility(View.GONE);
                    mReminderTimeButton.setVisibility(View.GONE);
                    mReminderTypeText.setVisibility(View.GONE);

                    editor.putBoolean("Notifications", false);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

}
