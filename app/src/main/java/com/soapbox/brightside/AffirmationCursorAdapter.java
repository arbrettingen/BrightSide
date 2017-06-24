package com.soapbox.brightside;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.soapbox.brightside.data.AffirmationContract.AffirmationEntry;

/**
 * {@link AffirmationCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of affirmation data as its data source. This adapter knows
 * how to create list items for each row of affirmation data in the {@link Cursor}.
 */
public class AffirmationCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link AffirmationCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public AffirmationCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.affirmation_list_row_cursor, parent, false);
    }

    /**
     * This method binds the affirmation data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the text for the current affirmation can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView bodyTextView = (TextView) view.findViewById(R.id.affirmation_row_text);

        // Find the columns of affirmation attributes that we're interested in
        int bodyColumnIndex = cursor.getColumnIndex(AffirmationEntry.COLUMN_AFFIRMATION_BODY);

        // Read the affirmation attributes from the Cursor for the current affirmation
        String affirmationBody = cursor.getString(bodyColumnIndex);

        // Update the TextViews with the attributes for the current affirmation
        bodyTextView.setText(affirmationBody);
    }
}