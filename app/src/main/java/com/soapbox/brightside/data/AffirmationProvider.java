package com.soapbox.brightside.data;

import android.content.ContentProvider;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.soapbox.brightside.data.*;

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
    }

}
