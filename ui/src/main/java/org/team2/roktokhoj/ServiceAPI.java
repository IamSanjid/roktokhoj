package org.team2.roktokhoj;

import com.google.gson.reflect.TypeToken;
import org.team2.roktokhoj.models.BloodDonor;
import org.team2.roktokhoj.models.FindBloodDonor;
import org.team2.roktokhoj.models.RegisterBloodDonor;
import org.team2.roktokhoj.models.ResponseError;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ServiceAPI {
    public static final URI API;
    public static final URI RegisterDonorEndpoint;
    public static final URI FindDonorEndpoint;

    static {
        var configUrl = ServiceAPI.class.getResource("config.json");
        Config config;
        if (configUrl == null) {
            config = new Config();
        } else {
            try {
                var path = Paths.get(configUrl.toURI());
                String configStr = Files.readString(path);
                Type configType = new TypeToken<Config>() {
                }.getType();
                config = GsonHelper.getInstance().fromJson(configStr, configType);
            } catch (Exception e) {
                config = new Config();
            }
        }
        try {
            API = URI.create(config.getServiceAPIUrl());
            RegisterDonorEndpoint = Utils.newURIWithPath(API, "register_donor");
            FindDonorEndpoint = Utils.newURIWithPath(API, "find_donor");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    private static final Type BLOOD_DONOR_TYPE = new TypeToken<BloodDonor>() {}.getType();
    private static final Type BLOOD_DONOR_LIST_TYPE = new TypeToken<List<BloodDonor>>() {}.getType();
    private static final Type RESPONSE_ERROR_TYPE = new TypeToken<ResponseError>() {}.getType();

    public static CompletableFuture<BloodDonor> registerBloodDonor(RegisterBloodDonor registerBloodDonor) {
        return postJson(RegisterDonorEndpoint, registerBloodDonor, BLOOD_DONOR_TYPE);
    }

    public static CompletableFuture<List<BloodDonor>> findBloodDonor(FindBloodDonor findBloodDonor) {
        return postJson(FindDonorEndpoint, findBloodDonor, BLOOD_DONOR_LIST_TYPE);
    }

    private static<T, R> CompletableFuture<R> postJson(URI endpoint, T bodyObject, Type resultType) {
        var body = GsonHelper.getInstance().toJson(bodyObject);
        return CompletableFuture.supplyAsync(Utils.wrapFutureToSupplier(RHttpClient.postString(endpoint, body)))
                .handle((r, e) -> {
                    if (e != null) throw new RuntimeException(e);
                    var re = checkForResponseError(r);
                    if (re != null) {
                        throw new RuntimeException(re.getMessage());
                    }
                    return GsonHelper.getInstance().fromJson(r, resultType);
                });
    }

    private static ResponseError checkForResponseError(String r) {
        try {
            ResponseError re = GsonHelper.getInstance().fromJson(r, RESPONSE_ERROR_TYPE);
            if (re.getMessage() == null && re.getTimeStamp() == -1 && re.getStatus() == -1) {
                return null;
            }
            return re;
        } catch (Exception _) {
            return null;
        }
    }
}
