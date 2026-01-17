package org.team2.roktokhoj;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class RHttpClient {
    private static final ThreadLocal<HttpClient> threadLocalValue;

    static {
        threadLocalValue = ThreadLocal.withInitial(HttpClient::newHttpClient);
//        Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36

//        fetch("https://nominatim.openstreetmap.org/search?q=Rampura%2C%20Dhaka&format=json&limit=10", {
//                "headers": {
//                    "accept": "*/*",
//                    "accept-language": "en-US,en;q=0.9",
//                    "priority": "u=1, i",
//                    "sec-ch-ua": "\"Not)A;Brand\";v=\"8\", \"Chromium\";v=\"138\", \"Google Chrome\";v=\"138\"",
//                    "sec-ch-ua-mobile": "?0",
//                    "sec-ch-ua-platform": "\"Windows\"",
//                    "sec-fetch-dest": "empty",
//                    "sec-fetch-mode": "cors",
//                    "sec-fetch-site": "cross-site"
//                },
//                "body": null,
//                "method": "GET",
//                "mode": "cors",
//                "credentials": "omit"
//        });
    }

    private static HttpRequest.Builder safeRequest(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36")
                .header("Accept", "*/*")
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("Sec-CH-UA-Mobile", "?0")
                .header("Sec-CH-UA-Platform", "Windows")
                .header("Sec-Fetch-Mode", "cross-site")
                .header("Sec-Fetch-Site", "hea");
    }

    public static Future<String> fetchString(URI uri) {
        return ThreadPool.getExecutor().submit(() -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();
            HttpResponse<String> response = threadLocalValue.get().send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        });
    }

    public static Future<String> postString(URI uri, String body) {
        return ThreadPool.getExecutor().submit(() -> {
            var bodyPublisher = HttpRequest.BodyPublishers.ofString(body);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(bodyPublisher)
                    .build();
            HttpResponse<String> response = threadLocalValue.get().send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        });
    }
}
