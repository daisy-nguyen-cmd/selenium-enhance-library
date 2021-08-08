***Selenium enhanced library***<br/>
**1, Summary**<br/>
This library is to help automation test engineer to write selenium test script more efficiently, by providing a wrapping level (```WebUI``` class) to Selenium API.<br/>

For example, instead of calling ```WebElement.click()``` directly from Selenium, ```WebUI.click()``` enhances with self healing mechanism, you can check Java doc at ```WebUI.click()``` method for more details

You still have the access to Selenium API, just by calling ```WebUI.getWebDriver()``` 

**2, How to use**<br/>
 Get the code base then you can start making your own test case immediately, take a look at ```SampleTestCase``` class for more details.<br/>
 
 Testing with chrome driver v83 is supported out of the box (that means when you test in your local machine, chrome's version should be matched - also 83).<br/>
 
 To use different chrome version or other browser, just add/update in Common.properties with key is Selenium defined driver key and value is the relative file path (Your driver must be in ```resources``` folder), for ex:
 - ```webdriver.chrome.driver=seleniumdriver/chromedriver_83/chromedriver.exe``` 
 
 The code base also supports some basic utilities, such as:
 - Logging
 - Packaging and window bat scripts (So can be run in terminal out of the box after build)
 - Multi profiles support:
    + Currently there are 3 profiles supported: ```dev```, ```int``` and ```prod```. Default profile is ```int```
    + If you wish to run test script with other profile, for ex: ```dev```, then run ```mvn clean install -Pdev``` first before running the test case
 - Properties loading in following order(The first is lowest priority):
    + Common.properties
    + ```Test case name```.properties (ex: SampleTestCase.properties)
    + Test case parameters defined in testng.xml
    + Properties in testng ```ITestContext``` (which could be get/set programmatically) 
  
 