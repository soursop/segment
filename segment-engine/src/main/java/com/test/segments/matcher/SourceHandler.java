package com.test.segments.matcher;

public interface SourceHandler<T> {
    String[] getSource(T t);

    SourceHandler<String[]> STRING_SOURCE_HANDLER = new SourceHandler<String[]>() {
        @Override
        public String[] getSource(String[] strings) {
            String[] source = new String[strings.length];
            for(int i=0; i<strings.length; i++) {
                source[i] = DataHandler.SOURCE_HANDLER.resolve(strings[i]);
            }
            return source;
        }
    };
}
