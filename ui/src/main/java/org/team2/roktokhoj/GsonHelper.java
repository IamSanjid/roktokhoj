package org.team2.roktokhoj;

import com.google.gson.*;
public class GsonHelper {
    private static final Gson GSON;

    static {
        GSON = new GsonBuilder().create();
    }

    public static Gson getInstance() {
        return GSON;
    }
}
