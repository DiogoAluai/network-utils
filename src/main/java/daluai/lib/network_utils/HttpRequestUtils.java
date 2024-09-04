package daluai.lib.network_utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;


public final class HttpRequestUtils {

    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestUtils.class);

    private HttpRequestUtils() {
        // utils class, no instantiation
    }

    public static RequestResult sendHttpRequest(String url, HttpMethod httpMethod, String endpoint) {
        return sendHttpRequest(url, httpMethod, endpoint, null);
    }


    /**
     * Send http request, with possible json body
     */
    public static RequestResult sendHttpRequest(String url, HttpMethod httpMethod, String endpoint, String jsonBody) {
        var request = new Request.Builder()
                .url(url + endpoint);

        switch (httpMethod) {
            case GET -> request.get();
            case POST -> request.post(RequestBody.create(
                    jsonBody,
                    MediaType.get("application/json; charset=utf-8")));
            default -> throw new UnsupportedOperationException(
                    "Http method not supported: " + httpMethod);
        }

        var call = new OkHttpClient().newCall(request.build());
        try (Response response = call.execute()) {
            return response.isSuccessful() ? RequestResult.OK : RequestResult.FAIL;
        } catch (Exception e) {
            LOG.error("Failed during http request", e);
            return RequestResult.FAIL;
        }
    }

    public record SSLOption (SSLContext sslContext, X509TrustManager trustManager) {}

    public static <T extends Serializable> T queryHttpRequest(String url, String endpoint, Class<T> clazz) {
        return queryHttpRequest(url, endpoint, clazz, null, Collections.emptyList());
    }

    public static <T extends Serializable> T queryHttpRequest(String url, String endpoint, JavaType type) {
        return queryHttpRequest(url, endpoint, type, null, Collections.emptyList());
    }

    public static <T extends Serializable> T queryHttpRequest(String url, String endpoint, Class<T> clazz,
                                                              SSLOption sslOption, List<Interceptor> interceptors) {
        JavaType type = TypeFactory.defaultInstance().constructType(clazz);
        return queryHttpRequest(url, endpoint, type, sslOption, interceptors);
    }

    /**
     * Send http GET request and deserialize it to provided class.
     * @param type type of returned object
     * @param sslOption nullable ssl option for https communication
     * @param interceptors interceptors to be added, such as {@link ApiKeyInterceptor API key in header}
     */
    public static <T extends Serializable> T queryHttpRequest(String url, String endpoint, JavaType type,
                                                              SSLOption sslOption, List<Interceptor> interceptors) {
        var request = new Request.Builder()
                .url(url + endpoint)
                .get()
                .build();

        var client = new OkHttpClient.Builder();
        if (sslOption != null) {
            client.sslSocketFactory(sslOption.sslContext.getSocketFactory(), sslOption.trustManager)
                    .hostnameVerifier((hostname, session) -> {
                        return true; // Bypass hostname verification
                    });
        }

        interceptors.forEach(client::addInterceptor);

        var call = client.build().newCall(request);
        try (Response response = call.execute()) {
            ResponseBody body = response.body();
            if (body == null) {
                LOG.error("Response was ok, but body was null.");
                return null;
            }
            if (type.getRawClass().equals(byte[].class)) {
                return (T) body.bytes();
            }

            return new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(body.string(), type);
        } catch (IOException e) {
            LOG.error("Ran into IOException. Possible serialization issue, or empty response body. Returning null", e);
            return null;
        }
    }

}
