package com.test.segments.matcher;

/**
 * Created by sonegy@sk.com on 2016. 2. 24..
 */
public class LongValueValidation {
    private Long value;
    private LongValueValidationException e;

    public LongValueValidation(String value) {
        try {
            this.value = Long.valueOf(value);
        } catch (NumberFormatException e) {
            this.e = new LongValueValidationException(e);
        }
    }

    public LongValueValidation(Object value) {
        if (Integer.class.isAssignableFrom(value.getClass())) {
            this.value = Long.valueOf((Integer) value).longValue();
        } else if (Long.class.isAssignableFrom(value.getClass())) {
            this.value = (Long) value;
        } else if (String.class.isAssignableFrom(value.getClass())) {
            try {
                this.value = Long.valueOf((String) value);
            } catch (NumberFormatException e) {
                this.e = new LongValueValidationException(e);
            }
        }
    }

    public long get() throws LongValueValidationException {
        if (e != null) {
            throw e;
        }
        return value.longValue();
    }

    public static class LongValueValidationException extends Exception {
        public LongValueValidationException(NumberFormatException e) {
            super(e);
        }
    }
}
