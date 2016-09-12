package com.test.segments.matcher.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class Assert {
    public static class NullOrEmptyException extends RuntimeException {
        public NullOrEmptyException(String message) {
            super(message);
        }
    }

    public static <T> T notEmpty(String message, T value) {
        if (value == null) {
            throw new NullOrEmptyException(message);
        } else if (Collection.class.isAssignableFrom(value.getClass())) {
            Collection casting = (Collection) value;
            if (casting.size() == 0) {
                throw new NullOrEmptyException(message);
            }
        } else if (Set.class.isAssignableFrom(value.getClass())) {
            Set casting = (Set) value;
            if (casting.size() == 0) {
                throw new NullOrEmptyException(message);
            }
        } else if(Map.class.isAssignableFrom(value.getClass())) {
            Map casting = (Map) value;
            if (casting.size() == 0) {
                throw new NullOrEmptyException(message);
            }
        } else if (Object[].class.isAssignableFrom(value.getClass())) {
            Object[] casting = (Object[]) value;
            if (casting.length == 0) {
                throw new NullOrEmptyException(message);
            }
        }
        return value;
    }
}
