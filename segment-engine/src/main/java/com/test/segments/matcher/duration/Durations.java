package com.test.segments.matcher.duration;


import com.test.segments.matcher.Condition;
import com.test.segments.matcher.Parameter;
import com.test.segments.matcher.utils.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Durations {
    private static final Logger LOGGER = LoggerFactory.getLogger(Durations.class);

    private TreeMap<Integer, Set<Condition>> conditionsGroupByDuration = new TreeMap<>();
    private final TreeMap<Integer, Set<Condition>> conditionsGroupByValidDuration = new TreeMap<>();
    private DurationDays[] durationDayses;

    public void addCondition(Condition condition) {
        if (conditionsGroupByDuration.containsKey(condition.getDurationDays()) == false) {
            Set<Condition> conditionsSet = new HashSet<>();
            conditionsSet.add(condition);
            conditionsGroupByDuration.put(condition.getDurationDays(), conditionsSet);
        }
        conditionsGroupByDuration.get(condition.getDurationDays()).add(condition);
    }

    private Set<DurationDays> getDurationConditions(Parameter parameter) {
        Set<DurationDays> durationDayses = new HashSet<>();
        int prevDuration = -1;
        for (int duration : conditionsGroupByDuration.keySet()) {
            if (prevDuration < 0) {
                DurationDays durationDays = new DurationDays(parameter, duration);
//                LOGGER.debug(String.format("prev:%d now:%d %s", prevDuration, duration, durationCondition.toString()));
                durationDayses.add(durationDays);
                prevDuration = duration;
                continue;
            }
            DurationDays durationDays = new DurationDays(parameter, prevDuration, duration);
//            LOGGER.debug(String.format("prev:%d now:%d %s", prevDuration, duration, durationCondition.toString()));
            durationDayses.add(durationDays);
            prevDuration = duration;
        }
        return durationDayses;
    }

    private void setConditionByValidDuration() {
        for(Map.Entry<Integer, Set<Condition>> entry : conditionsGroupByDuration.entrySet()) {
            conditionsGroupByValidDuration.put(entry.getKey(), entry.getValue());
            for(Map.Entry<Integer, Set<Condition>> subEntry : conditionsGroupByDuration.entrySet()) {
                if (entry.getKey() < subEntry.getKey()) {
                    conditionsGroupByValidDuration.get(entry.getKey()).addAll(subEntry.getValue());
                }
            }
        }
    }

    public void resolve(Parameter parameter) {
        Set<DurationDays> durationConditions = getDurationConditions(parameter);
        durationDayses = durationConditions.toArray(new DurationDays[durationConditions.size()]);
        setConditionByValidDuration();
        if (LOGGER.isDebugEnabled()) {
            for (Map.Entry<Integer, Set<Condition>> entry : conditionsGroupByValidDuration.entrySet()) {
                for (Condition condition : entry.getValue()) {
                    LOGGER.debug(String.format("key(%d) duration(%s) %s", entry.getKey(), condition.getDurationDays(), condition.getId()));
                }
            }
        }
        // 한번 resolve된 결과물은 다시 사용하지 않는다
        conditionsGroupByDuration = null;
    }

    public Set<Condition> findCondition(int date) {
        int durationDays = getDurationDays(date);
        if (durationDays < 0) {
            return Collections.EMPTY_SET;
        }
        return findConditionGroupByValidDuration(durationDays);
    }

    private Set<Condition> findConditionGroupByValidDuration(int durationDays) {
        Set<Condition> conditions = conditionsGroupByValidDuration.get(durationDays);
        Assert.notEmpty("conditionsGroupByValidDuration is null in darationDays(" + durationDays + ")", conditions);
        return conditions;
    }

    private int getDurationDays(int date) {
        for(DurationDays durationDays : durationDayses) {
            if (durationDays.isValidDate(date)) {
                return durationDays.getDurationDays();
            }
        }
//        LOGGER.debug("date is null in durationConditionMap("+date+")");
        return -1;
    }

}
