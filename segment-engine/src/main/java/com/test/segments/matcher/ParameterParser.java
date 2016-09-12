package com.test.segments.matcher;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ParameterParser {

    private static final String DATA_SOURCE = "dataSource";
    private static final String CONDITIONSES = "conditionses";
    private static final String CONDITIONS_ID = "id";
    private static final String CONDITIONS_CONJUNCTION = "conjunction";
    private static final String CONDITIONS_CONDITIONABLES = "conditionables";
    private static final String CONDITION_FIELD_META = "fieldMeta";
    private static final String CONDITION_FIELD_META_KEY = "key";
    private static final String CONDITION_FIELD_META_CLAZZ = "clazz";
    private static final String CONDITION_FIELD_META_INDEX = "index";
    private static final String CONDITION_ID = "id";
    private static final String CONDITION_SIGN = "sign";
    private static final String CONDITION_VALUE = "value";
    private static final String DURATION_DAYS = "durationDays";
    private static final String FREQUENCY = "frequency";
    private static final String STATUS = "status";

    private Object value;

    public ParameterParser(String jsonString) {
        value = JSONValue.parse(jsonString);
    }

    public Parameter[] parse() throws ParameterParsingException {
        JSONArray array = assertJSONArray("", value);
        Parameter[] result = new Parameter[array.size()];
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = assertJSONObject("", array.get(i));
            String dataSource = String.valueOf(jsonObject.get(DATA_SOURCE));
            JSONArray conditionses = assertJSONArray("", jsonObject.get(CONDITIONSES));
            result[i] = Parameter.of(dataSource, parseConditionsArray(conditionses));
        }
        return result;
    }

    public static Conditions[] parseConditionsArray(JSONArray jsonArray) throws ParameterParsingException {
        Conditions[] result = new Conditions[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            Object o = jsonArray.get(i);
            result[i] = parseConditions((JSONObject) o);
        }
        return result;
    }

    public static Conditionable[] parseConditionables(JSONArray jsonArray) throws ParameterParsingException {
        Conditionable[] result = new Conditionable[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            result[i] = parseConditionable(assertJSONObject("", jsonArray.get(i)));
        }
        return result;
    }

    public static Conditionable parseConditionable(JSONObject jsonObject) throws ParameterParsingException {
        Conditionable conditionable;
        if (jsonObject.get(CONDITIONS_CONJUNCTION) == null) {
            conditionable = parseCondition(jsonObject);
        } else {
            conditionable = parseConditions(jsonObject);
        }
        return conditionable;
    }

    public static Conditions parseConditions(JSONObject jsonObject) throws ParameterParsingException {
        String id = String.valueOf(jsonObject.get(CONDITIONS_ID));
        String conjunction = String.valueOf(jsonObject.get(CONDITIONS_CONJUNCTION));
        JSONArray conditionables = assertJSONArray("", jsonObject.get(CONDITIONS_CONDITIONABLES));
        return Conditions.of(id, Conditions.Conjunction.valueOf(conjunction), parseConditionables(conditionables));
    }

    public static Condition parseCondition(JSONObject jsonObject) throws ParameterParsingException {
        Condition condition;
        String id = String.valueOf(jsonObject.get(CONDITION_ID));
        JSONObject fieldMeta = assertJSONObject("", jsonObject.get(CONDITION_FIELD_META));
        String sign = String.valueOf(jsonObject.get(CONDITION_SIGN));
        String value = String.valueOf(jsonObject.get(CONDITION_VALUE));
        int durationDays = Integer.parseInt(String.valueOf(jsonObject.get(DURATION_DAYS)));
        int frequency = Integer.parseInt(String.valueOf(jsonObject.get(FREQUENCY)));
        Condition.Status status = convertStatus(String.valueOf(jsonObject.get(STATUS)));

        condition = Condition.of(id, parseFieldMeta(fieldMeta), ConditionSign.valueOf(sign), value, durationDays, frequency, status);

        return condition;
    }

    private static Condition.Status convertStatus(String statusStr) throws ParameterParsingException {
        switch(statusStr){
            case "ACTIVE" :
                return Condition.Status.ACTIVE;
            case "INACTIVE" :
                return Condition.Status.INACTIVE;
            case "DELETE" :
                return Condition.Status.DELETE;
            default :
                throw new ParameterParsingException(String.format("invalid condition status : %s", statusStr));
        }
    }

    public static FieldMeta parseFieldMeta(JSONObject jsonObject) throws ParameterParsingException {
        String key = String.valueOf(jsonObject.get(CONDITION_FIELD_META_KEY));
        FieldMetaType type = assertFieldMetaType(String.valueOf(jsonObject.get(CONDITION_FIELD_META_CLAZZ)));
        Long index = assertLong("", jsonObject.get(CONDITION_FIELD_META_INDEX));
        return FieldMeta.of(key, type, null, index.intValue());
    }


    public static JSONArray assertJSONArray(String message, Object value) throws ParameterParsingException {
        if (!JSONArray.class.isAssignableFrom(value.getClass())) {
            throw new ParameterParsingException(message);
        }
        return (JSONArray) value;
    }

    public static JSONObject assertJSONObject(String message, Object value) throws ParameterParsingException {
        if (!JSONObject.class.isAssignableFrom(value.getClass())) {
            throw new ParameterParsingException(message);
        }
        return (JSONObject) value;
    }

    public static Long assertLong(String message, Object value) throws ParameterParsingException {
        if (!Long.class.isAssignableFrom(value.getClass())) {
            throw new ParameterParsingException(message);
        }
        return (Long) value;
    }

    public static FieldMetaType assertFieldMetaType(String value) throws ParameterParsingException {
        FieldMetaType type;
        try {
            type = FieldMetaType.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new ParameterParsingException(e.getMessage());
        }
        return type;
    }
}
