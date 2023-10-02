package com.genians.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.genians.setup.setup;


public class GnActionTestBase {
    private Properties props;

    private void loadProperties() {
        String canonicalName = this.getClass().getCanonicalName();
        String propFileName = canonicalName.replaceAll("\\.", "/") + ".properties";
        props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try (InputStream resourceStream = loader.getResourceAsStream(propFileName)) {
            props.load(resourceStream);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public String getProperty(String key, String deValue) {
        String value = System.getProperty(key);
        if (value == null) {
            value = setup.getInstance().getSettings(key);
            if (value == null) {
                // class properties
                if (props == null) {
                    loadProperties();
                }
                if (props != null) {
                    value = props.getProperty(key);
                }
            }
        }

        if (value == null) {
            value = deValue;
        }

        return value;
    }
}
