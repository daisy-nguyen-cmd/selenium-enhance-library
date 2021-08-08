package org.my.automationtest;

import org.my.automationtest.constants.SeleniumConstants;
import org.my.automationtest.service.MyProperties;
import org.my.automationtest.service.WebUI;
import org.my.automationtest.utils.FileUtil;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;

import java.util.Map;
import java.util.Set;

public class BaseAutomationTest {

    private MyProperties myProperties;
    private ITestContext testContext;

    @BeforeTest(groups = "3", dependsOnGroups = "2")
    protected void initializeAppParams() {
        Set<String> propertyKeys = myProperties.getAllPropertyKeys();

        for (String key : propertyKeys) {
            if (key.indexOf("webdriver") == 0) {
                String systemWebDriverPathConfig = System.getProperty(key);
                if (systemWebDriverPathConfig == null || systemWebDriverPathConfig.isEmpty()) {
                    System.setProperty(key, FileUtil.obtainResourcesDirPath() + "/" + myProperties.getString(key));
                }
            }
            if (SeleniumConstants.SELECTED_WEB_BROWSER_KEY.equals(key)) {
                WebUI.setSelectWebBrowser(getProperty(SeleniumConstants.SELECTED_WEB_BROWSER_KEY));
            }
            if (SeleniumConstants.DEFAULT_WAIT_UNTIL_TIMEOUT_KEY.equals(key)) {
                WebUI.setDefaultWaitUntilTimeout(Long.parseLong(getProperty(SeleniumConstants.DEFAULT_WAIT_UNTIL_TIMEOUT_KEY)));
            }
        }

//        String sysSelectedWebBrowser = System.getProperty(SeleniumConstants.SELECTED_WEB_BROWSER_KEY);
//        if (sysSelectedWebBrowser != null && !sysSelectedWebBrowser.isEmpty()) {
//            WebUI.setSelectWebBrowser(sysSelectedWebBrowser);
//        }
    }

    @BeforeTest(groups = "2", dependsOnGroups = "1")
    protected void loadProperties() {
        String dataFileName = getTestContext().getCurrentXmlTest().getParameter("dataFile");
        if (dataFileName == null || dataFileName.isEmpty()) {
            dataFileName = getClass().getSimpleName() + ".properties";
        }
        myProperties = new MyProperties(dataFileName);

        Map<String, String> params = getTestContext().getCurrentXmlTest().getAllParameters();
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                getMyProperties().setString(param.getKey(), param.getValue());
            }
        }
        Set<String> attributeNames = getTestContext().getAttributeNames();
        if (attributeNames != null) {
            for (String attributeName : attributeNames) {
                getMyProperties().setString(attributeName, (String) getTestContext().getAttribute(attributeName));
            }
        }

        // Load from system properties
        System.getProperties().forEach((key, value) -> {
            String keyStr = key == null ? null : (key instanceof String ? (String) key : key.toString());
            String valueStr = value == null ? null : (value instanceof String ? (String) value : value.toString());
            getMyProperties().setString(keyStr, valueStr);
        });
    }

    @BeforeTest(groups = "1")
    public void obtainTestContext(ITestContext testContext) {
        this.testContext = testContext;
    }

    protected void takeScreenshot() {
        WebUI.takeScreenshot(getTestContext(), getClass().getSimpleName());
    }

    protected String getProperty(String key) {
        String value = getProperty(key, null);
        if (value == null) {
            throw new RuntimeException(String.format("Cannot find property with key = '%s'", key));
        } else {
            return value;
        }
    }

    protected String getProperty(String key, String defaultStr) {
        String value = getMyProperties().getString(key);
        if (value == null || value.isEmpty()) {
            value = defaultStr;
        }
        return value;
    }

    protected void setProperty(String key, String value) {
        getMyProperties().setString(key, value);
        getTestContext().setAttribute(key, value);
    }

    protected MyProperties getMyProperties() {
        return myProperties;
    }

    protected ITestContext getTestContext() {
        return testContext;
    }
}
