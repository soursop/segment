package com.test.segments.matcher;

import org.junit.Test;

import java.util.*;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;


public class MatcherTest {
    static final String DATA_SOURCE = "";
    static final List<FieldMeta> ALL_FILED_META_LIST = Arrays.asList(
            FieldMeta.of("prop1", FieldMetaType.STRING, "설명1", 0),
            FieldMeta.of("prop2", FieldMetaType.STRING, "설명2", 1),
            FieldMeta.of("prop3", FieldMetaType.STRING, "설명3", 2),
            FieldMeta.of("prop4", FieldMetaType.STRING, "설명4", 3),
            FieldMeta.of("prop5", FieldMetaType.STRING, "설명5", 4),
            FieldMeta.of("prop6", FieldMetaType.STRING, "설명6", 5),
            FieldMeta.of("prop7", FieldMetaType.STRING, "설명7", 6),
            FieldMeta.of("prop8", FieldMetaType.INTEGER, "설명8", 7)
    );
    static final Condition prop1Text1Condition = Condition.of("1", FieldMeta.findByKey(ALL_FILED_META_LIST, "prop1"), ConditionSign.CONTAINS, "text1", 32, 1, Condition.Status.ACTIVE);
    static final Condition prop2Text2Condition = Condition.of("2", FieldMeta.findByKey(ALL_FILED_META_LIST, "prop2"), ConditionSign.CONTAINS, "text2", 10, 1, Condition.Status.ACTIVE);
    static final Condition prop2Text1Condition = Condition.of("3", FieldMeta.findByKey(ALL_FILED_META_LIST, "prop2"), ConditionSign.CONTAINS, "text1", 30, 1, Condition.Status.ACTIVE);
    static final Condition prop3Text3Condition = Condition.of("4", FieldMeta.findByKey(ALL_FILED_META_LIST, "prop3"), ConditionSign.CONTAINS, "text3", 10, 1, Condition.Status.ACTIVE);
    static final Condition prop3Text1Condition = Condition.of("5", FieldMeta.findByKey(ALL_FILED_META_LIST, "prop3"), ConditionSign.CONTAINS, "text1", 10, 1, Condition.Status.ACTIVE);

    @Test
    public void testGetNotConditionsIds() throws Exception {
        Conditions conditions1 = Conditions.of("6", Conditions.Conjunction.NOT,
                prop1Text1Condition,
                Conditions.of("7", Conditions.Conjunction.NOT,
                        prop2Text2Condition
                )
        );
        Conditions conditions2 = Conditions.of("8", Conditions.Conjunction.NOT,
                prop3Text3Condition
                , prop2Text1Condition
        );
        Conditions[] conditionses = {
                conditions1,
                conditions2
        };
        Matcher matcher = new Matcher(Parameter.of(DATA_SOURCE, conditionses));

        assertThat("", matcher.findConditionsIds(new String[]{prop3Text3Condition.getId(), prop2Text2Condition.getId()}).isInConditionsIds(conditions1.getId()),
                is(true));
        assertThat("", matcher.findConditionsIds(new String[]{prop3Text3Condition.getId(), prop2Text1Condition.getId()}).isInConditionsIds(conditions1.getId()),
                is(false));
        assertThat("", matcher.findConditionsIds(new String[]{prop2Text2Condition.getId(), prop3Text1Condition.getId()}).isInConditionsIds(conditions1.getId(), conditions2.getId()),
                is(true));
    }

    @Test
    public void testNullString() throws Exception {
        Conditions[] conditionses = {
                Conditions.of("6", Conditions.Conjunction.AND,
                        prop1Text1Condition)
        };
        Matcher matcher = new Matcher(Parameter.of(DATA_SOURCE, conditionses, "20160902"));
        matcher.findConditionIds(SourceHandler.STRING_SOURCE_HANDLER, new String[]{null}, 20160901);
    }

    @Test
    public void testGetConditionsIds() throws Exception {
        Conditions conditions1 = Conditions.of("1", Conditions.Conjunction.AND,
                prop1Text1Condition,
                prop2Text2Condition,
                prop3Text3Condition
        );
        Conditions conditions2 = Conditions.of("2", Conditions.Conjunction.OR,
                prop1Text1Condition,
                prop2Text1Condition,
                prop3Text1Condition
        );
        Conditions[] conditionses = {
                conditions1,
                conditions2
        };
        Matcher matcher = new Matcher(Parameter.of(DATA_SOURCE, conditionses));

        assertThat("", matcher.findConditionsIds(new String[]{prop1Text1Condition.getId(), prop2Text2Condition.getId()}).isInConditionsIds(conditions2.getId()),
                is(true));
        assertThat("", matcher.findConditionsIds(new String[]{prop1Text1Condition.getId(), prop2Text2Condition.getId(), prop3Text3Condition.getId()}).isInConditionsIds(conditions1.getId(), conditions2.getId()),
                is(true));
        assertThat("", matcher.findConditionsIds(new String[]{prop3Text3Condition.getId()}).isInConditionsIds(), is(true));
    }

    @Test
    public void testFindConditionIds() throws Exception {
        Conditions conditions1 = Conditions.of(Conditions.Conjunction.AND,
                prop1Text1Condition,
                prop2Text2Condition,
                prop3Text3Condition
        );
        Conditions conditions2 = Conditions.of(Conditions.Conjunction.OR,
                prop1Text1Condition,
                prop2Text1Condition,
                prop3Text1Condition
        );
        Conditions[] conditionses = {
                conditions1,
                conditions2
        };
        Matcher matcher = new Matcher(Parameter.of("", conditionses, "20160328"));

        List<String> conditionIds1 = matcher.findConditionIds(SourceHandler.STRING_SOURCE_HANDLER, new String[]{"text1", "text2", "text3"}, 20160320).getConditionIds();
        ConditionsIds conditionsIds1 = matcher.findConditionsIds(conditionIds1.toArray(new String[conditionIds1.size()]));
        List<String> conditionIds2 = matcher.findConditionIds(SourceHandler.STRING_SOURCE_HANDLER, new String[]{"text1", "text1", "text1"}, 20160321).getConditionIds();
        ConditionsIds conditionsIds2 = matcher.findConditionsIds(conditionIds2.toArray(new String[conditionIds2.size()]));
        assertThat("", conditionsIds1.isInConditionIds(prop2Text2Condition.getId(), prop1Text1Condition.getId(), prop3Text3Condition.getId()), is(true));
        assertThat("", conditionsIds2.isInConditionIds(prop3Text1Condition.getId(), prop2Text1Condition.getId(), prop1Text1Condition.getId()), is(true));
        assertThat("", matcher.findConditionIds(SourceHandler.STRING_SOURCE_HANDLER, new String[]{"text9", "text9", "text9"}, 20160411).getConditionIds(), is(empty()));
    }

    @Test
    public void testDurationConditions() throws Exception {
        Conditions conditions1 = Conditions.of(Conditions.Conjunction.AND,
                prop1Text1Condition,
                prop2Text2Condition,
                prop3Text3Condition
        );
        Conditions conditions2 = Conditions.of(Conditions.Conjunction.OR,
                prop1Text1Condition,
                prop2Text1Condition,
                prop3Text1Condition
        );
        Conditions[] conditionses = {
                conditions1,
                conditions2
        };
        Parameter parameter = Parameter.of(DATA_SOURCE, conditionses, "20160328");
        Matcher matcher = new Matcher(parameter);
        List<String> conditionIds1 = matcher.findConditionIds(SourceHandler.STRING_SOURCE_HANDLER, new String[]{"text1", "text2", "TEXT3"}, 20160319).getConditionIds();
        ConditionsIds conditionsIds1 = matcher.findConditionsIds(conditionIds1.toArray(new String[conditionIds1.size()]));
        List<String> conditionIds2 = matcher.findConditionIds(SourceHandler.STRING_SOURCE_HANDLER, new String[]{"text1", "text1", "text3"}, 20160228).getConditionIds();
        ConditionsIds conditionsIds2 = matcher.findConditionsIds(conditionIds2.toArray(new String[conditionIds1.size()]));
        assertThat("", conditionsIds1.isInConditionIds(prop3Text3Condition.getId(), prop1Text1Condition.getId(), prop2Text2Condition.getId()), is(true));
        assertThat("", conditionsIds2.isInConditionIds(prop1Text1Condition.getId(), prop2Text1Condition.getId()), is(true));
        assertThat("", matcher.findConditionIds(SourceHandler.STRING_SOURCE_HANDLER, new String[]{"text1", "text2", "text3"}, 20160226).getConditionIds(), is(containsInAnyOrder(prop1Text1Condition.getId() + ",")));

    }

    @Test
    public void testDuplicateConditions() throws Exception {
        List<FieldMeta> fieldMetas = Arrays.asList(
                FieldMeta.of("act", FieldMetaType.STRING, "요청", 0)
        );
        Condition request1 = Condition.of("2", FieldMeta.findByKey(fieldMetas, "act"), ConditionSign.EQUALS, "request", 10, 1, Condition.Status.ACTIVE);
        Condition request2 = Condition.of("3", FieldMeta.findByKey(fieldMetas, "act"), ConditionSign.EQUALS, "request", 20, 1, Condition.Status.ACTIVE);
        Condition request3 = Condition.of("4", FieldMeta.findByKey(fieldMetas, "act"), ConditionSign.EQUALS, "request", 20, 1, Condition.Status.ACTIVE);
        Conditions[] conditionses = {
                Conditions.of(Conditions.Conjunction.AND,
                        request1
                ),
                Conditions.of(Conditions.Conjunction.OR,
                        request2
                ),
                Conditions.of(Conditions.Conjunction.OR,
                        request3
                )
        };
        Parameter parameter = Parameter.of(DATA_SOURCE, conditionses, "20160407");
        Matcher matcher = new Matcher(parameter);
        List<String> conditionIds = matcher.findConditionIds(SourceHandler.STRING_SOURCE_HANDLER, new String[]{"REQUEST"}, 20160406).getConditionIds();
        ConditionsIds conditionsIds = matcher.findConditionsIds(conditionIds.toArray(new String[conditionIds.size()]));
        assertThat("", conditionsIds.isInConditionIds("2", "3"), is(true));
        assertThat("", matcher.getEqualsConditionId("3"), is(containsInAnyOrder("3", "4")));
        assertThat("", matcher.getEqualsConditionId("4"), is(containsInAnyOrder("3", "4")));
    }

    @Test
    public void testFrequencyConditions() throws Exception {
        List<FieldMeta> fieldMetas = Arrays.asList(
                FieldMeta.of("act", FieldMetaType.STRING, "요청", 0)
                , FieldMeta.of("prdname", FieldMetaType.STRING, "상품이름", 1)
        );
        Condition request1 = Condition.of("act=request(1)", FieldMeta.findByKey(fieldMetas, "act"), ConditionSign.EQUALS, "request", 10, 1, Condition.Status.ACTIVE);
        Condition request2 = Condition.of("prdname=나이키(2)", FieldMeta.findByKey(fieldMetas, "prdname"), ConditionSign.CONTAINS, "나이키", 20, 2, Condition.Status.ACTIVE);
        Condition request3 = Condition.of("act=request(2)", FieldMeta.findByKey(fieldMetas, "act"), ConditionSign.EQUALS, "request", 20, 2, Condition.Status.ACTIVE);
        Conditions[] conditionses = {
                Conditions.of("1", Conditions.Conjunction.AND,
                        request1
                        , request2
                ),
                Conditions.of("2", Conditions.Conjunction.OR,
                        request3
                )
        };
        Parameter parameter = Parameter.of(DATA_SOURCE, conditionses, "20160407");
        Matcher matcher = new Matcher(parameter);
        MapConditionIds conditionIds = matcher.findConditionIds(SourceHandler.STRING_SOURCE_HANDLER, new String[]{"REQUEST", "나이키 신발"}, 20160406);
        conditionIds.addAll(matcher.findConditionIds(SourceHandler.STRING_SOURCE_HANDLER, new String[]{"CLICK", "신발은 나이키"}, 20160406));
        conditionIds.addAll(matcher.findConditionIds(SourceHandler.STRING_SOURCE_HANDLER, new String[]{"CLICK", "나이키 맞을까"}, 20160406));
        assertThat("", conditionIds.getConditionIds(), is(containsInAnyOrder("act=request(1),", "prdname=나이키(2)", "prdname=나이키(2)", "prdname=나이키(2)", "act=request(2)")));
    }

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
    public void testRepeatFrequencyConditions() throws Exception {
        Matcher matcher = new Matcher(getRepeatFRequencyParameter());
        List<String> conditionIds = new ArrayList<>();
        testMatchTime(matcher, conditionIds, new String[]{"REQUEST", "나이키 신발"}, 20160406);
        testMatchTime(matcher, conditionIds, new String[]{"CLICK", "신발은 나이키"}, 20160406);
        testMatchTime(matcher, conditionIds, new String[]{"REQUEST", "신발은 크럭스"}, 20160406);
        testMatchTime(matcher, conditionIds, new String[]{"CLICK", "리복 맞을까"}, 20160406);
//        System.out.println(String.format("conditionIds %s", conditionIds));
        ConditionsIds conditionsIds = matcher.findConditionsIds(conditionIds.toArray(new String[conditionIds.size()]));
        assertThat("", conditionsIds.isInConditionsIdsInRow("3", "4"), is(true));
        assertThat("", conditionsIds.isInConditionsIds("1", "2", "3", "4", "5"), is(true));
//        System.out.println(conditionsIds.getConditionIds());
    }

    private void testMatchTime(Matcher matcher, List<String> conditionIds, String[] rows, int date) throws Exception {
        MapConditionIds rowConditionIds = matcher.findConditionIds(SourceHandler.STRING_SOURCE_HANDLER, rows, date);
        conditionIds.addAll(rowConditionIds.getConditionIds());
//        System.out.println(String.format("rowConditionIds %s", rowConditionIds));
    }

}