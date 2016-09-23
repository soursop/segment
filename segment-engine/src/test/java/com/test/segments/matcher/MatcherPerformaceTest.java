package com.test.segments.matcher;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


public class MatcherPerformaceTest {
    static final String DATA_SOURCE = "";

    private Parameter getRepeatFRequencyParameter() {
        List<FieldMeta> fieldMetas = Arrays.asList(
                FieldMeta.of("act", FieldMetaType.STRING, "요청", 0)
                , FieldMeta.of("prdname", FieldMetaType.STRING, "상품이름", 1)
        );
        Condition request1 = Condition.of("act=request(1)10", FieldMeta.findByKey(fieldMetas, "act"), ConditionSign.EQUALS, "request", 10, 1, Condition.Status.ACTIVE);
        Condition request2 = Condition.of("prdname=나이키(2)", FieldMeta.findByKey(fieldMetas, "prdname"), ConditionSign.CONTAINS, "나이키", 20, 2, Condition.Status.ACTIVE);
        Condition request3 = Condition.of("act=request(2)", FieldMeta.findByKey(fieldMetas, "act"), ConditionSign.EQUALS, "request", 20, 2, Condition.Status.ACTIVE);
        Condition request4 = Condition.of("act=click(1)20", FieldMeta.findByKey(fieldMetas, "act"), ConditionSign.CONTAINS, "click", 20, 1, Condition.Status.ACTIVE);
        Condition request5 = Condition.of("prdname=리복(1)", FieldMeta.findByKey(fieldMetas, "prdname"), ConditionSign.CONTAINS, "리복", 20, 1, Condition.Status.ACTIVE);
        List<Conditions> list = new ArrayList<Conditions>();
        list.add(Conditions.of("1", Conditions.Conjunction.AND,
                request1
                , request2
        ));
        list.add(Conditions.of("2", Conditions.Conjunction.OR,
                request3
        ));
        list.add(Conditions.of("3", Conditions.Conjunction.OR,
                request4
                , request5
        ));
        list.add(Conditions.of("4", Conditions.Conjunction.AND,
                request4
        ));
        list.add(Conditions.of("5", Conditions.Conjunction.AND,
                request3
                , request5
        ));
        Conditions[] conditionsesSmall = list.toArray(new Conditions[list.size()]);
        return Parameter.of(DATA_SOURCE, conditionsesSmall, "20160407");
    }

    @Test
    public void testProfileConditions() throws Exception {
        Matcher matcher = new Matcher(getRepeatFRequencyParameter());
        for(int j=1; j<(300*10+1); j++) {
            List<String> conditionIds = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                testMatchTime(matcher, conditionIds, new String[]{"REQUEST", "나이키 신발" + i}, 20160406);
                testMatchTime(matcher, conditionIds, new String[]{"CLICK", "신발은 나이키" + i}, 20160406);
                testMatchTime(matcher, conditionIds, new String[]{"REQUEST", "신발은 크럭스" + i}, 20160406);
                testMatchTime(matcher, conditionIds, new String[]{"CLICK", "리복 맞을까" + i}, 20160406);
            }
            long start = System.currentTimeMillis();
//        System.out.println(String.format("conditionIds %s", conditionIds));
            ConditionsIds conditionsIds = matcher.findConditionsIds(conditionIds.toArray(new String[conditionIds.size()]));
            for(String id : conditionsIds.getConditionIds()) {
            }
            for(String id : conditionsIds.getConditionsIds()) {
            }
            for(String id : conditionsIds.getConditionsIdsInRow()) {
            }
//            assertThat("", conditionsIds.getConditionsIdsInRow(), is(containsInAnyOrder("3", "4")));
//            assertThat("", conditionsIds.getConditionsIds(), is(containsInAnyOrder("1", "2", "3", "4", "5")));
            if (j % 10 == 0) {
                long stop = System.currentTimeMillis();
                long diff = stop - start;
                start = stop;
                System.out.println(String.format("time(%d) :%d", j, diff));
            }
        }
//        System.out.println(conditionsIds.getConditionIds());
    }

    private void testMatchTime(Matcher matcher, List<String> conditionIds, String[] rows, int date) throws Exception {
        MapConditionIds rowConditionIds = matcher.findConditionIds(SourceHandler.STRING_SOURCE_HANDLER, rows, date);
        conditionIds.addAll(rowConditionIds.getConditionIds());
//        System.out.println(String.format("rowConditionIds %s", rowConditionIds));
    }

}