package com.test.segments.matcher;

import java.util.*;
import java.util.regex.Pattern;


public interface DataHandler<T> {
    T resolve(T target);

    DataHandler<String> SOURCE_HANDLER = new EscapeHandler<String>() {
        @Override
        public String resolve(String target) {
            if (target == null) {
                return target;
            }
            String lowered = target.toLowerCase();
            for (Map.Entry<String, String> entry : escapeSigns.entrySet()) {
                if (lowered.contains(entry.getKey())) {
                    lowered = lowered.replaceAll(Pattern.quote(entry.getKey()), entry.getValue());
                }
            }
            return lowered.trim();
        }
    };

    DataHandler<String> TARGET_HANDLER = new EscapeHandler<String>() {
        @Override
        public String resolve(String target) {
            String lowered = target.toLowerCase();
            for (Map.Entry<String, String> entry : escapeSigns.entrySet()) {
                if (lowered.contains(entry.getKey())) {
                    lowered = lowered.replaceAll(Pattern.quote(entry.getKey()), entry.getValue());
                }
            }
            return lowered;
        }
    };

    abstract class EscapeHandler<T> implements DataHandler<T> {
        protected Map<String, String> escapeSigns = new HashMap<String, String>();
        public EscapeHandler() {
            escapeSigns.put("||", "\u20ac");
        }
    }
}



