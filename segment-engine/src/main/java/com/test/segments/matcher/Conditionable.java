package com.test.segments.matcher;

import java.io.Serializable;
import java.util.Set;

public interface Conditionable extends Serializable {

    boolean matchByConditionIds(Set<Condition> conditionIds);

    String toConditionString();
}
