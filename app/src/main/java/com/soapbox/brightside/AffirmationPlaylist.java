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
    private Integer mPlaylist_ID;

    public AffirmationPlaylist(String name){
        mName = name;
        mAffirmationList = new ArrayList<>();
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

    public Integer getmPlaylist_ID() {
        return mPlaylist_ID;
    }

    public void setmPlaylist_ID(Integer mPlaylist_ID) {
        this.mPlaylist_ID = mPlaylist_ID;
    }

    @Override
    public String toString() {
        return mName;
    }
}
