package com.test.segments.matcher;

/**
 * Created by sonegy@sk.com on 2016. 2. 24..
 */
public interface Evaluation {
    boolean evaluate(String source, Object target) throws Exception;

    Evaluation CONTAINS_EVAL = new Evaluation() {
        public boolean evaluate(String source, Object target) throws Exception {
            return source.contains(String.valueOf(target));
        }
    };
    Evaluation NOT_CONTAINS_EVAL = new Evaluation() {
        public boolean evaluate(String source, Object target) throws Exception {
            return !CONTAINS_EVAL.evaluate(source, target);
        }
    };
    Evaluation START_WITH_EVAL = new Evaluation() {
        public boolean evaluate(String source, Object target) throws Exception {
            return source.startsWith(String.valueOf(target));
        }
    };
    Evaluation NOT_START_WITH_EVAL = new Evaluation() {
        public boolean evaluate(String source, Object target) throws Exception {
            return !START_WITH_EVAL.evaluate(source, target);
        }
    };
    Evaluation END_WITH_EVAL = new Evaluation() {
        public boolean evaluate(String source, Object target) throws Exception {
            return source.endsWith(String.valueOf(target));
        }
    };
    Evaluation NOT_END_WITH_EVAL = new Evaluation() {
        public boolean evaluate(String source, Object target) throws Exception {
            return !END_WITH_EVAL.evaluate(source, target);
        }
    };
    Evaluation EQUALS_EVAL = new Evaluation() {
        public boolean evaluate(String source, Object target) throws Exception {
            return source.equals(String.valueOf(target));
        }
    };
    Evaluation NOT_EQUALS_EVAL = new Evaluation() {
        public boolean evaluate(String source, Object target) throws Exception {
            return !EQUALS_EVAL.evaluate(source, target);
        }
    };
    Evaluation LESS_THAN_EVAL = new Evaluation() {
        public boolean evaluate(String source, Object target) throws Exception {
            LongValueValidation s = new LongValueValidation(source);
            LongValueValidation t = new LongValueValidation(target);
            return s.get() < t.get();
        }
    };
    Evaluation LESS_THAN_OR_EQUALS_TO_EVAL = new Evaluation() {
        @Override
        public boolean evaluate(String source, Object target) throws Exception {
            LongValueValidation s = new LongValueValidation(source);
            LongValueValidation t = new LongValueValidation(target);
            return s.get() <= t.get();
        }
    };
    Evaluation GREATER_THAN_EVAL = new Evaluation() {
        public boolean evaluate(String source, Object target) throws Exception {
            LongValueValidation s = new LongValueValidation(source);
            LongValueValidation t = new LongValueValidation(target);
            return s.get() > t.get();
        }
    };
    Evaluation GREATER_THAN_OR_EQUALS_TO_EVAL = new Evaluation() {
        @Override
        public boolean evaluate(String source, Object target) throws Exception {
            LongValueValidation s = new LongValueValidation(source);
            LongValueValidation t = new LongValueValidation(target);
            return s.get() >= t.get();
        }
    };
}
