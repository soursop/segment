package com.test.segments.matcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1002707 on 2016. 7. 27..
 */
public class MapConditionIds {
    private boolean isAdd = false;
    private StringBuffer conditionIdsInRow = new StringBuffer();
    private List<String> conditionIds = new ArrayList<String>();
    public static String separator = ",";
    public void addConditionIdByRow(String id) {
        if (isAdd) {
            throw new RuntimeException(String.format("%s : conditionIdsInRow field is already added to conditionIds", getClass().getName()));
        }
        conditionIdsInRow.append(id);
        conditionIdsInRow.append(",");
    }
    public void addConditionId(String id) {
        conditionIds.add(id);
    }

    public List<String> getConditionIds() {
        if (isAdd == false) {
            isAdd = true;
            String conditionIdsInRowStr = conditionIdsInRow.toString();
            if (conditionIdsInRowStr.isEmpty()) {
                return conditionIds;
            }
            conditionIds.add(conditionIdsInRowStr);
            return conditionIds;
        }
        return conditionIds;
    }

    public void addAll(MapConditionIds conditionIds) {
        this.conditionIds.addAll(conditionIds.getConditionIds());
    }

    @Override
    public String toString() {
        return "MapConditionIds{" +
                "isAdd=" + isAdd +
                ", conditionIdsInRow=" + conditionIdsInRow +
                ", conditionIds=" + conditionIds +
                '}';
    }
}
