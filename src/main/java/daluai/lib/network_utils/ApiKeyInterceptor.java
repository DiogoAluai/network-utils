package daluai.lib.network_utils;


import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class ApiKeyInterceptor implements Interceptor {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    private final String apiKey;

    public ApiKeyInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request modifiedRequest = originalRequest.newBuilder()
                .addHeader(HEADER_AUTHORIZATION, apiKey)
                .build();
        return chain.proceed(modifiedRequest);
    }
}
