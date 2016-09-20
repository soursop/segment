package com.test.segments.matcher;

import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;


public class Condition implements Conditionable {
    private String id;
    private FieldMeta fieldMeta;
    private ConditionSign sign;
    private String value;
    private String[] values;
    private int durationDays;
    private int frequency;
    private Status status;
    private int hashCode;
    private static Pattern slicePattern = Pattern.compile("\\|");

    public static Condition of(FieldMeta fieldMeta, ConditionSign sign, String value, int durationDays, int frequency, Status status) {
        return of(null, fieldMeta, sign, value, durationDays, frequency, status);
    }

    public static Condition of(String id, FieldMeta fieldMeta, ConditionSign sign, String value, int durationDays, int frequency, Status status) {
        Condition condition = new Condition();
        condition.id = id;
        condition.fieldMeta = fieldMeta;
        condition.sign = sign;
        condition.value = value;
        condition.values = getResolvedTarget(value);
        condition.durationDays = durationDays;
        condition.frequency = frequency;
        condition.status = status;
        condition.hashCode = condition.getHashCode();
        return condition;
    }

    public boolean match(String[] text) {
        int index = fieldMeta.getIndex();
        if (index < 0 || text.length < (index + 1)) {
            return false;
        }
        return sign.evaluate(text[index], values);
    }

    @Override
    public boolean matchByConditionIds(Set<Condition> conditionIds) {
        return conditionIds == null ? false : conditionIds.contains(this);
    }

    private static String[] getResolvedTarget(String target) {
        target = DataHandler.TARGET_HANDLER.resolve(target);
        return slicePattern.split(target);
    }

    @Override
    public String toConditionString() {
        return "{" +
                "id=" + id +
                ", field=" + fieldMeta +
                ", sign=" + sign +
                ", value='" + value + '\'' +
                ", values='" + Arrays.asList(values) + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public int getFieldMetaDisplayOrder() {
        return fieldMeta.getIndex();
    }

    public ConditionSign getSign() {
        return sign;
    }

    public String getValue() {
        return value;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public int getFrequency(){
        return frequency;
    }

    public void setFrequency(int frequency){
        this.frequency = frequency;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public FieldMeta getFieldMeta() {
        return fieldMeta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Condition condition = (Condition) o;

        if (fieldMeta != null ? !fieldMeta.equals(condition.fieldMeta) : condition.fieldMeta != null) return false;
        if (sign != condition.sign) return false;
        if (durationDays != condition.durationDays) return false;
        if (frequency != condition.frequency) return false;
        return !(value != null ? !value.equals(condition.value) : condition.value != null);

    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    private int getHashCode() {
        int result = fieldMeta != null ? fieldMeta.hashCode() : 0;
        result = 31 * result + (sign != null ? sign.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + durationDays;
        result = 31 * result + frequency;
        return result;
    }

    public enum Status {
        ACTIVE, INACTIVE, DELETE
    }
}
