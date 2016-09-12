package com.test.segments.matcher;

/**
 * Created by sonegy@sk.com on 2016. 2. 24..
 */
public enum FieldMetaType {
    STRING("문자"), INTEGER("숫자");

    final String description;

    FieldMetaType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static FieldMetaType[] STRING_TYPE = new FieldMetaType[]{STRING};
    public static FieldMetaType[] INTEGER_TYPE = new FieldMetaType[]{INTEGER};
    public static FieldMetaType[] STRING_INTEGER_TYPE = new FieldMetaType[]{STRING, INTEGER};
}
