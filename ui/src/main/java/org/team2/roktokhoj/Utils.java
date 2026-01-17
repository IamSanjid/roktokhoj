package org.team2.roktokhoj;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Future;
import java.util.function.Supplier;

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

    public static URI newURIWithPath(URI oldUri, String path) throws URISyntaxException {
        var oldPath = oldUri.getPath();
        StringBuilder newPath = new StringBuilder();
        var tmpPath = path.startsWith("/") ? path : "/" + path;
        if (oldPath == null || oldPath.isBlank() || oldPath.equals("/")) {
            newPath.append(tmpPath);
        } else {
            newPath.append(oldPath).append(tmpPath);
        }

        return new URI(oldUri.getScheme(), oldUri.getUserInfo(), oldUri.getHost(), oldUri.getPort(), newPath.toString(),
                oldUri.getQuery(), oldUri.getFragment());
    }

    public static <T> Supplier<T> wrapFutureToSupplier(Future<T> future) {
        return () -> {
            try {
                return future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static Double getScreenCircleRadius(Double lat, Double radius, Double zoom) {
//        var metersPerPixel =
//                (40075016.686 * Math.cos(lat * Math.PI / 180)) /
//                        (256 * Math.pow(2, zoom));
        var metersPerPixel = (156543.03392 * Math.cos(lat * Math.PI / 180)) / Math.pow(2, zoom);

        return radius / metersPerPixel;
    }
}
