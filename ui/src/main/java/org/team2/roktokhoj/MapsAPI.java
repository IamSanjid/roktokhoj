package org.team2.roktokhoj;

import com.google.gson.reflect.TypeToken;
import org.team2.roktokhoj.models.map.Place;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MapsAPI {
    public static final URI NOMINATIM_SEARCH_API = URI.create("https://nominatim.openstreetmap.org/search");

    public static CompletableFuture<List<Place>> searchAddress(String address, int resultLimit) throws URISyntaxException {
        if (resultLimit <= 0 || resultLimit > 30) resultLimit = 10;
        var uri = Utils.appendURIQueries(NOMINATIM_SEARCH_API, "q=" + address, "format=json", "limit=" + resultLimit);
        return CompletableFuture.supplyAsync(Utils.wrapFutureToSupplier(RHttpClient.fetchString(uri)))
                .handle((r, e) -> {
                    if (e != null) throw new RuntimeException(e);
                    Type listType = new TypeToken<List<Place>>() {
                    }.getType();
                    return GsonHelper.getInstance().fromJson(r, listType);
                });
    }
}
