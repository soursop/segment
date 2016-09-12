package com.test.segments.matcher;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 1002707 on 2016. 7. 26..
 */
public class ConditionsIds {
    private Set<String> conditionsIdsInRow = new HashSet<String>();
    private Set<String> conditionsIds = new HashSet<String>();
    private final Set<String> conditionIds;

    public ConditionsIds(Set<String> conditionIds) {
        this.conditionIds = conditionIds;
    }

    public void addConditionsIdsByRow(String id) {
        conditionsIdsInRow.add(id);
    }
    public void addConditionsIds(String id) {
        conditionsIds.add(id);
    }

    public Set<String> getConditionsIdsInRow() {
        return conditionsIdsInRow;
    }

    public Set<String> getConditionsIds() {
        return conditionsIds;
    }

    public Set<String> getConditionIds() {
        return conditionIds;
    }
}
