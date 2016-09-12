package com.test.segments.matcher;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by sonegy@sk.com on 2016. 1. 26..
 */
public class Parameter implements Serializable {
    private String dataSource;
    private Conditions[] conditionses;
    private String endDate;
    private static String FORMAT = "yyyyMMdd";

    private Parameter(String dataSource, Conditions[] conditionses, String endDate) {
        this.dataSource = dataSource;
        this.conditionses = conditionses;
        this.endDate = endDate;
        if (this.conditionses == null) {
            this.conditionses = new Conditions[0];
        }
    }

    public static Parameter of(String dataSource, Conditions[] conditionses) {
        return of(dataSource, conditionses, getDaysAgo(-1));
    }

    public static Parameter of(String dataSource, Conditions[] conditionses, String endDate) {
        return new Parameter(dataSource, conditionses, endDate);
    }

    public String getDataSource() {
        return dataSource;
    }

    public String getEndDate() {
        return endDate;
    }

    public Conditions[] getConditionses() {
        return conditionses;
    }

    public static String getDaysAgo(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        return new SimpleDateFormat(FORMAT).format(calendar.getTime());
    }

    public static String getDaysAgoFrom(int days, String from) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(from));
        calendar.add(Calendar.DATE, days);
        return new SimpleDateFormat(FORMAT).format(calendar.getTime());
    }

    public String getDaysAgoFromEndTime(int days) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(endDate));
        calendar.add(Calendar.DATE, days);
        return format.format(calendar.getTime());
    }
}
