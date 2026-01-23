package org.team2.roktokhoj;

import com.google.gson.reflect.TypeToken;
import org.team2.roktokhoj.models.*;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ServiceAPI {
    public static final URI API;
    public static final URI REGISTER_DONOR_ENDPOINT;
    public static final URI LOGIN_DONOR_ENDPOINT;
    public static final URI FIND_DONOR_ENDPOINT;
    public static final URI GET_PROFILE_ENDPOINT;
    public static final URI UPDATE_PROFILE_ENDPOINT;

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
            REGISTER_DONOR_ENDPOINT = Utils.newURIWithPath(API, "register_donor");
            LOGIN_DONOR_ENDPOINT = Utils.newURIWithPath(API, "login_donor");
            FIND_DONOR_ENDPOINT = Utils.newURIWithPath(API, "find_donor");
            GET_PROFILE_ENDPOINT = Utils.newURIWithPath(API, "get_profile");
            UPDATE_PROFILE_ENDPOINT = Utils.newURIWithPath(API, "update_profile");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    private static final Type BLOOD_DONOR_TYPE = new TypeToken<BloodDonor>() {
    }.getType();
    private static final Type BLOOD_DONOR_LIST_TYPE = new TypeToken<List<BloodDonor>>() {
    }.getType();
    private static final Type PROFILE_BLOOD_DONOR_TYPE = new TypeToken<ProfileBloodDonor>() {
    }.getType();
    private static final Type RESPONSE_ERROR_TYPE = new TypeToken<ResponseError>() {
    }.getType();

    public static CompletableFuture<ProfileBloodDonor> registerBloodDonor(NewBloodDonor newBloodDonor) {
        return postJson(REGISTER_DONOR_ENDPOINT, newBloodDonor, PROFILE_BLOOD_DONOR_TYPE);
    }

    public static CompletableFuture<ProfileBloodDonor> loginBloodDonor(LoginBloodDonor loginBloodDonor) {
        return postJson(LOGIN_DONOR_ENDPOINT, loginBloodDonor, PROFILE_BLOOD_DONOR_TYPE);
    }

    public static CompletableFuture<List<BloodDonor>> findBloodDonor(FindBloodDonor findBloodDonor) {
        return postJson(FIND_DONOR_ENDPOINT, findBloodDonor, BLOOD_DONOR_LIST_TYPE);
    }

    public static CompletableFuture<ProfileBloodDonor> getProfile(BloodDonor donor) {
        return CompletableFuture.supplyAsync(Utils.wrapFutureToSupplier(RHttpClient.fetchString(GET_PROFILE_ENDPOINT, "Bearer " + donor.getToken())))
                .thenApply(r -> {
                    var re = checkForResponseError(r);
                    if (re != null) {
                        throw new RuntimeException(re.getMessage());
                    }
                    return GsonHelper.getInstance().fromJson(r, PROFILE_BLOOD_DONOR_TYPE);
                });
    }

    public static CompletableFuture<ProfileBloodDonor> updateProfile(NewBloodDonor updatedDonor) {
        var body = GsonHelper.getInstance().toJson(updatedDonor);
        var token = updatedDonor.getProfile().getInfo().getToken();
        return CompletableFuture.supplyAsync(Utils.wrapFutureToSupplier(RHttpClient.putString(UPDATE_PROFILE_ENDPOINT, body, "Bearer " + token)))
                .thenApply(r -> {
                    var re = checkForResponseError(r);
                    if (re != null) {
                        throw new RuntimeException(re.getMessage());
                    }
                    return GsonHelper.getInstance().fromJson(r, PROFILE_BLOOD_DONOR_TYPE);
                });
    }

    private static <T, R> CompletableFuture<R> postJson(URI endpoint, T bodyObject, Type resultType) {
        var body = GsonHelper.getInstance().toJson(bodyObject);
        return CompletableFuture.supplyAsync(Utils.wrapFutureToSupplier(RHttpClient.postString(endpoint, body)))
                .thenApply(r -> {
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
