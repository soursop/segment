package com.test.segments.matcher;

import com.test.segments.matcher.utils.Assert;

import java.io.Serializable;
import java.util.List;


public class FieldMeta implements Serializable {
    private String key;
    private FieldMetaType clazz;
    private String description;
    private int index;

    public FieldMeta() {
    }

    private FieldMeta(String key, FieldMetaType clazz, String description, int index) {
        Assert.notEmpty("FieldMeta key value is empty", key);
        Assert.notEmpty("FieldMeta clazz value is empty", clazz);

        this.key = key;
        this.clazz = clazz;
        this.description = description;
        this.index = index;
    }

    public static FieldMeta of(String key, FieldMetaType clazz, String description, int index) {
        return new FieldMeta(key, clazz, description, index);
    }

    public static FieldMeta findByKey(List<FieldMeta> allFiledMetas, String key) {
        for (int i = 0; i < allFiledMetas.size(); i++) {
            if (allFiledMetas.get(i).getKey().equals(key)) {
                return allFiledMetas.get(i);
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public FieldMetaType getClazz() {
        return clazz;
    }

    public void setClazz(FieldMetaType clazz) {
        this.clazz = clazz;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldMeta fieldMeta = (FieldMeta) o;

        if (key != null ? !key.equals(fieldMeta.key) : fieldMeta.key != null) return false;
        return clazz == fieldMeta.clazz;

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (clazz != null ? clazz.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return key;
    }
}
