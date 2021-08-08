package org.my.automationtest.service;

import org.my.automationtest.constants.SeleniumConstants;
import org.my.automationtest.locator.WebLocator;
import org.my.automationtest.utils.DateTimeUtil;
import org.my.automationtest.utils.FileUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

import static org.my.automationtest.constants.SeleniumConstants.DEFAULT_SLEEP_THEN_ACTION_TIME;

public class WebUI {

//    private static final WebUI WEB_UI_INSTANCE = new WebUI();
    private static WebDriver webDriver;
    private static Method previousAction;
    private static Object[] previousActionParams;
    private static String selectWebBrowser = "chrome";
    private static long defaultWaitUntilTimeout = 20; // in seconds

    private static final Logger LOGGER = LoggerFactory.getLogger(WebUI.class);

    public static void getUrl(String url) {
        webDriver.get(url);
    }

    public static void maximizeWindow() {
        webDriver.manage().window().maximize();
    }

    /**
     * Repeatably check for the web element located by <code>webLocator</code> with timeout is {@link #defaultWaitUntilTimeout}
     * @param webLocator {@link WebLocator} instance to locate web element
     * @return {@link WebElement} instance if the web element is found and visible, throw {@link TimeoutException} otherwise
     */
    public static WebElement waitForElementVisible(WebLocator webLocator) {
        return waitForElementVisible(webLocator, defaultWaitUntilTimeout);
    }

    /**
     * Repeatably check for the web element located by <code>webLocator</code> to be visible with <code>timeout</code>
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param timeout maximum time allowed to wait for element to be visible
     * @return {@link WebElement} instance if the web element is found and visible, throw {@link TimeoutException} otherwise
     */
    public static WebElement waitForElementVisible(WebLocator webLocator, long timeout) {
        WebDriverWait wait = new WebDriverWait(webDriver, timeout);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(webLocator.toBy()));
    }

    /**
     * Repeatably check for the web element located by <code>webLocator</code> to be clickable with timeout is {@link #defaultWaitUntilTimeout}
     * @param webLocator {@link WebLocator} instance to locate web element
     * @return {@link WebElement} instance if the web element is found and clickable, throw {@link TimeoutException} otherwise
     */
    public static WebElement waitForElementClickable(WebLocator webLocator) {
        return waitForElementClickable(webLocator, defaultWaitUntilTimeout);
    }

    /**
     * Repeatably check for the web element located by <code>webLocator</code> to be clickable with <code>timeout</code>
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param timeout maximum time allowed to wait for element to be clickable
     * @return {@link WebElement} instance if the web element is found and clickable, throw {@link TimeoutException} otherwise
     */
    public static WebElement waitForElementClickable(WebLocator webLocator, long timeout) {
        WebDriverWait wait = new WebDriverWait(webDriver, timeout);
        return wait.until(ExpectedConditions.elementToBeClickable(webLocator.toBy()));
    }

    /**
     * Repeatably check for the web element located by <code>webLocator</code> to be invisible timeout is {@link #defaultWaitUntilTimeout}
     * @param webLocator {@link WebLocator} instance to locate web element
     * @return {@link WebElement} instance if the web element is found and invisible, throw {@link TimeoutException} otherwise
     */
    public static void waitForElementInvisible(WebLocator webLocator) {
        waitForElementInvisible(webLocator, defaultWaitUntilTimeout);
    }

    /**
     * Repeatably check for the web element located by <code>webLocator</code> to be invisible with <code>timeout</code>
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param timeout maximum time allowed to wait for element to be invisible
     * @return {@link WebElement} instance if the web element is found and invisible, throw {@link TimeoutException} otherwise
     */
    public static void waitForElementInvisible(WebLocator webLocator, long timeout) {
        WebDriverWait wait = new WebDriverWait(webDriver, timeout);
        boolean isElementInvisible =  wait.until(ExpectedConditions.invisibilityOfElementLocated(webLocator.toBy()));
        if (!isElementInvisible) {
            throw new RuntimeException("Element is not invisible after waiting for " + timeout + " seconds");
        }
    }

    /*Start of retry disabled actions in Selenium WebDriver*/
    /**
     * Wait for iframe located by <code>webLocator</code> to be visible with timeout is {@link #defaultWaitUntilTimeout} then switch to HTML DOM inside this iframe
     * @param webLocator {@link WebLocator} instance to locate iframe
     */
    public static void switchToFrame(WebLocator webLocator) {
        switchToFrame(webLocator, defaultWaitUntilTimeout);
    }

    /**
     * Wait for iframe located by <code>webLocator</code> to be visible with <code>timeout</code> then switch to HTML DOM inside this iframe
     * @param webLocator {@link WebLocator} instance to locate iframe
     * @param timeout maximum time allowed to wait for iframe to be visible
     */
    public static void switchToFrame(WebLocator webLocator, long timeout) {
        webDriver.switchTo().frame(waitForElementVisible(webLocator, timeout));
    }

    /**
     * Switch out of current iframe to default HTML DOM (normally parent of iframe - original web page)
     */
    public static void switchOutOfFrame() {
        webDriver.switchTo().defaultContent();
    }

    /**
     * Switch to next tab in browser
     * For example, from left to right, if your current tab index is 2nd, then it will switch to 3rd tab
     */
    public static void switchToNextTab() {
        List<String> windows = webDriver.getWindowHandles().stream().collect(Collectors.toList());
        String currentWindow = webDriver.getWindowHandle();
        int currentIndex = windows.indexOf(currentWindow);
        webDriver.switchTo().window(windows.get(currentIndex + 1));
    }

    /**
     * Switch to previous tab in browser
     * For example, from left to right, if your current tab index is 2nd, then it will switch to 1st tab
     */
    public static void switchToPrevTab() {
        List<String> windows = webDriver.getWindowHandles().stream().collect(Collectors.toList());
        String currentWindow = webDriver.getWindowHandle();
        int currentIndex = windows.indexOf(currentWindow);
        webDriver.switchTo().window(windows.get(currentIndex - 1));
    }

    /**
     * open web page with <code>url</code> in new tab
     * @param url url to be opened
     */
    public static void openUrlInNewTab(String url) {
        executeJavascript(String.format("window.open('%s', '_blank');", url));
    }

    /**
     * Scroll to web element located by <code>webLocator</code>
     * @param webLocator {@link WebLocator} instance to locate web element
     */
    public static void scrollToElement(WebLocator webLocator) {
        if (webLocator.toXpath() == null) {
            throw new RuntimeException("Cannot generate xpath from input webLocator");
        }
        String script = String.format(SeleniumConstants.JS_FIND_ELEMENT_BY_XPATH, webLocator.toXpath()) + ".scrollIntoView();";
        executeJavascript(script);
    }
    /*End of retry disabled actions in Selenium WebDriver*/

    /*Start of retry enabled actions in Selenium WebDriver*/
    /**
     * Wait for element located by <code>webLocator</code> to be visible with timeout is {@link #defaultWaitUntilTimeout} then append <code>text</code> to this element.<br/><br/>
     * This method is controlled with retryPreviousAction enabled, that means if {@link TimeoutException} is thrown out when wait for element to be visible, it will retry with registered previous action.<br/><br/>
     * Method which is controlled with retryPreviousAction enabled is also registered as previousAction when it is completed.<br/><br/>
     * After web element is visible, this method will try {@link WebElement#sendKeys(CharSequence... text)} first, if there is any exception thrown out then it will retry with {@link Actions#sendKeys(WebElement webElement, CharSequence... text)}
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param text text to be appended to web element
     */
    public static void sendKeys(WebLocator webLocator, String text) {
        sendKeys(webLocator, text, defaultWaitUntilTimeout);
    }

    /**
     * Wait for element located by <code>webLocator</code> to be visible with <code>timeout</code> then append <code>text</code> to this element.<br/><br/>
     * This method is controlled with retryPreviousAction enabled, that means if {@link TimeoutException} is thrown out when wait for element to be visible, it will retry with registered previous action.<br/><br/>
     * Method which is controlled with retryPreviousAction enabled is also registered as previousAction when it is completed.<br/><br/>
     * After web element is visible, this method will try {@link WebElement#sendKeys(CharSequence... text)} first, if there is any exception thrown out then it will retry with {@link Actions#sendKeys(WebElement webElement, CharSequence... text)}
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param text text to be appended to web element
     * @param timeout maximum time allowed to wait for web element to be visible
     */
    public static void sendKeys(WebLocator webLocator, String text, long timeout) {
        sendKeys(webLocator, text, timeout, true);
    }

    /**
     * Wait for element located by <code>webLocator</code> to be visible with <code>timeout</code> then append <code>text</code> to this element.<br/><br/>
     * This method is controlled with retryPreviousAction = <code>retryEnabled</code>.<br/><br/>
     * In case <code>retryEnabled</code> = true, if {@link TimeoutException} is thrown out when wait for element to be visible, it will retry with registered previous action.<br/>
     * Method which is controlled with retryPreviousAction enabled is also registered as previousAction when it is completed.<br/><br/>
     * In case <code>retryEnabled</code> = false, then none mentioned above is applied.<br/><br/>
     * After web element is visible, this method will try {@link WebElement#sendKeys(CharSequence... text)} first, if there is any exception thrown out then it will retry with {@link Actions#sendKeys(WebElement webElement, CharSequence... text)}
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param text text to be appended to web element
     * @param timeout maximum time allowed to wait for web element to be visible
     * @param retryEnabled control if this method is execute with retryPreviousAction enabled or not
     */
    public static void sendKeys(WebLocator webLocator, String text, Long timeout, Boolean retryEnabled) {
        sendKeys(webLocator, text, timeout.longValue(), retryEnabled.booleanValue());
    }

    /**
     * Wait for element located by <code>webLocator</code> to be visible with <code>timeout</code> then append <code>text</code> to this element.<br/><br/>
     * This method is controlled with retryPreviousAction = <code>retryEnabled</code>.<br/><br/>
     * In case <code>retryEnabled</code> = true, if {@link TimeoutException} is thrown out when wait for element to be visible, it will retry with registered previous action.<br/>
     * Method which is controlled with retryPreviousAction enabled is also registered as previousAction when it is completed.<br/><br/>
     * In case <code>retryEnabled</code> = false, then none mentioned above is applied.<br/><br/>
     * After web element is visible, this method will try {@link WebElement#sendKeys(CharSequence... text)} first, if there is any exception thrown out then it will retry with {@link Actions#sendKeys(WebElement webElement, CharSequence... text)}
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param text text to be appended to web element
     * @param timeout maximum time allowed to wait for web element to be visible
     * @param retryEnabled control if this method is execute with retryPreviousAction enabled or not
     */
    public static void sendKeys(WebLocator webLocator, String text, long timeout, boolean retryEnabled) {
        performActionWithRetry(retryEnabled, () -> {
            Actions builder = new Actions(webDriver);
            WebElement webElement = waitForElementVisible(webLocator, timeout);
            try {
                webElement.sendKeys(text);
            } catch (Exception ex) {
                LOGGER.error(String.format("WARNING: Cannot perform webElement.sendKeys() for element(%s), start retrying with Action.sendKeys().perform()", webElement.toString()));
                builder.sendKeys(webElement, text).perform();
            }
        }, "sendKeys", webLocator, text, timeout, false);
    }

    /**
     * Wait for element located by <code>webLocator</code> to be visible with timeout is {@link #defaultWaitUntilTimeout} then overwrite <code>text</code> to this element.<br/><br/>
     * This method is controlled with retryPreviousAction enabled, that means if {@link TimeoutException} is thrown out when wait for element to be visible, it will retry with registered previous action.<br/><br/>
     * Method which is controlled with retryPreviousAction enabled is also registered as previousAction when it is completed.<br/><br/>
     * After web element is visible, this method will call {@link WebElement#clear()}.<br/>
     * Then it will try {@link WebElement#sendKeys(CharSequence... text)} first, if there is any exception thrown out then it will retry with {@link Actions#sendKeys(WebElement webElement, CharSequence... text)}
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param text text to be overwritten to web element
     */
    public static void setText(WebLocator webLocator, String text) {
        setText(webLocator, text, defaultWaitUntilTimeout);
    }

    /**
     * Wait for element located by <code>webLocator</code> to be visible with <code>timeout</code> then overwrite <code>text</code> to this element.<br/><br/>
     * This method is controlled with retryPreviousAction enabled, that means if {@link TimeoutException} is thrown out when wait for element to be visible, it will retry with registered previous action.<br/><br/>
     * Method which is controlled with retryPreviousAction enabled is also registered as previousAction when it is completed.<br/><br/>
     * After web element is visible, this method will call {@link WebElement#clear()}.<br/>
     * Then it will try {@link WebElement#sendKeys(CharSequence... text)} first, if there is any exception thrown out then it will retry with {@link Actions#sendKeys(WebElement webElement, CharSequence... text)}
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param text text to be overwritten to web element
     * @param timeout maximum time allowed to wait for web element to be visible
     */
    public static void setText(WebLocator webLocator, String text, long timeout) {
        setText(webLocator, text, timeout, true);
    }

    /**
     * Wait for element located by <code>webLocator</code> to be visible with <code>timeout</code> then overwrite <code>text</code> to this element.<br/><br/>
     * This method is controlled with retryPreviousAction = <code>retryEnabled</code>.<br/><br/>
     * In case <code>retryEnabled</code> = true, if {@link TimeoutException} is thrown out when wait for element to be visible, it will retry with registered previous action.<br/>
     * Method which is controlled with retryPreviousAction enabled is also registered as previousAction when it is completed.<br/><br/>
     * In case <code>retryEnabled</code> = false, then none mentioned above is applied.<br/><br/>
     * After web element is visible, this method will call {@link WebElement#clear()}.<br/>
     * Then it will try {@link WebElement#sendKeys(CharSequence... text)} first, if there is any exception thrown out then it will retry with {@link Actions#sendKeys(WebElement webElement, CharSequence... text)}
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param text text to be overwritten to web element
     * @param timeout maximum time allowed to wait for web element to be visible
     * @param retryEnabled control if this method is execute with retryPreviousAction enabled or not
     */
    public static void setText(WebLocator webLocator, String text, Long timeout, Boolean retryEnabled) {
        setText(webLocator, text, timeout.longValue(), retryEnabled.booleanValue());
    }

    /**
     * Wait for element located by <code>webLocator</code> to be visible with <code>timeout</code> then overwrite <code>text</code> to this element.<br/><br/>
     * This method is controlled with retryPreviousAction = <code>retryEnabled</code>.<br/><br/>
     * In case <code>retryEnabled</code> = true, if {@link TimeoutException} is thrown out when wait for element to be visible, it will retry with registered previous action.<br/>
     * Method which is controlled with retryPreviousAction enabled is also registered as previousAction when it is completed.<br/><br/>
     * In case <code>retryEnabled</code> = false, then none mentioned above is applied.<br/><br/>
     * After web element is visible, this method will call {@link WebElement#clear()}.<br/>
     * Then it will try {@link WebElement#sendKeys(CharSequence... text)} first, if there is any exception thrown out then it will retry with {@link Actions#sendKeys(WebElement webElement, CharSequence... text)}
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param text text to be overwritten to web element
     * @param timeout maximum time allowed to wait for web element to be visible
     * @param retryEnabled control if this method is execute with retryPreviousAction enabled or not
     */
    public static void setText(WebLocator webLocator, String text, long timeout, boolean retryEnabled) {
        performActionWithRetry(retryEnabled, () -> {
            Actions builder = new Actions(webDriver);
            WebElement webElement = waitForElementVisible(webLocator, timeout);
            try {
                webElement.clear();
                webElement.sendKeys(text);
            } catch (Exception ex) {
                LOGGER.error(String.format("WARNING: Cannot perform webElement.sendKeys() for element(%s), start retrying with Action.sendKeys().perform()", webElement.toString()));
                builder.sendKeys(webElement, text).perform();
            }
        }, "setText", webLocator, text, timeout, false);
    }

    /**
     * Wait for element located by <code>webLocator</code> to be visible with timeout is {@link #defaultWaitUntilTimeout} then overwrite decrypted <code>text</code> to this element.<br/><br/>
     * This method is controlled with retryPreviousAction enabled, that means if {@link TimeoutException} is thrown out when wait for element to be visible, it will retry with registered previous action.<br/><br/>
     * Method which is controlled with retryPreviousAction enabled is also registered as previousAction when it is completed.<br/><br/>
     * After web element is visible, this method will call {@link WebElement#clear()}.<br/>
     * Then it will try {@link WebElement#sendKeys(CharSequence... decryptedText)} first, if there is any exception thrown out then it will retry with {@link Actions#sendKeys(WebElement webElement, CharSequence... decryptedText)}
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param text text to be decrypted by {@link #decryptStr(String text)} then overwritten to web element
     */
    public static void setEncryptedText(WebLocator webLocator, String text) {
        setEncryptedText(webLocator, text, defaultWaitUntilTimeout);
    }

    /**
     * Wait for element located by <code>webLocator</code> to be visible with timeout is <code>timeout</code> then overwrite decrypted <code>text</code> to this element.<br/><br/>
     * This method is controlled with retryPreviousAction enabled, that means if {@link TimeoutException} is thrown out when wait for element to be visible, it will retry with registered previous action.<br/><br/>
     * Method which is controlled with retryPreviousAction enabled is also registered as previousAction when it is completed.<br/><br/>
     * After web element is visible, this method will call {@link WebElement#clear()}.<br/>
     * Then it will try {@link WebElement#sendKeys(CharSequence... decryptedText)} first, if there is any exception thrown out then it will retry with {@link Actions#sendKeys(WebElement webElement, CharSequence... decryptedText)}
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param text text to be decrypted by {@link #decryptStr(String text)} then overwritten to web element
     * @param timeout maximum time allowed to wait for web element to be visible
     */
    public static void setEncryptedText(WebLocator webLocator, String text, long timeout) {
        setEncryptedText(webLocator, text, timeout, true);
    }

    /**
     * Wait for element located by <code>webLocator</code> to be visible with <code>timeout</code> then overwrite decrypted <code>text</code> to this element.<br/><br/>
     * This method is controlled with retryPreviousAction = <code>retryEnabled</code>.<br/><br/>
     * In case <code>retryEnabled</code> = true, if {@link TimeoutException} is thrown out when wait for element to be visible, it will retry with registered previous action.<br/>
     * Method which is controlled with retryPreviousAction enabled is also registered as previousAction when it is completed.<br/><br/>
     * In case <code>retryEnabled</code> = false, then none mentioned above is applied.<br/><br/>
     * After web element is visible, this method will call {@link WebElement#clear()}.<br/>
     * Then it will try {@link WebElement#sendKeys(CharSequence... decryptedText)} first, if there is any exception thrown out then it will retry with {@link Actions#sendKeys(WebElement webElement, CharSequence... decryptedText)}
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param text text to be decrypted by {@link #decryptStr(String text)} then overwritten to web element
     * @param timeout maximum time allowed to wait for web element to be visible
     * @param retryEnabled control if this method is execute with retryPreviousAction enabled or not
     */
    public static void setEncryptedText(WebLocator webLocator, String text, long timeout, boolean retryEnabled) {
        String decryptedText = decryptStr(text);
        setText(webLocator, decryptedText, timeout, retryEnabled);
    }

    /**
     * Wait for element located by <code>webLocator</code> to be clickable with timeout is {@link #defaultWaitUntilTimeout} then sleep for {@link SeleniumConstants#DEFAULT_SLEEP_THEN_ACTION_TIME} before trying to click on this element <br/><br/>
     * This method is controlled with retryPreviousAction enabled, that means if {@link TimeoutException} is thrown out when wait for element to be clickable, it will retry with registered previous action.<br/><br/>
     * Method which is controlled with retryPreviousAction enabled is also registered as previousAction when it is completed.<br/><br/>
     * After web element is clickable, this method will call {@link WebElement#click()} first.<br/>
     * If {@link ElementClickInterceptedException} is thrown out then it will retry by clicking on element repeatably on every {@link SeleniumConstants#DEFAULT_RETRY_ACTION_INTERVAL} seconds with timeout is {@link SeleniumConstants#DEFAULT_RETRY_ACTION_TIMEOUT}.<br/>
     * If still not success then it will perform last retry by using {@link Actions#click(WebElement webElement)}.
     * @param webLocator {@link WebLocator} instance to locate web element
     */
    public static void sleepThenClick(WebLocator webLocator) {
        sleepThenClick(webLocator, defaultWaitUntilTimeout);
    }

    /**
     * Wait for element located by <code>webLocator</code> to be clickable with timeout is <code>timeout</code> then sleep for {@link SeleniumConstants#DEFAULT_SLEEP_THEN_ACTION_TIME} before trying to click on this element <br/><br/>
     * This method is controlled with retryPreviousAction enabled, that means if {@link TimeoutException} is thrown out when wait for element to be clickable, it will retry with registered previous action.<br/><br/>
     * Method which is controlled with retryPreviousAction enabled is also registered as previousAction when it is completed.<br/><br/>
     * After web element is clickable, this method will call {@link WebElement#click()} first.<br/>
     * If {@link ElementClickInterceptedException} is thrown out then it will retry by clicking on element repeatably on every {@link SeleniumConstants#DEFAULT_RETRY_ACTION_INTERVAL} seconds with timeout is {@link SeleniumConstants#DEFAULT_RETRY_ACTION_TIMEOUT}.<br/>
     * If still not success then it will perform last retry by using {@link Actions#click(WebElement webElement)}.
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param timeout maximum time allowed to wait for web element to be clickable
     */
    public static void sleepThenClick(WebLocator webLocator, long timeout) {
        sleepThenClick(webLocator, timeout, true);
    }

    /**
     * Wait for element located by <code>webLocator</code> to be clickable with timeout is <code>timeout</code> then sleep for {@link SeleniumConstants#DEFAULT_SLEEP_THEN_ACTION_TIME} before trying to click on this element <br/><br/>
     * This method is controlled with retryPreviousAction = <code>retryEnabled</code>.<br/><br/>
     * In case <code>retryEnabled</code> = true, if {@link TimeoutException} is thrown out when wait for element to be clickable, it will retry with registered previous action.<br/>
     * Method which is controlled with retryPreviousAction enabled is also registered as previousAction when it is completed.<br/><br/>
     * In case <code>retryEnabled</code> = false, then none mentioned above is applied.<br/><br/>
     * After web element is clickable, this method will call {@link WebElement#click()} first.<br/>
     * If {@link ElementClickInterceptedException} is thrown out then it will retry by clicking on element repeatably on every {@link SeleniumConstants#DEFAULT_RETRY_ACTION_INTERVAL} seconds with timeout is {@link SeleniumConstants#DEFAULT_RETRY_ACTION_TIMEOUT}.<br/>
     * If still not success then it will perform last retry by using {@link Actions#click(WebElement webElement)}.
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param timeout maximum time allowed to wait for web element to be clickable
     * @param retryEnabled control if this method is execute with retryPreviousAction enabled or not
     */
    public static void sleepThenClick(WebLocator webLocator, Long timeout, Boolean retryEnabled) {
        sleepThenClick(webLocator, timeout.longValue(), retryEnabled.booleanValue());
    }

    /**
     * Wait for element located by <code>webLocator</code> to be clickable with timeout is <code>timeout</code> then sleep for {@link SeleniumConstants#DEFAULT_SLEEP_THEN_ACTION_TIME} before trying to click on this element <br/><br/>
     * This method is controlled with retryPreviousAction = <code>retryEnabled</code>.<br/><br/>
     * In case <code>retryEnabled</code> = true, if {@link TimeoutException} is thrown out when wait for element to be clickable, it will retry with registered previous action.<br/>
     * Method which is controlled with retryPreviousAction enabled is also registered as previousAction when it is completed.<br/><br/>
     * In case <code>retryEnabled</code> = false, then none mentioned above is applied.<br/><br/>
     * After web element is clickable, this method will call {@link WebElement#click()} first.<br/>
     * If {@link ElementClickInterceptedException} is thrown out then it will retry by clicking on element repeatably on every {@link SeleniumConstants#DEFAULT_RETRY_ACTION_INTERVAL} seconds with timeout is {@link SeleniumConstants#DEFAULT_RETRY_ACTION_TIMEOUT}.<br/>
     * If still not success then it will perform last retry by using {@link Actions#click(WebElement webElement)}.
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param timeout maximum time allowed to wait for web element to be clickable
     * @param retryEnabled control if this method is execute with retryPreviousAction enabled or not
     */
    public static void sleepThenClick(WebLocator webLocator, long timeout, boolean retryEnabled) {
        performActionWithRetry(retryEnabled, () -> {
            WebElement webElement = waitForElementClickable(webLocator, timeout);
            try {
                Thread.sleep(DEFAULT_SLEEP_THEN_ACTION_TIME * 1000L);
            } catch (InterruptedException e) {
                LOGGER.error("InterruptedException", e);
            }
            performClick(webElement);
        }, "sleepThenClick", webLocator, timeout, false);
    }

    /**
     * Wait for element located by <code>webLocator</code> to be clickable with timeout is {@link #defaultWaitUntilTimeout} then try to click on this element <br/><br/>
     * This method is controlled with retryPreviousAction enabled, that means if {@link TimeoutException} is thrown out when wait for element to be clickable, it will retry with registered previous action.<br/><br/>
     * Method which is controlled with retryPreviousAction enabled is also registered as previousAction when it is completed.<br/><br/>
     * After web element is clickable, this method will call {@link WebElement#click()} first.<br/>
     * If {@link ElementClickInterceptedException} is thrown out then it will retry by clicking on element repeatably on every {@link SeleniumConstants#DEFAULT_RETRY_ACTION_INTERVAL} seconds with timeout is {@link SeleniumConstants#DEFAULT_RETRY_ACTION_TIMEOUT}.<br/>
     * If still not success then it will perform last retry by using {@link Actions#click(WebElement webElement)}.
     * @param webLocator {@link WebLocator} instance to locate web element
     */
    public static void click(WebLocator webLocator) {
        click(webLocator, defaultWaitUntilTimeout);
    }

    /**
     * Wait for element located by <code>webLocator</code> to be clickable with timeout is <code>timeout</code> then try to click on this element <br/><br/>
     * This method is controlled with retryPreviousAction enabled, that means if {@link TimeoutException} is thrown out when wait for element to be clickable, it will retry with registered previous action.<br/><br/>
     * Method which is controlled with retryPreviousAction enabled is also registered as previousAction when it is completed.<br/><br/>
     * After web element is clickable, this method will call {@link WebElement#click()} first.<br/>
     * If {@link ElementClickInterceptedException} is thrown out then it will retry by clicking on element repeatably on every {@link SeleniumConstants#DEFAULT_RETRY_ACTION_INTERVAL} seconds with timeout is {@link SeleniumConstants#DEFAULT_RETRY_ACTION_TIMEOUT}.<br/>
     * If still not success then it will perform last retry by using {@link Actions#click(WebElement webElement)}.
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param timeout maximum time allowed to wait for web element to be clickable
     */
    public static void click(WebLocator webLocator, long timeout) {
        click(webLocator, timeout, true);
    }

    /**
     * Wait for element located by <code>webLocator</code> to be clickable with timeout is <code>timeout</code> then try to click on this element <br/><br/>
     * This method is controlled with retryPreviousAction = <code>retryEnabled</code>.<br/><br/>
     * In case <code>retryEnabled</code> = true, if {@link TimeoutException} is thrown out when wait for element to be clickable, it will retry with registered previous action.<br/>
     * Method which is controlled with retryPreviousAction enabled is also registered as previousAction when it is completed.<br/><br/>
     * In case <code>retryEnabled</code> = false, then none mentioned above is applied.<br/><br/>
     * After web element is clickable, this method will call {@link WebElement#click()} first.<br/>
     * If {@link ElementClickInterceptedException} is thrown out then it will retry by clicking on element repeatably on every {@link SeleniumConstants#DEFAULT_RETRY_ACTION_INTERVAL} seconds with timeout is {@link SeleniumConstants#DEFAULT_RETRY_ACTION_TIMEOUT}.<br/>
     * If still not success then it will perform last retry by using {@link Actions#click(WebElement webElement)}.
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param timeout maximum time allowed to wait for web element to be clickable
     * @param retryEnabled control if this method is execute with retryPreviousAction enabled or not
     */
    public static void click(WebLocator webLocator, Long timeout, Boolean retryEnabled) {
        click(webLocator, timeout.longValue(), retryEnabled.booleanValue());
    }

    /**
     * Wait for element located by <code>webLocator</code> to be clickable with timeout is <code>timeout</code> then try to click on this element <br/><br/>
     * This method is controlled with retryPreviousAction = <code>retryEnabled</code>.<br/><br/>
     * In case <code>retryEnabled</code> = true, if {@link TimeoutException} is thrown out when wait for element to be clickable, it will retry with registered previous action.<br/>
     * Method which is controlled with retryPreviousAction enabled is also registered as previousAction when it is completed.<br/><br/>
     * In case <code>retryEnabled</code> = false, then none mentioned above is applied.<br/><br/>
     * After web element is clickable, this method will call {@link WebElement#click()} first.<br/>
     * If {@link ElementClickInterceptedException} is thrown out then it will retry by clicking on element repeatably on every {@link SeleniumConstants#DEFAULT_RETRY_ACTION_INTERVAL} seconds with timeout is {@link SeleniumConstants#DEFAULT_RETRY_ACTION_TIMEOUT}.<br/>
     * If still not success then it will perform last retry by using {@link Actions#click(WebElement webElement)}.
     * @param webLocator {@link WebLocator} instance to locate web element
     * @param timeout maximum time allowed to wait for web element to be clickable
     * @param retryEnabled control if this method is execute with retryPreviousAction enabled or not
     */
    public static void click(WebLocator webLocator, long timeout, boolean retryEnabled) {
        performActionWithRetry(retryEnabled, () -> {
            WebElement webElement = waitForElementClickable(webLocator, timeout);
            performClick(webElement);
        }, "click", webLocator, timeout, false);
    }
    /*End of retry enabled actions in Selenium WebDriver*/

    /**
     * Wait until <code>message</code> to be visible on the screen with timeout is {@link #defaultWaitUntilTimeout}.<br/><br/>
     * {@link TimeoutException} is thrown out if this doesn't happen
     * @param message message to be verified to be visible on the screen
     */
    public static void verifyMessageAppear(String message) {
        verifyMessageAppear(message, defaultWaitUntilTimeout);
    }

    /**
     * Wait until <code>message</code> to be visible on the screen with <code>timeout</code>.<br/><br/>
     * {@link TimeoutException} is thrown out if this doesn't happen
     * @param message message to be verified to be visible on the screen
     * @param timeout maximum time allowed to wait for <code>message</code> to be visible
     */
    public static void verifyMessageAppear(String message, long timeout) {
        waitForElementVisible(new WebLocator().innerText(message), timeout);
    }

    public static void initializeNewSession() {
        switch (selectWebBrowser) {
            case "chrome":
                webDriver = new ChromeDriver();
                break;
            case "firefox":
                webDriver = new FirefoxDriver();
                break;
            case "edge":
                webDriver = new EdgeDriver();
                break;
//            case "safari":
//                webDriver = new SafariDriver();
//                break;
            default:
                throw new RuntimeException(String.format("Browser %s is not supported", selectWebBrowser));
        }
    }

    public static void quitSession() {
        webDriver.quit();
        webDriver = null;
    }

    public static WebDriver getWebDriver() {
        return webDriver;
    }

    public static String getSelectWebBrowser() {
        return selectWebBrowser;
    }

    public static void setSelectWebBrowser(String selectWebBrowser) {
        WebUI.selectWebBrowser = selectWebBrowser;
    }

    public static long getDefaultWaitUntilTimeout() {
        return defaultWaitUntilTimeout;
    }

    public static void setDefaultWaitUntilTimeout(long defaultWaitUntilTimeout) {
        WebUI.defaultWaitUntilTimeout = defaultWaitUntilTimeout;
    }

    private static void performClick(WebElement webElement) {
        Actions builder = new Actions(webDriver);
        try {
            webElement.click();
        } catch (ElementClickInterceptedException ex) {
            LOGGER.error(String.format("WARNING: ElementClickInterceptedException appeared, likely that other element has covered the clicking element (%s), start perform retrying"
                    , webElement.toString()));

            // Retry click until no more ElementClickInterceptedException
            long currentTimestamp = Calendar.getInstance().getTimeInMillis();
            boolean isSuccess = false;
            while ((Calendar.getInstance().getTimeInMillis() - currentTimestamp) <= SeleniumConstants.DEFAULT_RETRY_ACTION_TIMEOUT * 1000L) {
                try {
                    Thread.sleep(SeleniumConstants.DEFAULT_RETRY_ACTION_INTERVAL * 1000L);
                    webElement.click();
                    isSuccess = true;
                } catch (ElementClickInterceptedException e) {
                    // do nothing
                } catch (InterruptedException e) {
                    LOGGER.error("InterruptedException", e);
                }
                if (isSuccess) {
                    break;
                }
            }
            if (!isSuccess) {
                builder.click(webElement).perform();
            }
        } catch (Exception ex) {
            LOGGER.error(String.format("WARNING: Cannot perform webElement.click() for element(%s), start retrying with Action.click().perform()", webElement.toString()));
            builder.click(webElement).perform();
        }
    }

    private static void performActionWithRetry(boolean retryEnabled, Function function, String methodName, Object... currentParams) {
        Method currentAction = null;
        try {
            Class[] currentParamClasses = new Class[currentParams.length];
            for (int i = 0; i < currentParams.length; i++) {
                currentParamClasses[i] = currentParams[i].getClass();
            }
            currentAction = WebUI.class.getMethod(methodName, currentParamClasses);
            function.apply();
        } catch (NoSuchMethodException nme) {
            LOGGER.error("NoSuchMethodException", nme);
        } catch (TimeoutException te) {
            if (retryEnabled) {
                LOGGER.error("TimeoutException", te);
                retryPreviousAction(currentAction
                        , currentParams
                        , previousAction
                        , previousActionParams);
            } else {
                throw te;
            }
        }
        if (retryEnabled) {
            previousAction = currentAction;
            previousActionParams = currentParams;
        }
    }

    private static void retryPreviousAction(Method currentAction, Object[] currentParams, Method previousAction, Object[] previousParams) {
        LOGGER.info(String.format("Start retry previous action with currentAction: %s, currentParams: %s, previousAction: %s, previousParams: %s"
                , currentAction.toGenericString(), Arrays.asList(currentParams).toString(), previousAction.toGenericString(), Arrays.asList(previousParams).toString()));
        long currentTimestamp = Calendar.getInstance().getTimeInMillis();
        boolean isSuccess = false;
        Exception lastException = new RuntimeException("Something wrong");
        while ((Calendar.getInstance().getTimeInMillis() - currentTimestamp) <= SeleniumConstants.DEFAULT_RETRY_ACTION_TIMEOUT * 1000L) {
            try {
                Thread.sleep(SeleniumConstants.DEFAULT_RETRY_ACTION_INTERVAL * 1000L);
                previousAction.invoke(null, previousParams);
                currentAction.invoke(null, currentParams);
                isSuccess = true;
            } catch (Exception e) {
                lastException = e;
            }
            if (isSuccess) {
                return;
            }
        }
        throw new RuntimeException(lastException);
    }

    private static void executeJavascript(String script) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        LOGGER.info(script);
        js.executeScript(script);
    }

//    public static void scrollBy(String x, String y) {
//        JavascriptExecutor js = (JavascriptExecutor) webDriver;
//        String script = "window.scrollBy(%s, %s)";
//        js.executeScript(String.format(script, x, y));
//        sleepThread(2);
//    }
//
//    public static void scrollBy(int x, int y) {
//        scrollBy(x + "", y + "");
//    }
//
//    public static void scrollToBottom() {
//        scrollBy("0", "document.body.scrollHeight");
//    }

//    public static void moveMouseThenClick(WebLocator webLocator) {
//        Actions builder = new Actions(webDriver);
//        WebElement webElement = waitForElementClickable(webLocator, SeleniumConstants.DEFAULT_WAIT_UNTIL_TIMEOUT);
//        builder.moveToElement(webElement).click().perform();
//    }

//    public static void webElementClick(WebLocator webLocator) {
//        webElementClick(webLocator, SeleniumConstants.DEFAULT_WAIT_UNTIL_TIMEOUT);
//    }
//
//    public static void webElementClick(WebLocator webLocator, long timeout) {
//        WebElement webElement = waitForElementClickable(webLocator, timeout);
//        webElement.click();
//    }
//
//    public static void webElementSetText(WebLocator webLocator, String text) {
//        webElementSetText(webLocator, text, DEFAULT_WAIT_UNTIL_TIMEOUT);
//    }
//
//    public static void webElementSetText(WebLocator webLocator, String text, long timeout) {
//        WebElement webElement = waitForElementVisible(webLocator, timeout);
//        webElement.sendKeys(text);
//    }

    public static void sleepThread(long sleepTimeInSeconds) {
        try {
            Thread.sleep(sleepTimeInSeconds * 1000L);
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException", e);
        }
    }

    /**
     * Take screenshot of current screen and store the picture in designated folder.<br/><br/>
     * For example if testCaseName is testXyz then the picture with suffix of timestamp will be in <code>screenshots/testXyz</code> folder
     * @param testCaseName directory name for screenshot picture to be stored
     */
    public static void takeScreenshot(ITestContext testContext, String testCaseName){
//        Screenshot screenshot=new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(webDriver);
        File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        File parentDir = new File(testContext.getOutputDirectory() + "/screenshots/" + testCaseName);
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        try {
            File outputFile = new File(parentDir, String.format("%s.png", DateTimeUtil.obtainCurrentDateTimeInMiliSecond()));
//            ImageIO.write(screenshot.getImage(), "PNG", outputFile);
            Files.copy(file.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.error("IOException", e);
            throw new RuntimeException("Cannot copy screenshot to configured destination folder");
        }
    }

    private static String decryptStr(String encryptedStr) {
        byte[] decodedByteArr = Base64.getDecoder().decode(encryptedStr.getBytes());
        return new String(decodedByteArr);
    }

    private interface Function {
        void apply();
    }

    public static class DefaultRetryControl implements IRetryAnalyzer {
        private static Map<String, Integer> testMethod2NumOfRetryMap = new HashMap<>();

        @Override
        public boolean retry(ITestResult result) {
            String testMethod = result.getTestClass() + "." + result.getMethod().getMethodName();
            Integer currentNumOfRetry = 1;
            if (testMethod2NumOfRetryMap.containsKey(testMethod)) {
                currentNumOfRetry = testMethod2NumOfRetryMap.get(testMethod) + 1;
            }
            testMethod2NumOfRetryMap.put(testMethod, currentNumOfRetry);

            if (currentNumOfRetry <= SeleniumConstants.DEFAULT_TEST_CASE_RETRY_NUM) {
                return true;
            } else {
                return false;
            }
        }
    }
}
