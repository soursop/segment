package com.test.segments.matcher;

import java.util.*;

public class FilteredCondition {

    private Set<Condition> conditions = new HashSet<>();
    private List<Set<Condition>> conditionsGroupByRow = new LinkedList<>();
    private Set<String> conditionIdsSet = new HashSet<>();

    public FilteredCondition(String[] conditionIds, Map<String, Condition> conditionMapInDataSource) {
        Map<String, Integer> conditionCount = new HashMap<>();
        for (String conditionId : conditionIds) {
            if (conditionId.contains(MapConditionIds.separator)) {
                addConditionSet(conditionId, conditionMapInDataSource);
                continue;
            }
            if (conditionIdsSet.contains(conditionId)) {
                continue;
            }
            if (conditionCount.containsKey(conditionId) == false) {
                putNotExistConditionSet(conditionCount, conditionId, conditionMapInDataSource);
                continue;
            }
            putExistConditionSet(conditionCount, conditionId, conditionMapInDataSource);
        }
    }

    private void putNotExistConditionSet(Map<String, Integer> conditionCount, String conditionId, Map<String, Condition> conditionMapInDataSource) {
        Condition condition = conditionMapInDataSource.get(conditionId);
        if (condition == null) {
            return;
        }
        int count = condition.getFrequency() - 1;
        if (count > 0) {
            conditionCount.put(conditionId, count);
            return;
        }
        conditionIdsSet.add(conditionId);
        conditions.add(condition);
    }

    private void putExistConditionSet(Map<String, Integer> conditionCount, String conditionId, Map<String, Condition> conditionMapInDataSource) {
        int count = conditionCount.get(conditionId) - 1;
        if (count > 0) {
            conditionCount.put(conditionId, count);
            return;
        }
        Condition condition = conditionMapInDataSource.get(conditionId);
        conditionIdsSet.add(conditionId);
        conditions.add(condition);
    }

    private void addConditionSet(String conditionId, Map<String, Condition> conditionMap) {
        Set<Condition> conditionSet = new HashSet<>();
        StringTokenizer stringTokenizer = new StringTokenizer(conditionId, MapConditionIds.separator);
        while (stringTokenizer.hasMoreTokens()) {
            String splitConditionId = stringTokenizer.nextToken();
            if (splitConditionId.isEmpty()) {
                continue;
            }
            Condition condition = conditionMap.get(splitConditionId);
            if (condition == null) {
                continue;
            }
            conditionSet.add(condition);
            if (conditionIdsSet.contains(splitConditionId)) {
                continue;
            }
            conditionIdsSet.add(splitConditionId);
            conditions.add(condition);
        }
        if (conditionSet.isEmpty()) {
            return;
        }
        conditionsGroupByRow.add(conditionSet);
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
