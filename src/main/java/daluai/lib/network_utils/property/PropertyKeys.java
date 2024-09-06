package daluai.lib.network_utils.property;

/**
 * List of property keys
 */
public final class PropertyKeys {

    public static PropertyKey REGISTRY_API_KEY = new PropertyKey("registry.api.key");
    public static PropertyKey REGISTRY_API_KEY_SHA512_DIGEST = new PropertyKey("registry.api.key.sha512.digest");
    public static PropertyKey REGISTRY_API_KEY_AES_SECRET = new PropertyKey("registry.api.key.aes.secret");
    public static PropertyKey FLY_APP_NAME = new PropertyKey("fly.app.name");

    private PropertyKeys() {
    }
}
