package org.team2.roktokhoj_backend.services;

import org.springframework.stereotype.Service;
import org.team2.roktokhoj_backend.extra.JsonUtil;
import org.team2.roktokhoj_backend.extra.Utils;
import org.team2.roktokhoj_backend.models.map.ReversePlace;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

@Service
public class MapsService {
    public static final URI BIGDATACLOUD_REVERSE_GEOCODE_API = URI.create("https://api.bigdatacloud.net/data/reverse-geocode-client");

    private final BasicHttpService httpService;

    public MapsService(BasicHttpService httpService) {
        this.httpService = httpService;
    }

    public CompletableFuture<ReversePlace> reverseSearch(double latitude, double longitude) throws URISyntaxException {
        var uri = Utils.appendURIQueries(BIGDATACLOUD_REVERSE_GEOCODE_API, "latitude=" + latitude, "longitude=" + longitude, "localityLanguage=en");
        return httpService.fetchString(uri)
                .thenApply(r -> JsonUtil.parseJson(r, ReversePlace.class));
    }
}
