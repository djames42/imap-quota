package com.djames42.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

public class GetProperties {
    private ResourceBundle resourceBundle;
    private Properties properties;

    public GetProperties(String propFile) {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(propFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getProperties(String property) {
        return properties.getProperty(property);
    }
}
