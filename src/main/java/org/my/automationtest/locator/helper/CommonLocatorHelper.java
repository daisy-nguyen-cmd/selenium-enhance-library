package org.my.automationtest.locator.helper;

import org.my.automationtest.locator.WebLocator;

public class CommonLocatorHelper {
    public static WebLocator findByContainedText(String containedText) {
        return new WebLocator().xpath(String.format("//*[text()[contains(., '%s')]]", containedText));
    }
}
