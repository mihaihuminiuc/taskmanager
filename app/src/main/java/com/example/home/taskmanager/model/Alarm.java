package com.example.home.taskmanager.model;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.home.taskmanager.activity.AlarmNotificationActivity;
import com.orm.SugarRecord;


public class Alarm extends SugarRecord implements Parcelable {

    protected Alarm(Parcel in) {
        mTitle = in.readString();
        mDate = in.readLong();
        mEnabled = in.readByte() != 0;
        mOccurrence = in.readInt();
        mDays = in.readInt();
    }

    public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public long getmDate() {
        return mDate;
    }

    public void setmDate(long mDate) {
        this.mDate = mDate;
    }

    public boolean ismEnabled() {
        return mEnabled;
    }

    public void setmEnabled(boolean mEnabled) {
        this.mEnabled = mEnabled;
    }

    public int getmOccurrence() {
        return mOccurrence;
    }

    public void setmOccurrence(int mOccurrence) {
        this.mOccurrence = mOccurrence;
    }

    public int getmDays() {
        return mDays;
    }

    public void setmDays(int mDays) {
        this.mDays = mDays;
    }

    public Alarm(){}

    private String mTitle;
    private long mDate;
    private boolean mEnabled;
    private int mOccurrence;
    private int mDays;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeLong(mDate);
        parcel.writeByte((byte) (mEnabled ? 1 : 0));
        parcel.writeInt(mOccurrence);
        parcel.writeInt(mDays);
    }

    public static Alarm fromIntent(Intent intent){
        return intent.getExtras().getParcelable(AlarmNotificationActivity.MODE_NAME);
    }

    public static void toIntent(Intent intent, Alarm alarm){
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putParcelable(AlarmNotificationActivity.MODE_NAME,alarm);
        intent.putExtra(AlarmNotificationActivity.MODE_NAME,bundle);
    }
}

