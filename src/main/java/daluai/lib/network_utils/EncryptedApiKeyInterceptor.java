package daluai.lib.network_utils;


import daluai.lib.encryption.AESEncrypter;
import daluai.lib.network_utils.property.PropertyKey;
import daluai.lib.network_utils.property.PropertyManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Intercept http requests to add encrypted Api key header.
 * For Api key filtering check services-parent's ApiKeyFilter
 */
public class EncryptedApiKeyInterceptor implements Interceptor {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    private final String encryptedApiKey;

    /**
     * Fetch api key and aes secret from properties and use them.
     */
    public EncryptedApiKeyInterceptor(PropertyKey apiProperty, PropertyKey encryptionSecretProperty) {
        this(PropertyManager.DEFAULT_INSTANCE, apiProperty, encryptionSecretProperty);
    }

    public EncryptedApiKeyInterceptor(PropertyManager propertyManager, PropertyKey apiProperty, PropertyKey encryptionSecretProperty) {
        this(propertyManager.getProperty(apiProperty),
                propertyManager.getProperty(encryptionSecretProperty));
    }

    public EncryptedApiKeyInterceptor(PropertyKey apiProperty, String encryptionSecret) {
        this(PropertyManager.DEFAULT_INSTANCE.getProperty(apiProperty), encryptionSecret);
    }


    public EncryptedApiKeyInterceptor(String apiKey, String encryptionSecret) {
        var encrypter = new AESEncrypter(encryptionSecret);
        try {
            this.encryptedApiKey = encrypter.encrypt(apiKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt");
        }
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request modifiedRequest = originalRequest.newBuilder()
                .addHeader(HEADER_AUTHORIZATION, encryptedApiKey)
                .build();
        return chain.proceed(modifiedRequest);
    }
}
