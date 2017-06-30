package com.soapbox.brightside.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.soapbox.brightside.data.AffirmationContract.AffirmationEntry;
import com.soapbox.brightside.data.PlaylistContract.PlaylistEntry;



/**
 * Database helper for brightside app. Manages database creation and version management.
 */
public class AffirmationDbHelper extends SQLiteOpenHelper {

    /** Name of the database file */
    private static final String DATABASE_NAME = "brightside.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link AffirmationDbHelper}.
     *
     * @param context of the app
     */
    public AffirmationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table

        String SQL_CREATE_AFFIRMATIONS_TABLE = "CREATE TABLE " + AffirmationEntry.TABLE_NAME + " ("
                + AffirmationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AffirmationEntry.COLUMN_AFFIRMATION_BODY + " TEXT NOT NULL, "
                + AffirmationEntry.COLUMN_AFFIRMATION_CREDIT + " TEXT, "
                + AffirmationEntry.COLUMN_AFFIRMATION_IMAGE + " TEXT, "
                + AffirmationEntry.COLUMN_AFFIRMATION_FAVORITED + " INTEGER NOT NULL DEFAULT 0, "
                + AffirmationEntry.COLUMN_AFFIRMATION_CONFIDENCE_FLAG + " INTEGER NOT NULL DEFAULT 0, "
                + AffirmationEntry.COLUMN_AFFIRMATION_MOTIVATION_FLAG + " INTEGER NOT NULL DEFAULT 0, "
                + AffirmationEntry.COLUMN_AFFIRMATION_SATISFACTION_FLAG + " INTEGER NOT NULL DEFAULT 0);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_AFFIRMATIONS_TABLE);

        String SQL_CREATE_PLAYLISTS_TABLE = "CREATE TABLE " + PlaylistEntry.TABLE_NAME + " ("
                + PlaylistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PlaylistEntry.COLUMN_PLAYLIST_NAME + " TEXT NOT NULL, "
                + PlaylistEntry.COLUMN_PLAYLIST_AFFIRMATION_ID + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_PLAYLISTS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}