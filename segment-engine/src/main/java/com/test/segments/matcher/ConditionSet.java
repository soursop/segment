package com.test.segments.matcher;

import java.util.*;

/**
 * Created by 1002707 on 2016. 7. 26..
 */
@Deprecated
public class ConditionSet {

    private Set<Condition> conditions = new HashSet<>();
    private List<Set<Condition>> conditionsGroupByRow = new LinkedList<>();
    private Set<String> conditionIdsSet = new HashSet<>();

    public ConditionSet(Iterable<String> conditionIds, Map<String, Condition> conditionMapInDataSource) {
        for (String conditionId : conditionIds) {
            if (conditionId.contains(MapConditionIds.separator) == false) {
                Condition condition = conditionMapInDataSource.get(conditionId);
                if (condition == null) {
                    continue;
                }
                conditionIdsSet.add(conditionId);
                conditions.add(conditionMapInDataSource.get(conditionId));
                continue;
            }
            Set<Condition> conditionSet = getConditionSet(conditionId, conditionMapInDataSource);
            if (conditionSet.isEmpty()) {
                continue;
            }
            conditionsGroupByRow.add(conditionSet);
        }
    }

    private Set<Condition> getConditionSet(String conditionId, Map<String, Condition> conditionMapInDataSource) {
        Set<Condition> conditionSet = new HashSet<>();
        StringTokenizer stringTokenizer = new StringTokenizer(conditionId, MapConditionIds.separator);
        while (stringTokenizer.hasMoreTokens()) {
            String splitConditionId = stringTokenizer.nextToken();
            if (splitConditionId.isEmpty()) {
                continue;
            }
            Condition condition = conditionMapInDataSource.get(splitConditionId);
            if (condition == null || conditionSet.contains(condition)) {
                continue;
            }
            conditionSet.add(condition);
            conditionIdsSet.add(splitConditionId);
            conditions.add(condition);
        }
        return conditionSet;
    }

    public Set<Condition> getConditions() {
        return conditions;
    }

    public List<Set<Condition>> getConditionsGroupByRow() {
        return conditionsGroupByRow;
    }

    public Set<String> getConditionIdsSet() {
        return conditionIdsSet;
    }
}
