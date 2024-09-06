package daluai.lib.network_utils.property;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Manager for fetching properties from resources
 */
public class PropertyManager {

    public static final String FILE_CONFIG_PROPERTIES = "config.properties";
    public static final PropertyManager DEFAULT_INSTANCE = new PropertyManager(FILE_CONFIG_PROPERTIES);
    // user may provide overriding properties
    public static final String VM_OPTION_CONFIG_PROPERTIES = "config.properties.file";

    private static final Logger LOG = LoggerFactory.getLogger(PropertyManager.class);

    private final Properties properties;

    /**
     * Create instance using properties from provided property file
     */
    public static PropertyManager of(String propertiesFile) {
        return new PropertyManager(propertiesFile);
    }

    private PropertyManager(String propertiesFile) {
        this.properties = new Properties();
        loadResources(propertiesFile);
        String propertiesFileVmOption = System.getProperty(VM_OPTION_CONFIG_PROPERTIES);
        if (propertiesFileVmOption != null) {
            loadResources(propertiesFileVmOption);
        }
    }

    private void loadResources(String propertiesFile) {
        try (InputStream input = PropertyManager.class.getClassLoader().getResourceAsStream(propertiesFile)) {
            if (input == null) {
                LOG.info("Unable to find file: " + propertiesFile);
                LOG.info("Properties not loaded");
                return;
            }
            properties.load(input);
        } catch (IOException exception) {
            LOG.error("Error loading properties file", exception);
        }
    }

    /**
     * Safe property fetch, with default value returned when property no found.
     */
    public String getProperty(PropertyKey propertyKey, String defaultValue) {
        String propertyValue = properties.getProperty(propertyKey.key());
        if (propertyValue == null) {
            return defaultValue;
        }
        return propertyValue;
    }

    /**
     * Get property value for provided property key. Throws exception if property is not found.
     */
    public String getProperty(PropertyKey propertyKey) {
        String propertyValue = properties.getProperty(propertyKey.key());
        if (propertyValue == null || propertyValue.isEmpty()) {
            throw new IllegalArgumentException("Property not found: " + propertyKey);
        }
        return propertyValue;
    }
}
