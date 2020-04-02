package utils.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static InputStream CONFIG_LOCATION =Config.class.getClassLoader().getResourceAsStream("config.properties");
    public static Properties getProperties() {
        Properties prop = new Properties();
        try {
            prop.load(CONFIG_LOCATION);
            return prop;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config properties");
        }
    }
}
