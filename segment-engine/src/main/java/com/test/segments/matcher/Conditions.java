package com.test.segments.matcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class Conditions implements Conditionable {
    private String id;
    private Conjunction conjunction;
    private List<Conditionable> conditionables;

    public enum Conjunction {
        AND {
            @Override
            boolean isMatching(List<Conditionable> conditionables, Set<Condition> conditionIds) {
                for (Conditionable conditionable : conditionables) {
                    if (conditionable.matchByConditionIds(conditionIds) == false)
                        return false;
                }
                return true;
            }
        }, OR {
            @Override
            boolean isMatching(List<Conditionable> conditionables, Set<Condition> conditionIds) {
                for (Conditionable conditionable : conditionables) {
                    if (conditionable.matchByConditionIds(conditionIds) == true)
                        return true;
                }
                return false;
            }
        }, NOT {
            @Override
            boolean isMatching(List<Conditionable> conditionables, Set<Condition> conditionIds) {
                for (Conditionable conditionable : conditionables) {
                    if (conditionable.matchByConditionIds(conditionIds) == true)
                        return false;
                }
                return true;
            }
        }
        ;
        abstract boolean isMatching(List<Conditionable> conditionables, Set<Condition> conditionIds);
    }

    /**
     * Set public access default constructor for Jackson Mapper
     */
    public Conditions () { }

    private Conditions(Conjunction conjunction) {
        this.conjunction = conjunction;
    }

    public static Conditions of(Conjunction conjunction, Conditionable... conditionables) {
        return of(null, conjunction, conditionables);
    }

    public static Conditions of(String id, Conjunction conjunction, Conditionable... conditionables) {
        Conditions conditions = new Conditions(conjunction);
        conditions.id = id;
        conditions.conditionables = Arrays.asList(conditionables);
        return conditions;
    }

    @Override
    public boolean matchByConditionIds(Set<Condition> conditionIds) {
        return conjunction.isMatching(conditionables, conditionIds);
    }

    @Override
    public String toConditionString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(" + id + " ");
        for (int i = 0; i < conditionables.size(); i++) {
            Conditionable conditionable = conditionables.get(i);
            if (i > 0)
                sb.append(" " + conjunction.toString() + " ");
            sb.append(conditionable.toConditionString());
        }
        sb.append(")");
        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public Conjunction getConjunction() {
        return conjunction;
    }

    public List<Conditionable> getConditionables() {
        return conditionables;
    }

    public int getDurationDays() {
        int durationDays = 0;
        for(Condition condition : getConditions()) {
            if (condition.getDurationDays() > durationDays) {
                durationDays = condition.getDurationDays();
            }
        }
        return durationDays;
    }

    public List<Condition> getConditions() {
        List<Condition> result = new ArrayList<>();
        for (Conditionable conditionable : conditionables) {
            if (Condition.class.isAssignableFrom(conditionable.getClass())) {
                Condition condition = (Condition) conditionable;
                result.add(condition);
            } else if (Conditions.class.isAssignableFrom(conditionable.getClass())) {
                Conditions conditions = (Conditions) conditionable;
                result.addAll(conditions.getConditions());
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Conditions that = (Conditions) o;

        if (conjunction != that.conjunction) return false;
        return !(conditionables != null ? !conditionables.equals(that.conditionables) : that.conditionables != null);
    }

    @Override
    public int hashCode() {
        int result = conjunction != null ? conjunction.hashCode() : 0;
        result = 31 * result + (conditionables != null ? conditionables.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Conditions{" +
                "id='" + id + '\'' +
                ", conjunction=" + conjunction +
                ", conditionables=" + conditionables +
                '}';
    }
}
