package com.soapbox.brightside;

/**
 * Created by Alex on 3/24/2017.
 */

public class Affirmation {

    private String mImgSrc;
    private String mAffirmationBody;

    public Affirmation(String imgSrc, String affirmationBody){
        mImgSrc = imgSrc;
        mAffirmationBody = affirmationBody;
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
}
