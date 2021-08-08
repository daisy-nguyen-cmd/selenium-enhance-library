package org.my.automationtest.locator.helper;

import org.my.automationtest.locator.WebLocator;
import org.my.automationtest.service.WebUI;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class DivTableHelper {
    public static WebLocator findByColumnAndRows(WebLocator colLocator, WebLocator rowLocator, WebLocator relativeElementLocator, int numberOfColumns) {
        WebElement colElement = WebUI.waitForElementVisible(colLocator);
        WebElement rowElement = WebUI.waitForElementVisible(rowLocator);
        String columnName = colElement.getText();
        String rowName = rowElement.getText();
        final int realNumOfCols = numberOfColumns; //real number of columns in DOM

        WebDriver webDriver = WebUI.getWebDriver();
        List<WebElement> allDivsInTable = webDriver.findElements(By.xpath(String.format("//div[div/text()='%s']/div", columnName)));
        if (allDivsInTable == null || allDivsInTable.isEmpty() || allDivsInTable.size() < (realNumOfCols)) {
            throw new RuntimeException(String.format("Cannot find cell in table with columnName = %s, rowName = %s, numberOfColumns = %d", columnName, rowName, realNumOfCols));
        }

        List<WebElement> allColumnDivs = allDivsInTable.subList(0, realNumOfCols);
        int indexOfFindingCol = -1;
        boolean indexOfFindingColFound = false;
        for (WebElement we : allColumnDivs) {
            indexOfFindingCol++;
            if (colElement.getLocation().equals(we.getLocation())) {
                indexOfFindingColFound = true;
                break;
            }
        }
        if (!indexOfFindingColFound) {
            throw new RuntimeException(String.format("Cannot find columnName: %s in table", columnName));
        }

        List<WebElement> allFirstDivsOfRows = allDivsInTable.stream().filter(e -> (allDivsInTable.indexOf(e) % realNumOfCols) == 0).collect(Collectors.toList());
        int indexOfFindingRow = -1;
        boolean indexOfFindingRowFound = false;
        for (WebElement we : allFirstDivsOfRows) {
            indexOfFindingRow++;
            if (rowElement.getLocation().equals(we.getLocation())) {
                indexOfFindingRowFound = true;
                break;
            }
        }
        if (!indexOfFindingRowFound) {
            throw new RuntimeException(String.format("Cannot find rowName: %s in table", rowName));
        }

        int indexOfFindingCell = indexOfFindingRow * numberOfColumns + indexOfFindingCol + 1; //xpath count start from 1, not 0
        return new WebLocator().xpath(String.format("//div[div/text()='%s']/div[%d]%s", columnName, indexOfFindingCell, relativeElementLocator.toXpath()));
    }

    public static WebLocator findByColumnAndRowNames(String columnName, String rowName, WebLocator relativeElementLocator, int numberOfColumns) {
        WebUI.waitForElementVisible(new WebLocator().innerText(columnName));
        final int realNumOfCols = numberOfColumns; //real number of columns in DOM

        WebDriver webDriver = WebUI.getWebDriver();
        List<WebElement> allDivsInTable = webDriver.findElements(By.xpath(String.format("//div[div/text()='%s']/div", columnName)));
        if (allDivsInTable == null || allDivsInTable.isEmpty() || allDivsInTable.size() < (realNumOfCols)) {
            throw new RuntimeException(String.format("Cannot find cell in table with columnName = %s, rowName = %s, numberOfColumns = %d", columnName, rowName, realNumOfCols));
        }

        List<WebElement> allColumnDivs = allDivsInTable.subList(0, realNumOfCols);
        int indexOfFindingCol = -1;
        boolean indexOfFindingColFound = false;
        for (WebElement we : allColumnDivs) {
            indexOfFindingCol++;
            if (columnName.equals(we.getText())) {
                indexOfFindingColFound = true;
                break;
            }
        }
        if (!indexOfFindingColFound) {
            throw new RuntimeException(String.format("Cannot find columnName: %s in table", columnName));
        }

        List<WebElement> allFirstDivsOfRows = allDivsInTable.stream().filter(e -> (allDivsInTable.indexOf(e) % realNumOfCols) == 0).collect(Collectors.toList());
        int indexOfFindingRow = -1;
        boolean indexOfFindingRowFound = false;
        for (WebElement we : allFirstDivsOfRows) {
            indexOfFindingRow++;
            if (rowName.equals(we.getText())) {
                indexOfFindingRowFound = true;
                break;
            }
        }
        if (!indexOfFindingRowFound) {
            throw new RuntimeException(String.format("Cannot find rowName: %s in table", rowName));
        }

        int indexOfFindingCell = indexOfFindingRow * numberOfColumns + indexOfFindingCol + 1; //xpath count start from 1, not 0
        return new WebLocator().xpath(String.format("//div[div/text()='%s']/div[%d]%s", columnName, indexOfFindingCell, relativeElementLocator.toXpath()));
    }
}
