package com.soapbox.brightside;

import android.content.Context;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Alex on 2/21/2017.
 */

public class NavListAdapter extends ArrayAdapter<String> {

    Context mContext;
    int mLayoutResourceID;
    String[] mData;

    public NavListAdapter(Context context, int resource, String[] data) {
        super(context, resource, data);
        this.mContext = context;
        this.mLayoutResourceID = resource;
        this.mData = data;
    }


    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }


    @Override
    public View getView(int position, View row, ViewGroup parent) {

        NavHolder holder;

        if (row == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(mLayoutResourceID, parent, false);

            holder = new NavHolder();

            holder.mediumText = (TextView) row.findViewById(R.id.nameTextView);

            row.setTag(holder);
        }

        else{
            holder = (NavHolder) row.getTag();
        }

        holder.mediumText.setText(mData[position]);




        /*
        int resID = mContext.getResources().getIdentifier(place.mNameOfImage, "drawable", mContext.getPackageName());
        holder.imageView.setImageResource(resID);
         */

        return row;
    }

    private static class NavHolder{

        TextView mediumText;

    }


}
