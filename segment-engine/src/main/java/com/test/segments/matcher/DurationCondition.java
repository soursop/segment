package com.test.segments.matcher;

import java.io.Serializable;
import java.text.ParseException;

/**
 * Created by 1002707 on 2016. 4. 22..
 */
public class DurationCondition implements Serializable {
    private int startDate;
    private int conditionEndDate;
    private String endDate;
    private int durationDays;

    public DurationCondition(Parameter parameter, int durationDays) {
        this.endDate = parameter.getEndDate();
        this.conditionEndDate = Integer.parseInt(this.endDate);
        this.durationDays = durationDays;
        try {
            startDate = Integer.parseInt(parameter.getDaysAgoFromEndTime(-durationDays + 1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public DurationCondition(Parameter parameter, int prevDurationDays, int durationDays) {
        this.endDate = parameter.getEndDate();
        try {
            this.startDate = Integer.parseInt(parameter.getDaysAgoFromEndTime(-durationDays + 1));
            this.conditionEndDate = Integer.parseInt(parameter.getDaysAgoFromEndTime(-prevDurationDays));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.durationDays = durationDays;
    }

    public boolean isValidDate(int date) {
        return startDate <= date && date <= conditionEndDate;
    }

    public int getStartDate() {
        return startDate;
    }

    public int getConditionEndDate() {
        return conditionEndDate;
    }

    public int getDurationDays() {
        return durationDays;
    }

    @Override
    public String toString() {
        return "DurationCondition{" +
                "startDate='" + startDate + '\'' +
                ", conditionEndDate='" + conditionEndDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", durationDays=" + durationDays +
                '}';
    }
}
