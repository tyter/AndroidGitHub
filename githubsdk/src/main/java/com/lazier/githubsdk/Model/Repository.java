package com.lazier.githubsdk.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by zhoukaifeng(zhoukaifeng@meituan.com) on 16/10/20.
 */

public class Repository {

    @SerializedName(value="id")
    public long mId;

    @SerializedName(value="name")
    public String mName;

    @SerializedName(value="full_name")
    public String mFullName;

    @SerializedName(value="description")
    public String mDescription;

    @SerializedName(value="html_url")
    public String mHtmlUrl;

    @SerializedName(value="clone_url")
    public String mCloneUrl;

    @SerializedName(value="language")
    public String mLanguage;

    @SerializedName(value="stargazers_count")
    public int mStargazersCount;

    @SerializedName(value="forks_count")
    public int mForksCount;

    @SerializedName(value="watchers_count")
    public int mWatchersCount;

    @SerializedName(value="created_at")
    public String mCreateAt;

    @SerializedName(value="pushed_at")
    public String mPushAt;

    @SerializedName(value="owner")
    public Owner mOwner;

    public static class Time {
        public int mYear;
        public int mMonth;
        public int mDay;
        public int mHour;
        public int mMinute;
        public int mSecond;

        public static final int TIME_FORMAT_LENGTH = 20;

        public static Time parse(Calendar calendar) {
            Time time = new Time();
            time.mYear = calendar.get(Calendar.YEAR);
            time.mMonth = calendar.get(Calendar.MONTH);
            time.mDay = calendar.get(Calendar.DAY_OF_MONTH);
            time.mHour = calendar.get(Calendar.HOUR_OF_DAY);
            time.mMinute = calendar.get(Calendar.MINUTE);
            time.mSecond = calendar.get(Calendar.SECOND);
            return time;
        }

        public static Time parse(String timeStr) {

            if (TextUtils.isEmpty(timeStr) || timeStr.length() != TIME_FORMAT_LENGTH) {
                return null;
            }

            Time time = new Time();

            try {

                timeStr = timeStr.substring(0, timeStr.length() - 1);
                String[] array = timeStr.split("T");
                String[] dateArray = array[0].split("-");

                time.mYear = Integer.parseInt(dateArray[0]);
                time.mMonth = Integer.parseInt(dateArray[1]);
                time.mDay = Integer.parseInt(dateArray[2]);

                String[] timeArray = array[1].split(":");
                time.mHour = Integer.parseInt(timeArray[0]);
                time.mMinute = Integer.parseInt(timeArray[1]);
                time.mSecond = Integer.parseInt(timeArray[2]);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return time;
        }

        public String printNormal() {
            return String.format(Locale.getDefault(), "%4d-%2d-%2d %2d:%2d:%2d",
                    mYear, mMonth, mDay, mHour, mMinute, mSecond);
        }
    }
}
