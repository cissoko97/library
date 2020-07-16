package org.ckCoder.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GetFileProperties {

    public static Properties getFileConfigProperties() throws IOException {
        InputStream inputStream = GetFileProperties.class.getResourceAsStream("/properties/config.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }
}
