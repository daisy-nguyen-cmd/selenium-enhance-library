package org.my.automationtest.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class FileUtil {

    public static String obtainAbsolutePathFromURL(URL url) {
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public static final String obtainResourcesDirPath() {
        File testNgFile = new File("testng.xml");
        if (testNgFile.exists()) {
            return ".";
        } else {
            return "src/main/resources";
        }
    }
}
