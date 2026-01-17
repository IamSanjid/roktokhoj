package org.team2.roktokhoj_backend.extra;

import tools.jackson.databind.json.JsonMapper;
import tools.jackson.core.JacksonException;

public class JsonUtil {
    private static final JsonMapper mapper;
    static {
        mapper = JsonMapper.builder().build();
    }

    public static <T> T parseJson(String json, Class<T> clazz) throws JacksonException {
        return mapper.readValue(json, clazz);
    }
}
