package com.soapbox.brightside;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Alex on 3/24/2017.
 */

public class AffirmationPlaylist {

    private ArrayList<Affirmation> mAffirmationList;
    private String mName;

    public AffirmationPlaylist(String name){
        mName = name;
    }

    public String getPlaylistName() {
        return mName;
    }

    public void setPlaylistName(String mName) {
        this.mName = mName;
    }

    public ArrayList<Affirmation> getAffirmationList() {
        return mAffirmationList;
    }

    public void setAffirmationList(ArrayList<Affirmation> mAffirmationList) {
        this.mAffirmationList = mAffirmationList;
    }



    @Override
    public String toString() {
        return mName;
    }
}
