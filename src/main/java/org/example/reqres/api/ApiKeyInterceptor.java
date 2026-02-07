package org.example.reqres.api;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class ApiKeyInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String apiKey = System.getenv("REQRES_API_KEY");
        Request newRequest = chain.request().newBuilder()
                .addHeader("x-api-key", apiKey)
                .addHeader("Content-Type", "application/json")
                .build();
        return chain.proceed(newRequest);
    }
}
