package com.soapbox.brightside;

/**
 * Created by Alex on 3/24/2017.
 */

public class Affirmation {

    private String mImgSrc;
    private String mAffirmationBody;
    private String mCredit;

    private String m_ID;
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
        mCredit = "";
    }

    public Affirmation(String affirmationBody, boolean satisfactionFlag, boolean confidenceFlag, boolean motivationFlag){
        mImgSrc = "";
        mAffirmationBody = affirmationBody;
        mSatisfactionFlag = satisfactionFlag;
        mConfidenceFlag = confidenceFlag;
        mMotivationFlag = motivationFlag;
        mFavorited = false;
        mCredit = "";
    }

    public Affirmation(String imgSrc, String affirmationBody){
        mImgSrc = imgSrc;
        mAffirmationBody = affirmationBody;
        mFavorited = false;
        mSatisfactionFlag = false;
        mConfidenceFlag = false;
        mMotivationFlag = false;
        mCredit = "";
    }

    public Affirmation(String affirmationBody){
        mAffirmationBody = affirmationBody;
        mImgSrc = "";
        mFavorited = false;
        mSatisfactionFlag = false;
        mConfidenceFlag = false;
        mMotivationFlag = false;
        mCredit = "";
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

    public String getCredit() {
        return mCredit;
    }

    public void setCredit(String mCredit) {
        this.mCredit = mCredit;
    }

    public String getShortenedBody(){
        if (mAffirmationBody.length() > 100){
            return mAffirmationBody.substring(0,98) + "...";
        }
        return mAffirmationBody;
    }

    public String getM_ID() {
        return m_ID;
    }

    public void setM_ID(String m_ID) {
        this.m_ID = m_ID;
    }

    @Override
    public String toString() {
        return mAffirmationBody;
    }
}
