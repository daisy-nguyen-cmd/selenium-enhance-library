package org.my.automationtest.constants;

import org.my.automationtest.utils.FileUtil;

public class SeleniumConstants {

	public static final long DEFAULT_SLEEP_THEN_ACTION_TIME = 1; // in seconds
	public static final long DEFAULT_RETRY_ACTION_TIMEOUT = 20; // in seconds
	public static final long DEFAULT_RETRY_ACTION_INTERVAL = 1; // in second
	public static final long DEFAULT_TEST_CASE_RETRY_NUM = 2; // in second

	public static final String NON_BREAKING_SPACE = "\u00A0";

	public static final String JS_FIND_ELEMENT_BY_XPATH = "document.evaluate(\"%s\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue";

	public static final String SELECTED_WEB_BROWSER_KEY = "selectedWebBrowser";
	public static final String DEFAULT_WAIT_UNTIL_TIMEOUT_KEY = "defaultWaitUntilTimeout";
}
