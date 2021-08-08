package org.my.automationtest.service;

import org.my.automationtest.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITest;
import org.testng.ITestContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class MyProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyProperties.class);
    private Properties properties = new Properties();

    public MyProperties(String relativeFilePath) {
        try (InputStream commonFis = getClass().getResourceAsStream("/Common.properties")) {
            properties.load(commonFis);
        } catch (Exception e) {
            LOGGER.error("Cannot load Common.properties", e);
        }

        try (InputStream inputFis = getClass().getResourceAsStream("/" + relativeFilePath)) {
            properties.load(inputFis);
        } catch (Exception e) {
            LOGGER.warn("Cannot load {}", relativeFilePath);
        }
    }

    public Set<String> getAllPropertyKeys() {
        return properties.stringPropertyNames();
    }

    public String getString(String key) {
        return properties.getProperty(key);
    }

    public void setString(String key, String value) {
        properties.setProperty(key, value);
    }
}
