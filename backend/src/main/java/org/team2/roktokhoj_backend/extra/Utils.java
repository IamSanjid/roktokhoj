package org.team2.roktokhoj_backend.extra;

import java.net.URI;
import java.net.URISyntaxException;

public class Utils {
    public static URI appendURIQueries(URI oldUri, String... queries) throws URISyntaxException {
        var oldQuery = oldUri.getQuery();
        StringBuilder newQuery = new StringBuilder();
        int i = 0;
        if (oldQuery == null) {
            newQuery.append(queries[0]);
            i = 1;
        }
        for (; i < queries.length; i++) {
            newQuery.append("&").append(queries[i]);
        }

        return new URI(oldUri.getScheme(), oldUri.getAuthority(),
                oldUri.getPath(), newQuery.toString(), oldUri.getFragment());
    }
}
