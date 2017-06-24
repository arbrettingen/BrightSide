package com.soapbox.brightside.data;

import android.net.Uri;
import android.content.ContentResolver;
import android.provider.BaseColumns;

/**
 * API Contract
 */
public final class AffirmationContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private AffirmationContract() {}

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
    public static final String PATH_AFFIRMATIONS = "affirmations";

    /**
     * Inner class that defines constant values for the affirmations database table.
     * Each entry in the table represents a single affirmation.
     */
    public static final class AffirmationEntry implements BaseColumns {

        /** The content URI to access the affirmation data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_AFFIRMATIONS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of affirmations.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_AFFIRMATIONS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single affirmation.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_AFFIRMATIONS;

        /** Name of database table for affirmations */
        public final static String TABLE_NAME = "affirmations";

        /**
         * Unique ID number for the affirmation (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Affirmations body text
         *
         * Type: TEXT
         */
        public final static String COLUMN_AFFIRMATION_BODY ="body";

        /**
         * Whether the affirmation has been favorited.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_AFFIRMATION_FAVORITED = "favorited";

        /**
         * Image source for affirmation.
         *
         *
         * Type: TEXT
         */
        public final static String COLUMN_AFFIRMATION_IMAGE = "image";

        /**
         * Author credit for the affirmation.
         *
         * Type: TEXT
         */
        public final static String COLUMN_AFFIRMATION_CREDIT = "credit";

        /**
         * Whether the affirmation is categorized under motivation.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_AFFIRMATION_MOTIVATION_FLAG = "motivation";

        /**
         * Whether the affirmation is categorized under satisfaction.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_AFFIRMATION_SATISFACTION_FLAG = "satisfaction";

        /**
         * Whether the affirmation is categorized under confidence.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_AFFIRMATION_CONFIDENCE_FLAG = "confidence";
    }

}