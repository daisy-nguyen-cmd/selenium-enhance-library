set today=%DATE:~10,4%%DATE:~7,2%%DATE:~4,2%
java -cp lib/* -DselectedWebBrowser=chrome -DdefaultWaitUntilTimeout=20 org.testng.TestNG testng.xml -d chrome-testoutput-%today%
./killWebDriver.bat