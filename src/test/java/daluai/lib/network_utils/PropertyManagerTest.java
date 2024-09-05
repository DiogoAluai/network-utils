package daluai.lib.network_utils;

import daluai.lib.network_utils.property.PropertyKey;
import daluai.lib.network_utils.property.PropertyManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This is a test class
 */
public class PropertyManagerTest {

    @Test
    public void fetchProperty() {
        var propertyManager = PropertyManager.of("testconfig.properties");
        var testPropertyKey = new PropertyKey("test.api.key");
        assertEquals("test_api_key_value", propertyManager.getProperty(testPropertyKey));
    }
}
