package org.team2.roktokhoj;

import com.google.gson.reflect.TypeToken;
import org.team2.roktokhoj.models.map.ClientIp;
import org.team2.roktokhoj.models.map.IpGeo;
import org.team2.roktokhoj.models.map.Place;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MapsAPI {
    public static final URI NOMINATIM_SEARCH_API = URI.create("https://nominatim.openstreetmap.org/search");
    public static final URI CLIENT_IP = URI.create("https://api.bigdatacloud.net/data/client-ip");
    public static final URI IP_GEO = URI.create("https://api.techniknews.net/ipgeo");

    private static final Type LIST_PLACE_TYPE = new TypeToken<List<Place>>() {
    }.getType();
    private static final Type CLIENT_IP_TYPE = new TypeToken<ClientIp>() {
    }.getType();
    private static final Type IP_GEO_TYPE = new TypeToken<IpGeo>() {
    }.getType();

    public static CompletableFuture<List<Place>> searchAddress(String address, int resultLimit) throws URISyntaxException {
        if (resultLimit <= 0 || resultLimit > 30) resultLimit = 10;
        var uri = Utils.appendURIQueries(NOMINATIM_SEARCH_API, "q=" + address, "format=json", "limit=" + resultLimit);
        return CompletableFuture.supplyAsync(Utils.wrapFutureToSupplier(RHttpClient.fetchString(uri)))
                .thenApply(r -> GsonHelper.getInstance().fromJson(r, LIST_PLACE_TYPE));
    }

    public static CompletableFuture<IpGeo> getCurrentAddressByIp() {
        CompletableFuture<ClientIp> clientIpCompletableFuture = CompletableFuture.supplyAsync(Utils.wrapFutureToSupplier(RHttpClient.fetchString(CLIENT_IP)))
                .thenApply(r -> GsonHelper.getInstance().fromJson(r, CLIENT_IP_TYPE));
        return clientIpCompletableFuture.thenCompose(r -> {
            try {
                return getCurrentAddressByIp(r.getIpString());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static CompletableFuture<IpGeo> getCurrentAddressByIp(String ip) throws URISyntaxException {
        return getCurrentAddressByIpImpl(Utils.newURIWithPath(IP_GEO, ip));
    }

    private static CompletableFuture<IpGeo> getCurrentAddressByIpImpl(URI endpoint) {
        return CompletableFuture.supplyAsync(Utils.wrapFutureToSupplier(RHttpClient.fetchString(endpoint)))
                .thenApply(r -> GsonHelper.getInstance().fromJson(r, IP_GEO_TYPE));
    }
}
