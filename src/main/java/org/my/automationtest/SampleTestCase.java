package org.my.automationtest;

import org.my.automationtest.locator.WebLocator;
import org.my.automationtest.locator.helper.CommonLocatorHelper;
import org.my.automationtest.service.WebUI;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SampleTestCase extends BaseAutomationTest {

    @Test(retryAnalyzer = WebUI.DefaultRetryControl.class)
    public void testSearchInAmazon() {
        WebUI.initializeNewSession();
        WebUI.maximizeWindow();
        WebUI.getUrl(getProperty("url"));
        WebUI.setText(new WebLocator().tag("input").id("twotabsearchtextbox"), getProperty("search.text"));
        WebUI.click(new WebLocator().tag("input").type("submit"));
        try {
            WebUI.waitForElementVisible( CommonLocatorHelper.findByContainedText("results for") );
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
}
