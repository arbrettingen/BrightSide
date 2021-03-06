package com.soapbox.brightside.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.soapbox.brightside.data.AffirmationContract.AffirmationEntry;
import com.soapbox.brightside.data.PlaylistContract.PlaylistEntry;

/**
 * Brightside SQL
 */

public class AffirmationProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = AffirmationProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the affirmations table */
    private static final int AFFIRMATIONS = 100;

    /** URI matcher code for the content URI for a single affirmation in the affirmations table */
    private static final int AFFIRMATION_ID = 101;

    /** URI matcher code for the content URI for the playlists table */
    private static final int PLAYLISTS = 200;

    /** URI matcher code for the content URI for a single playlist in the affirmations table */
    private static final int PLAYLIST_ID = 201;

    /** URI matcher code for the content URI for a single playlist name in the affirmations table */

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.xxx/xxx" will map to the
        // integer code {@link #XXXX}. This URI is used to provide access to MULTIPLE rows
        // of the pets table.
        sUriMatcher.addURI(AffirmationContract.CONTENT_AUTHORITY, AffirmationContract.PATH_AFFIRMATIONS, AFFIRMATIONS);

        // The content URI of the form "content://com.example.android.xxx/xxx/#" will map to the
        // integer code {@link #XXX_ID}. This URI is used to provide access to ONE single row
        // of the xxx table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.android.xxx/xxx/3" matches, but
        // "content://com.example.android.xxx/xxx" (without a number at the end) doesn't match.
        sUriMatcher.addURI(AffirmationContract.CONTENT_AUTHORITY, AffirmationContract.PATH_AFFIRMATIONS + "/#", AFFIRMATION_ID);

        sUriMatcher.addURI(PlaylistContract.CONTENT_AUTHORITY, PlaylistContract.PATH_PLAYLISTS, PLAYLISTS);
        sUriMatcher.addURI(PlaylistContract.CONTENT_AUTHORITY, PlaylistContract.PATH_PLAYLISTS + "/#", PLAYLIST_ID);
    }

    /** Database helper object */
    private AffirmationDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new AffirmationDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case AFFIRMATIONS:
                // For the AFFIRMATIONS code, query the affirmations table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the affirmations table.
                cursor = database.query(AffirmationEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case AFFIRMATION_ID:
                // For the AFFIRMATION_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.xxx/xxx/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = AffirmationEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the affirmations table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(AffirmationEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PLAYLISTS:
                cursor = database.query(PlaylistContract.PlaylistEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PLAYLIST_ID:
                selection = PlaylistContract.PlaylistEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the affirmations table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(PlaylistContract.PlaylistEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case AFFIRMATIONS:
                return insertAffirmation(uri, contentValues);
            case PLAYLISTS:
                return insertPlaylist(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a playlist entry into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */

    private Uri insertPlaylist(Uri uri, ContentValues values){
        // Check that the name is not null
        String name = values.getAsString(PlaylistEntry.COLUMN_PLAYLIST_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Playlist requires name text.");
        }
        //check that affirmation ID is not null or less than 1
        Integer affirmation_ID = values.getAsInteger(PlaylistEntry.COLUMN_PLAYLIST_AFFIRMATION_ID);
        if (affirmation_ID == null || affirmation_ID < 0){
            throw new IllegalArgumentException("Associated affirmation ID must be a positive integer.");
        }

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new playlist entry with the given values
        long id = database.insert(PlaylistEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the affirmation content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Insert an affirmation into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertAffirmation(Uri uri, ContentValues values) {
        // Check that the body is not null
        String body = values.getAsString(AffirmationEntry.COLUMN_AFFIRMATION_BODY);
        if (body == null) {
            throw new IllegalArgumentException("Affirmation requires body text.");
        }

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new affirmation with the given values
        long id = database.insert(AffirmationEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the affirmation content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case AFFIRMATIONS:
                return updateAffirmation(uri, contentValues, selection, selectionArgs);
            case AFFIRMATION_ID:
                // For the AFFIRMATION_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = AffirmationEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateAffirmation(uri, contentValues, selection, selectionArgs);
            case PLAYLISTS:
                return updatePlaylist(uri, contentValues, selection, selectionArgs);
            case PLAYLIST_ID:
                selection = PlaylistEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updatePlaylist(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more affirmations).
     * Return the number of rows that were successfully updated.
     */
    private int updateAffirmation(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link AffirmationEntry#COLUMN_AFFIRMATION_BODY} key is present,
        // check that the body value is not null.
        if (values.containsKey(AffirmationEntry.COLUMN_AFFIRMATION_BODY)) {
            String body = values.getAsString(AffirmationEntry.COLUMN_AFFIRMATION_BODY);
            if (body == null) {
                throw new IllegalArgumentException("Affirmation requires a body of text");
            }
        }

        if (values.containsKey(AffirmationEntry.COLUMN_AFFIRMATION_FAVORITED)){
            Integer val = values.getAsInteger(AffirmationEntry.COLUMN_AFFIRMATION_FAVORITED);
            if (val == null){
                throw new IllegalArgumentException("Affirmation favorite status cannot be null.");
            }
            if (val != 0 && val != 1){
                throw new IllegalArgumentException("Affirmation favorite status must be either 0 or 1.");
            }
        }

        if (values.containsKey(AffirmationEntry.COLUMN_AFFIRMATION_CONFIDENCE_FLAG)){
            Integer val = values.getAsInteger(AffirmationEntry.COLUMN_AFFIRMATION_CONFIDENCE_FLAG);
            if (val == null){
                throw new IllegalArgumentException("Affirmation confidence flag status cannot be null.");
            }
            if (val != 0 && val != 1){
                throw new IllegalArgumentException("Affirmation confidence flag status must be either 0 or 1.");
            }
        }

        if (values.containsKey(AffirmationEntry.COLUMN_AFFIRMATION_MOTIVATION_FLAG)){
            Integer val = values.getAsInteger(AffirmationEntry.COLUMN_AFFIRMATION_MOTIVATION_FLAG);
            if (val == null){
                throw new IllegalArgumentException("Affirmation motivation flag status cannot be null.");
            }
            if (val != 0 && val != 1){
                throw new IllegalArgumentException("Affirmation motivation flag status must be either 0 or 1.");
            }
        }

        if (values.containsKey(AffirmationEntry.COLUMN_AFFIRMATION_SATISFACTION_FLAG)){
            Integer val = values.getAsInteger(AffirmationEntry.COLUMN_AFFIRMATION_SATISFACTION_FLAG);
            if (val == null){
                throw new IllegalArgumentException("Affirmation satisfaction flag status cannot be null.");
            }
            if (val != 0 && val != 1){
                throw new IllegalArgumentException("Affirmation satisfaction flag status must be either 0 or 1.");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(AffirmationEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more playlist entries).
     * Return the number of rows that were successfully updated.
     */
    private int updatePlaylist(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(PlaylistEntry.COLUMN_PLAYLIST_AFFIRMATION_ID)){
            Integer val = values.getAsInteger(PlaylistEntry.COLUMN_PLAYLIST_AFFIRMATION_ID);
            if (val == null){
                throw new IllegalArgumentException("Affirmation ID cannot be null.");
            }
            if (val < 0){
                throw new IllegalArgumentException("Affirmation ID cannot be negative.");
            }
        }

        if (values.containsKey(PlaylistEntry.COLUMN_PLAYLIST_NAME)) {
            String body = values.getAsString(PlaylistEntry.COLUMN_PLAYLIST_NAME);
            if (body == null) {
                throw new IllegalArgumentException("Playlist entry requires playlist name text");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(PlaylistEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case AFFIRMATIONS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(AffirmationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case AFFIRMATION_ID:
                // Delete a single row given by the ID in the URI
                selection = AffirmationEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(AffirmationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PLAYLISTS:
                rowsDeleted = database.delete(PlaylistEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PLAYLIST_ID:
                selection = PlaylistEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(PlaylistEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case AFFIRMATIONS:
                return AffirmationEntry.CONTENT_LIST_TYPE;
            case AFFIRMATION_ID:
                return AffirmationEntry.CONTENT_ITEM_TYPE;
            case PLAYLISTS:
                return PlaylistEntry.CONTENT_LIST_TYPE;
            case PLAYLIST_ID:
                return PlaylistEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
