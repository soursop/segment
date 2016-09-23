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

    public String[] getConditionsIdsInRow() {
        return conditionsIdsInRow.toArray(new String[conditionsIdsInRow.size()]);
    }

    public String[] getConditionsIds() {
        return conditionsIds.toArray(new String[conditionsIds.size()]);
    }

    public String[] getConditionIds() {
        return conditionIds.toArray(new String[conditionIds.size()]);
    }

    public boolean isInConditionsIds(String ... matchedConditionsIds) {
        Set<String> ids = new HashSet<>(conditionsIds);
        for(String conditionsId : matchedConditionsIds) {
            if (ids.contains(conditionsId) == false) {
                return false;
            }
            ids.remove(conditionsId);
        }
        if (ids.isEmpty()) {
            return true;
        }
        throw new RuntimeException("not match " + ids);
    }

    public boolean isInConditionIds(String ... matchedConditionIds) {
        Set<String> ids = new HashSet<>(conditionIds);
        for(String conditionId : matchedConditionIds) {
            if (ids.contains(conditionId) == false) {
                return false;
            }
            ids.remove(conditionId);
        }
        if (ids.isEmpty()) {
            return true;
        }
        throw new RuntimeException("not match " + ids);
    }

    public boolean isInConditionsIdsInRow(String ... matchedConditionsIds) {
        Set<String> ids = new HashSet<>(conditionsIdsInRow);
        for(String conditionsId : matchedConditionsIds) {
            if (ids.contains(conditionsId) == false) {
                return false;
            }
            ids.remove(conditionsId);
        }
        if (ids.isEmpty()) {
            return true;
        }
        throw new RuntimeException("not match " + ids);
    }
}
