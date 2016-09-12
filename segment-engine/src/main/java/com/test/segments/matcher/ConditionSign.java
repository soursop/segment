package com.test.segments.matcher;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by sonegy@sk.com on 2016. 2. 24..
 */
public enum ConditionSign {
    CONTAINS(STRING_TYPE, CONTAINS_EVAL),
    NOT_CONTAINS(STRING_TYPE, NOT_CONTAINS_EVAL),
    START_WITH(STRING_TYPE, START_WITH_EVAL),
    NOT_START_WITH(STRING_TYPE, NOT_START_WITH_EVAL),
    END_WITH(STRING_TYPE, END_WITH_EVAL),
    NOT_END_WITH(STRING_TYPE, NOT_END_WITH_EVAL),
    EQUALS(STRING_INTEGER_TYPE, EQUALS_EVAL),
    NOT_EQUALS(STRING_INTEGER_TYPE, NOT_EQUALS_EVAL),
    LESS_THAN(INTEGER_TYPE, LESS_THAN_EVAL),
    LESS_THAN_OR_EQUALS_TO(INTEGER_TYPE, LESS_THAN_OR_EQUALS_TO_EVAL),
    GREATER_THAN(INTEGER_TYPE, GREATER_THAN_EVAL),
    GREATER_THAN_OR_EQUALS_TO(INTEGER_TYPE, GREATER_THAN_OR_EQUALS_TO_EVAL);

    private final List<FieldMetaType> enableTypes;
    private final Evaluation evaluation;
    private List<SignHandler<String>> sourceHandler = new ArrayList<SignHandler<String>>();
    private List<SignHandler> targetHandler = new ArrayList<SignHandler>();

    ConditionSign(FieldMetaType[] enableTypes, Evaluation evaluation) {
        this.sourceHandler.add(new StringHandler());
        this.targetHandler.add(new ObjectTargetHandler());
        this.enableTypes = Arrays.asList(enableTypes);
        this.evaluation = evaluation;
    }

    public boolean evaluate(String source, Object target) {
        if (source == null || target == null) {
            return false;
        }
        try {
            Object[] targets = getTargets(target);
            String resolvedSource = getResolvedSource(source);
            for(int i=0; i< targets.length; i++) {
                if (evaluation.evaluate(resolvedSource, targets[i])) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private Object[] getTargets(Object target) {
        Object resolvedTarget = getResolvedTarget(target);
        if (target instanceof String) {
            return String.valueOf(resolvedTarget).split("\\|");
        }
        return new Object[]{resolvedTarget};
    }

    private Object getResolvedTarget(Object target) {
        for(SignHandler<Object> handler : targetHandler) {
            target = handler.resolve(target);
        }
        return target;
    }

    private String getResolvedSource(String source) {
        for(SignHandler<String> handler : sourceHandler) {
            source = handler.resolve(source);
        }
        return source;
    }

    public static List<ConditionSign> getSigns(final FieldMetaType fieldMetaType) {
        List<ConditionSign> signs = new ArrayList<>();
        for (ConditionSign sign : values()) {
            if (sign.enableTypes.contains(fieldMetaType)) {
                signs.add(sign);
            }
        }
        return signs;
    }

}

interface SignHandler<T> {
    T resolve(T target);
}

abstract class EscapeHandler<T> implements SignHandler<T> {
    protected Map<String, String> escapeSigns = new HashMap<String, String>();
    public EscapeHandler() {
        escapeSigns.put("||", "\u20ac");
    }
}

class StringHandler extends EscapeHandler<String> {
    @Override
    public String resolve(String target) {
        String lowered = target.toString().toLowerCase();
        for (Map.Entry<String, String> entry : escapeSigns.entrySet()) {
            if (lowered.contains(entry.getKey())) {
                lowered = lowered.replaceAll(Pattern.quote(entry.getKey()), entry.getValue());
            }
        }
        return lowered.trim();
    }
}

class ObjectTargetHandler extends EscapeHandler<Object> {
    @Override
    public Object resolve(Object target) {
        if (target instanceof String) {
            String lowered = target.toString().toLowerCase();
            for (Map.Entry<String, String> entry : escapeSigns.entrySet()) {
                if (lowered.contains(entry.getKey())) {
                    lowered = lowered.replaceAll(Pattern.quote(entry.getKey()), entry.getValue());
                }
            }
            return lowered;
        }
        return target;
    }
}
