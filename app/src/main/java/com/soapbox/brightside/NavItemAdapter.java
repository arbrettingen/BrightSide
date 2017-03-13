package com.soapbox.brightside;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Alex on 2/26/2017.
 */

public class NavItemAdapter extends ArrayAdapter {

    Context mContext;
    int mLayoutResourceId;
    NavItem[] mData = null;

    public NavItemAdapter(Context context, int resource, NavItem[] data){
        super(context, resource, data);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mData = data;
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        NavHolder holder;

        if (row == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new NavHolder();

            holder.largeText = (TextView) row.findViewById(R.id.largeTextView);
            holder.smallText = (TextView) row.findViewById(R.id.smallTextView);
            holder.imageView = (ImageView) row.findViewById(R.id.imageViewHome);

            row.setTag(holder);
        }

        else{
            holder = (NavHolder) row.getTag();
        }

        holder.largeText.setText(mData[position].mName);
        holder.smallText.setText(mData[position].mDescription);

        int resID = mContext.getResources().getIdentifier(mData[position].mNameOfImage, "drawable",
                mContext.getPackageName());
        holder.imageView.setImageResource(resID);

        return row;
    }


    private static class NavHolder{

        TextView largeText;
        TextView smallText;
        ImageView imageView;

    }


}
