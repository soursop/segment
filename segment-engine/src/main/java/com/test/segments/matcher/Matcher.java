package com.test.segments.matcher;

import com.test.segments.matcher.duration.Durations;
import com.test.segments.matcher.utils.Assert;

import java.io.Serializable;
import java.text.ParseException;
import java.util.*;


public class Matcher implements Serializable {

    private final Parameter parameter;
    private final Map<String, Condition> conditionMap;
    private final Durations durations;
    private Map<String, Set<String>> conditionMappingMap;

    public Matcher(Parameter parameter) {
        this.parameter = parameter;
        conditionMap = new HashMap<>();
        durations = new Durations();
        conditionMappingMap = new HashMap<>();

        Conditions[] conditionses = parameter.getConditionses();
        for (Conditions conditions : conditionses) {
            for (Condition condition : conditions.getConditions()) {
                conditionMap.put(condition.getId(), condition);
                durations.addCondition(condition);
            }
        }
        durations.resolve(parameter);
    }

    /**
     * return same hash code ids
     * @param id condition id
     * @return
     */
    public Set<String> getEqualsConditionId(String id) {
        if (conditionMappingMap != null) {
            return findMatchedConditionId(id);
        }

        HashMap<Integer, Set<String>> conditionHashCodeMap = new HashMap<>();
        for (Map.Entry<String, Condition> entry : conditionMap.entrySet()) {
            int hashCode = entry.getValue().hashCode();
            String conditionId = entry.getKey();
            if (conditionHashCodeMap.containsKey(hashCode)) {
                conditionHashCodeMap.get(hashCode).add(conditionId);
                continue;
            }
            Set<String> idSet = new HashSet<>();
            idSet.add(conditionId);
            conditionHashCodeMap.put(hashCode, idSet);
        }

        HashMap<String, Set<String>> conditionHashMap = new HashMap<>();
        for (Map.Entry<String, Condition> entry : conditionMap.entrySet()) {
            int hashCode = entry.getValue().hashCode();
            String conditionId = entry.getKey();
            conditionHashMap.put(conditionId, conditionHashCodeMap.get(hashCode));
        }
        conditionMappingMap = conditionHashMap;

        return findMatchedConditionId(id);
    }

    private Set<String> findMatchedConditionId(String id) {
        return Assert.notEmpty("Condition Mapping is empty from matchedConditionId at id", conditionMappingMap.get(id));
    }

    @Deprecated
    private ConditionSet findFrequencyConditionByIds(String[] conditionIds, Map<String, Condition> conditionMapInDataSource) {
        Map<String, Integer> conditionCount = new HashMap<>();
        Set<String> conditionSet = new HashSet<>();
        for (String conditionId : conditionIds) {
            Integer count = conditionCount.get(conditionId);
            if (count == null) {
                if (conditionSet.contains(conditionId)) {
                    continue;
                }
                Condition condition = conditionMapInDataSource.get(conditionId);
                if (condition == null) {
                    if (conditionId.contains(MapConditionIds.separator) == false) {
                        continue;
                    }
                    conditionSet.add(conditionId);
                    continue;
                }
                addAndPutFrequency(conditionCount, conditionSet, conditionId, condition.getFrequency() - 1);
                continue;
            }
            addAndPutFrequency(conditionCount, conditionSet, conditionId, count - 1);
        }
//        System.out.println("conditionCount\t" + conditionCount);
//        System.out.println("conditionSet\t" + conditionSet);
        return new ConditionSet(conditionSet, conditionMapInDataSource);
    }

    private void addAndPutFrequency(Map<String, Integer> conditionCount, Set<String> conditionSet, String conditionId, int count) {
        if (count > 0) {
            conditionCount.put(conditionId, count);
            return;
        }
        conditionSet.add(conditionId);
    }

    /**
     * map 에서 Condition Id List를 받아 올바른 conditions ID List를 반환한다.
     *
     * @param conditionIds
     * @return
     */
    public ConditionsIds findConditionsIds(String[] conditionIds) {
//        Set<String> frequencyConditionByIds = findFrequencyConditionByIds(conditionIds, conditionMapInDataSource);
        FilteredCondition filteredCondition = new FilteredCondition(conditionIds, conditionMap);

        Conditions[] conditionses = parameter.getConditionses();

        ConditionsIds conditionsIds = new ConditionsIds(filteredCondition.getConditionIdsSet());
        for (Conditions conditions : conditionses) {
            // single row에서 부합하는 conditions의 id를 넣는다
            for(Set<Condition> conditionSetByRow : filteredCondition.getConditionsGroupByRow()) {
                if (conditions.matchByConditionIds(conditionSetByRow)) {
                    conditionsIds.addConditionsIdsByRow(conditions.getId());
                }
            }
            // multi row 에서 부합하는 conditions의 id를 넣는다
            if (conditions.matchByConditionIds(filteredCondition.getConditions())) {
                conditionsIds.addConditionsIds(conditions.getId());
            }
        }
        return conditionsIds;
    }

    /**
     * map 에서 log를 받아 매칭된 Condition Id List 를 반환한다
     *
     * @param rows
     * @param date 날짜
     * @return
     */
    public MapConditionIds findConditionIds(String[] rows, int date) throws ParseException {
        MapConditionIds conditionIds = new MapConditionIds();
        Set<Condition> conditionMap = durations.findCondition(date);
        for (Condition condition : conditionMap) {
            if (condition.match(rows) == false) {
                continue;
            }
            if (condition.getFrequency() < 2) {
                conditionIds.addConditionIdByRow(condition.getId());
                continue;
            }
            conditionIds.addConditionId(condition.getId());
        }
        return conditionIds;
    }

}
