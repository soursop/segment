package com.test.segments.matcher;

import com.test.segments.matcher.utils.Assert;

import java.io.Serializable;
import java.text.ParseException;
import java.util.*;


public class Matcher implements Serializable {

    private Map<String, Parameter> parameterMap;
    private Map<String, Map<String, Condition>> conditionMap;
    private Map<String, TreeMap<Integer, Set<Condition>>> conditionByDurationMap;
    private Map<String, Set<DurationCondition>> durationConditionMap;
    private Map<String, Map<String, Set<String>>> conditionMappingMap;

    public Matcher(Parameter[] parameters) {
        parameterMap = new HashMap<>();
        conditionMap = new HashMap<>();
        conditionByDurationMap = new HashMap<>();
        durationConditionMap = new HashMap<>();
        conditionMappingMap = new HashMap<>();
        for (Parameter parameter : parameters) {
            parameterMap.put(parameter.getDataSource(), parameter);

            Map<String, Condition> conditionMapInDataSource = new HashMap<>();
            TreeMap<Integer, Set<Condition>> conditionByDurationMapInDataSource = new TreeMap<>();
            Conditions[] conditionses = parameter.getConditionses();
            for (Conditions conditions : conditionses) {
                for (Condition condition : conditions.getConditions()) {
                    conditionMapInDataSource.put(condition.getId(), condition);
                    conditionByDurationMapInDataSource = addConditionByDuration(conditionByDurationMapInDataSource, condition);
                }
            }
            conditionMap.put(parameter.getDataSource(), conditionMapInDataSource);
            Set<DurationCondition> durationConditions = getDurationConditions(parameter, conditionByDurationMapInDataSource);
            conditionByDurationMapInDataSource = getConditionByValidDuration(conditionByDurationMapInDataSource);
//            for(Map.Entry<Integer, Set<Condition>> entry : conditionByDurationMapInDataSource.entrySet()) {
//                for(Condition condition : entry.getValue()) {
//                    System.out.println(String.format("key(%d) duration(%s) %s", entry.getKey(), condition.getDurationDays(), condition.getId()));
//                }
//            }
            durationConditionMap.put(parameter.getDataSource(), durationConditions);
            conditionByDurationMap.put(parameter.getDataSource(), conditionByDurationMapInDataSource);
        }
    }

    /**
     * return same hash code ids
     * @param dataSource
     * @param id condition id
     * @return
     */
    public Set<String> getEqualsConditionId(String dataSource, String id) {
        if (conditionMappingMap.containsKey(dataSource)) {
            return findMatchedConditionId(dataSource, id);
        }

        Map<String, Condition> conditionMapInDataSource = findCorrectConditionMapInDataSource(dataSource);
        HashMap<Integer, Set<String>> conditionHashCodeMap = new HashMap<>();
        for (Map.Entry<String, Condition> entry : conditionMapInDataSource.entrySet()) {
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
        for (Map.Entry<String, Condition> entry : conditionMapInDataSource.entrySet()) {
            int hashCode = entry.getValue().hashCode();
            String conditionId = entry.getKey();
            conditionHashMap.put(conditionId, conditionHashCodeMap.get(hashCode));
        }
        conditionMappingMap.put(dataSource, conditionHashMap);

        return findMatchedConditionId(dataSource, id);
    }

    private Set<String> findMatchedConditionId(String dataSource, String id) {
        Map<String, Set<String>> matchedConditionId = findMatchedConditionId(dataSource);
        return Assert.notEmpty("Condition Mapping is empty from matchedConditionId at id", matchedConditionId.get(id));
    }

    private Map<String, Set<String>> findMatchedConditionId(String dataSource) {
        return Assert.notEmpty("Condition Mapping is empty from conditionMappingMap at DataSource", conditionMappingMap.get(dataSource));
    }

    private Set<DurationCondition> getDurationConditions(Parameter parameter, TreeMap<Integer, Set<Condition>> conditionByDurationMapInDataSource) {
        Set<DurationCondition> durationConditions = new HashSet<>();
        int prevDuration = -1;
        for (int duration : conditionByDurationMapInDataSource.keySet()) {
            if (prevDuration < 0) {
                DurationCondition durationCondition = new DurationCondition(parameter, duration);
//                System.out.println(String.format("prev:%d now:%d %s", prevDuration, duration, durationCondition.toString()));
                durationConditions.add(durationCondition);
                prevDuration = duration;
                continue;
            }
            DurationCondition durationCondition = new DurationCondition(parameter, prevDuration, duration);
//            System.out.println(String.format("prev:%d now:%d %s", prevDuration, duration, durationCondition.toString()));
            durationConditions.add(durationCondition);
            prevDuration = duration;

        }
        return durationConditions;
    }

    private TreeMap<Integer, Set<Condition>> getConditionByValidDuration(TreeMap<Integer, Set<Condition>> conditionByDurationMapInDataSource) {
        TreeMap<Integer, Set<Condition>> conditionByDurationMap = new TreeMap<>();
        for(Map.Entry<Integer, Set<Condition>> entry : conditionByDurationMapInDataSource.entrySet()) {
            conditionByDurationMap.put(entry.getKey(), entry.getValue());
            for(Map.Entry<Integer, Set<Condition>> subEntry : conditionByDurationMapInDataSource.entrySet()) {
                if (entry.getKey() < subEntry.getKey()) {
                    conditionByDurationMap.get(entry.getKey()).addAll(subEntry.getValue());
                }
            }
        }
        return conditionByDurationMap;
    }

    private TreeMap<Integer, Set<Condition>> addConditionByDuration(TreeMap<Integer, Set<Condition>> conditionByDurationMapInDataSource, Condition condition) {
        if (conditionByDurationMapInDataSource.containsKey(condition.getDurationDays()) == false) {
            Set<Condition> conditionsSet = new HashSet<>();
            conditionsSet.add(condition);
            conditionByDurationMapInDataSource.put(condition.getDurationDays(), conditionsSet);
            return conditionByDurationMapInDataSource;
        }
        conditionByDurationMapInDataSource.get(condition.getDurationDays()).add(condition);
        return conditionByDurationMapInDataSource;
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

    private Map<String, Condition> findCorrectConditionMapInDataSource(String dataSource) {
        Map<String, Condition> conditionMapInDataSource = conditionMap.get(dataSource);
        Assert.notEmpty("conditionMap is null in dataSource", conditionMapInDataSource);
        return conditionMapInDataSource;
    }

    private Set<Condition> findCorrectConditionByDurationMapInDataSource(String dataSource, int durationDays) {
        Map<Integer, Set<Condition>> conditionByDurationMapInDataSource = conditionByDurationMap.get(dataSource);
        Assert.notEmpty("conditionByDurationMap is null in dataSource", conditionByDurationMapInDataSource);
        Set<Condition> conditions = conditionByDurationMapInDataSource.get(durationDays);
        Assert.notEmpty("conditionByDurationMapInDataSource is null in darationDays("+durationDays+")", conditions);
        return conditions;
    }


    private Parameter findParameter(String dataSource) {
        return Assert.notEmpty("Parameter is empty at DataSource", parameterMap.get(dataSource));
    }

    /**
     * map 에서 Condition Id List를 받아 올바른 conditions ID List를 반환한다.
     *
     * @param dataSource
     * @param conditionIds
     * @return
     */
    public ConditionsIds findConditionsIds(String dataSource, String[] conditionIds) {
        Map<String, Condition> conditionMapInDataSource = findCorrectConditionMapInDataSource(dataSource);
//        Set<String> frequencyConditionByIds = findFrequencyConditionByIds(conditionIds, conditionMapInDataSource);
        FilteredCondition filteredCondition = new FilteredCondition(conditionIds, conditionMapInDataSource);

        Parameter parameter = findParameter(dataSource);
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
     * @param dataSource
     * @param rows
     * @param date 날짜
     * @return
     */
    public MapConditionIds findConditionIds(String dataSource, String[] rows, int date) throws ParseException {
        MapConditionIds conditionIds = new MapConditionIds();
        int durationDays = getDurationDays(dataSource, date);
        if (durationDays < 0) {
            return conditionIds;
        }
        Set<Condition> conditionMapInDataSource = findCorrectConditionByDurationMapInDataSource(dataSource, durationDays);
        for (Condition condition : conditionMapInDataSource) {
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

    private int getDurationDays(String dataSource, int date) {
        Set<DurationCondition> durationConditions = durationConditionMap.get(dataSource);
        Assert.notEmpty("durationConditionMap is null in dataSource", durationConditionMap);
        for(DurationCondition durationCondition : durationConditions) {
            if (durationCondition.isValidDate(date)) {
                return durationCondition.getDurationDays();
            }
        }
//        System.out.println("date is null in durationConditionMap("+date+")");
        return -1;
    }

}
