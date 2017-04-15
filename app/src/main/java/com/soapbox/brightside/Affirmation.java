package com.soapbox.brightside;

/**
 * Created by Alex on 3/24/2017.
 */

public class Affirmation {

    private String mImgSrc;
    private String mAffirmationBody;
    //todo: rating feature
    //todo: happiness, confidence, motivation

    public Affirmation(String imgSrc, String affirmationBody){
        mImgSrc = imgSrc;
        mAffirmationBody = affirmationBody;
    }

    public Affirmation(String affirmationBody){
        mAffirmationBody = affirmationBody;
        mImgSrc = "";
    }

    public String getmImgSrc() {
        return mImgSrc;
    }

    public void setmImgSrc(String mImgSrc) {
        this.mImgSrc = mImgSrc;
    }

    public String getmAffirmationBody() {
        return mAffirmationBody;
    }

    public void setmAffirmationBody(String mAffirmationBody) {
        this.mAffirmationBody = mAffirmationBody;
    }

    @Override
    public String toString() {
        return mAffirmationBody;
    }
}
