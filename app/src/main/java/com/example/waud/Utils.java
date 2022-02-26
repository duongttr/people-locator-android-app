package com.example.waud;

import android.text.format.DateUtils;

import java.util.Date;

public class Utils {

    static final long AVERAGE_MONTH_IN_MILLIS = DateUtils.DAY_IN_MILLIS * 30;

    public static String getRelationTime(long time) {
        final long now = new Date().getTime();
        final long delta = now - time;
        long resolution;
        if (delta <= DateUtils.MINUTE_IN_MILLIS) {
            resolution = DateUtils.SECOND_IN_MILLIS;
        } else if (delta <= DateUtils.HOUR_IN_MILLIS) {
            resolution = DateUtils.MINUTE_IN_MILLIS;
        } else if (delta <= DateUtils.DAY_IN_MILLIS) {
            resolution = DateUtils.HOUR_IN_MILLIS;
        } else if (delta <= DateUtils.WEEK_IN_MILLIS) {
            resolution = DateUtils.DAY_IN_MILLIS;
        } else if (delta <= AVERAGE_MONTH_IN_MILLIS) {
            return (int) (delta / DateUtils.WEEK_IN_MILLIS) + " weeks(s) ago";
        } else if (delta <= DateUtils.YEAR_IN_MILLIS) {
            return (int) (delta / AVERAGE_MONTH_IN_MILLIS) + " month(s) ago";
        } else {
            return (int) (delta / DateUtils.YEAR_IN_MILLIS) + " year(s) ago";
        }
        return DateUtils.getRelativeTimeSpanString(time, now, resolution).toString();
    }
}
