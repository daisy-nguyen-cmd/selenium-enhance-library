set today=%DATE:~10,4%%DATE:~7,2%%DATE:~4,2%
java -cp lib/* -DselectedWebBrowser=edge -DdefaultWaitUntilTimeout=300 org.testng.TestNG testng.xml -d edge-testoutput-%today%
./killWebDriver.bat