package com.soapbox.brightside;

/**
 * Created by Alex on 3/24/2017.
 */

public class Affirmation {

    private String mImgSrc;
    private String mAffirmationBody;
    private boolean mSatisfactionFlag;
    private boolean mConfidenceFlag;
    private boolean mMotivationFlag;
    private boolean mFavorited;
    private boolean mHidden;

    public Affirmation(String affirmationBody, String imgSrc, boolean satisfactionFlag, boolean confidenceFlag, boolean motivationFlag){
        mImgSrc = imgSrc;
        mAffirmationBody = affirmationBody;
        mSatisfactionFlag = satisfactionFlag;
        mConfidenceFlag = confidenceFlag;
        mMotivationFlag = motivationFlag;
        mFavorited = false;
    }

    public Affirmation(String affirmationBody, boolean satisfactionFlag, boolean confidenceFlag, boolean motivationFlag){
        mImgSrc = "";
        mAffirmationBody = affirmationBody;
        mSatisfactionFlag = satisfactionFlag;
        mConfidenceFlag = confidenceFlag;
        mMotivationFlag = motivationFlag;
        mFavorited = false;
    }

    public Affirmation(String imgSrc, String affirmationBody){
        mImgSrc = imgSrc;
        mAffirmationBody = affirmationBody;
        mFavorited = false;
    }

    public Affirmation(String affirmationBody){
        mAffirmationBody = affirmationBody;
        mImgSrc = "";
        mFavorited = false;
    }

    public String getImgSrc() {
        return mImgSrc;
    }

    public void setImgSrc(String mImgSrc) {
        this.mImgSrc = mImgSrc;
    }

    public String getAffirmationBody() {
        return mAffirmationBody;
    }

    public void setAffirmationBody(String mAffirmationBody) {
        this.mAffirmationBody = mAffirmationBody;
    }

    public boolean isSatisfactionFlag() {
        return mSatisfactionFlag;
    }

    public void setSatisfactionFlag(boolean mSatisfactionFlag) {
        this.mSatisfactionFlag = mSatisfactionFlag;
    }

    public boolean isConfidenceFlag() {
        return mConfidenceFlag;
    }

    public void setConfidenceFlag(boolean mConfidenceFlag) {
        this.mConfidenceFlag = mConfidenceFlag;
    }

    public boolean isMotivationFlag() {
        return mMotivationFlag;
    }

    public void setMotivationFlag(boolean mMotivationFlag) {
        this.mMotivationFlag = mMotivationFlag;
    }

    public boolean isFavorited() {
        return mFavorited;
    }

    public void setFavorited(boolean mFavorited) {
        this.mFavorited = mFavorited;
    }

    public boolean isHidden() {
        return mHidden;
    }

    public void setHidden(boolean mHidden) {
        this.mHidden = mHidden;
    }

    public String getShortenedBody(){
        if (mAffirmationBody.length() > 100){
            return mAffirmationBody.substring(0,98) + "...";
        }
        return mAffirmationBody;
    }

    @Override
    public String toString() {
        return mAffirmationBody;
    }
}
