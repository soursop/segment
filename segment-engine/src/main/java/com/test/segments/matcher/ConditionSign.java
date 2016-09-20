package com.test.segments.matcher;

import java.util.*;
import static com.test.segments.matcher.Evaluation.*;
import static com.test.segments.matcher.FieldMetaType.*;

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

    ConditionSign(FieldMetaType[] enableTypes, Evaluation evaluation) {
        this.enableTypes = Arrays.asList(enableTypes);
        this.evaluation = evaluation;
    }

    public boolean evaluate(String source, Object[] targets) {
        if (source == null || targets == null) {
            return false;
        }
        try {
//            String resolvedSource = getResolvedSource(source);
            for(int i=0; i< targets.length; i++) {
                if (evaluation.evaluate(source, targets[i])) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
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

