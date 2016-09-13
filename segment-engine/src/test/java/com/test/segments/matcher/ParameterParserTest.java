package com.test.segments.matcher;

import com.test.segments.matcher.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class ParameterParserTest {

    @Test
    public void testParseConditions() throws Exception {
        String json = new JSONObject(){{
            put("id", "2");
            put("conjunction", "OR");
            put("conditionables", new JSONArray(){{
                add(new JSONObject(){{
                    put("id", "5");
                    put("fieldMeta", new JSONObject(){{
                        put("key", "prop1");
                        put("clazz", "STRING");
                        put("index", 0);
                    }});
                    put("sign", "CONTAINS");
                    put("value", "");
                    put("durationDays", "30");
                    put("frequency", "1");
                    put("status", "ACTIVE");
                }});
                add(new JSONObject(){{
                    put("id", "6");
                    put("fieldMeta", new JSONObject(){{
                        put("key", "prop2");
                        put("clazz", "STRING");
                        put("index", 1);
                    }});
                    put("sign", "CONTAINS");
                    put("value", "");
                    put("durationDays", "30");
                    put("frequency", "1");
                    put("status", "ACTIVE");
                }});
            }});
        }}.toJSONString();

        Conditions conditions = ParameterParser.parseConditions((JSONObject) JSONValue.parse(json));
        assertThat("", conditions.getId(), is("2"));
        assertThat("", conditions.getConjunction(), is(Conditions.Conjunction.OR));
        assertThat("", conditions.getConditionables().size(), is(2));
    }

    @Test
    public void testParseCondition() throws Exception {
        String json = new JSONObject(){{
            put("id", "1");
            put("fieldMeta", new JSONObject(){{
                put("key", "prop1");
                put("clazz", "STRING");
                put("index", 0);
            }});
            put("sign", "CONTAINS");
            put("value", "");
            put("durationDays", "30");
            put("frequency", "1");
            put("status", "ACTIVE");
        }}.toJSONString();

        Condition condition = ParameterParser.parseCondition((JSONObject) JSONValue.parse(json));
        assertThat("", condition.getId(), is("1"));
    }

    @Test
    public void testParseFieldMeta() throws Exception {
        String json = new JSONObject(){{
            put("key", "prop1");
            put("clazz", "STRING");
            put("index", 0);
        }}.toJSONString();

        FieldMeta fieldMeta = ParameterParser.parseFieldMeta((JSONObject) JSONValue.parse(json));
        assertThat("", fieldMeta.getKey(), is("prop1"));
        assertThat("", fieldMeta.getClazz(), is(FieldMetaType.STRING));
        assertThat("", fieldMeta.getIndex(), is(0));
    }

    @Test(expected = ParameterParsingException.class)
    public void testParseFieldMetaTypeFailure() throws Exception {
        String json = new JSONObject(){{
            put("key", "prop1");
            put("clazz", "!STRING");
            put("index", 0);
        }}.toJSONString();

        FieldMeta fieldMeta = ParameterParser.parseFieldMeta((JSONObject) JSONValue.parse(json));
        assertThat("", fieldMeta.getKey(), is("prop1"));
        assertThat("", fieldMeta.getClazz(), is(FieldMetaType.STRING));
        assertThat("", fieldMeta.getIndex(), is(0));
    }

    @Test(expected = ParameterParsingException.class)
    public void testParseFieldMetaIndexFailure() throws Exception {
        String json = new JSONObject(){{
            put("key", "prop1");
            put("clazz", "STRING");
            put("index", "0");
        }}.toJSONString();

        FieldMeta fieldMeta = ParameterParser.parseFieldMeta((JSONObject) JSONValue.parse(json));
        assertThat("", fieldMeta.getKey(), is("prop1"));
        assertThat("", fieldMeta.getClazz(), is(FieldMetaType.STRING));
        assertThat("", fieldMeta.getIndex(), is(0));
    }

    @Test
    public void testParseConditionable() throws Exception {
        String json = new JSONArray(){{
            add(new JSONObject(){{
                put("id", "5");
                put("fieldMeta", new JSONObject(){{
                    put("key", "prop1");
                    put("clazz", "STRING");
                    put("index", 0);
                }});
                put("sign", "CONTAINS");
                put("value", "");
                put("durationDays", "30");
                put("frequency", "1");
                put("status", "ACTIVE");
            }});
            add(new JSONObject(){{
                put("id", "2");
                put("conjunction", "OR");
                put("conditionables", new JSONArray(){{
                    add(new JSONObject(){{
                        put("id", "5");
                        put("fieldMeta", new JSONObject(){{
                            put("key", "prop1");
                            put("clazz", "STRING");
                            put("index", 0);
                        }});
                        put("sign", "CONTAINS");
                        put("value", "");
                        put("durationDays", "30");
                        put("frequency", "1");
                        put("status", "ACTIVE");
                    }});
                    add(new JSONObject(){{
                        put("id", "6");
                        put("fieldMeta", new JSONObject(){{
                            put("key", "prop2");
                            put("clazz", "STRING");
                            put("index", 1);
                        }});
                        put("sign", "CONTAINS");
                        put("value", "");
                        put("durationDays", "30");
                        put("frequency", "1");
                        put("status", "ACTIVE");
                    }});
                }});
            }});
        }}.toJSONString();

        Conditionable[] conditionables = ParameterParser.parseConditionables((JSONArray) JSONValue.parse(json));

        assertTrue(Condition.class.isAssignableFrom(conditionables[0].getClass()));
        assertTrue(Conditions.class.isAssignableFrom(conditionables[1].getClass()));
    }
}