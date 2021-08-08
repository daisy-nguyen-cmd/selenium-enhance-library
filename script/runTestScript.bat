set today=%DATE:~10,4%%DATE:~7,2%%DATE:~4,2%
java -cp lib/* -DselectedWebBrowser=chrome -DdefaultWaitUntilTimeout=300 org.testng.TestNG testng.xml -d chrome-testoutput-%today%
java -cp lib/* -DselectedWebBrowser=edge -DdefaultWaitUntilTimeout=300 org.testng.TestNG testng.xml -d edge-testoutput-%today%
java -cp lib/* -DselectedWebBrowser=firefox -DdefaultWaitUntilTimeout=300 org.testng.TestNG testng.xml -d firefox-testoutput-%today%
./killWebDriver.bat