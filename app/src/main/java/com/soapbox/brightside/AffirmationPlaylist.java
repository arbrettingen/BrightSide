package com.soapbox.brightside;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Alex on 3/24/2017.
 */

public class AffirmationPlaylist implements Parcelable{

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<AffirmationPlaylist> CREATOR
            = new Parcelable.Creator<AffirmationPlaylist>() {
        public AffirmationPlaylist createFromParcel(Parcel in) {
            return new AffirmationPlaylist(in);
        }

        public AffirmationPlaylist[] newArray(int size) {
            return new AffirmationPlaylist[size];
        }
    };

    private AffirmationPlaylist(Parcel in) {

    }

    @Override
    public String toString() {
        return mName;
    }
}
