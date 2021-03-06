package com.soapbox.brightside.data;

import android.net.Uri;
import android.content.ContentResolver;
import android.provider.BaseColumns;

/**
 * API Contract
 */
public final class PlaylistContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private PlaylistContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.soapbox.brightside";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     */
    public static final String PATH_PLAYLISTS = "playlists";

    /**
     * Inner class that defines constant values for the playlists database table.
     * Each entry in the table represents a single playlist.
     */
    public static final class PlaylistEntry implements BaseColumns {

        /** The content URI to access the playlist data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PLAYLISTS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of playlists.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYLISTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single playlist.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYLISTS;

        /** Name of database table for playlists */
        public final static String TABLE_NAME = "playlists";

        /**
         * Unique ID number for the playlist (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * playlists name text
         *
         * Type: TEXT
         */
        public final static String COLUMN_PLAYLIST_NAME ="name";

        /**
         * playlists name text
         *
         * Type: TEXT
         */
        public final static String COLUMN_PLAYLIST_AFFIRMATION_ID ="a_ID";


    }

}