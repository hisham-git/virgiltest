package mt.siteportal.utils.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Test data provider (a singleton container for properties).
 *
 */
public abstract class PropertiesHolder {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesHolder.class);

    private static Properties properties;
    public static Properties getProperties()
    {
        if (null == properties){
            try {
                properties = PropertiesLoaderUtils.loadAllProperties("smoke_tests.properties");
            }
        catch (IOException e) {
                logger.error("Cannot load property file.");     }
        }

        return properties;
    }
}
