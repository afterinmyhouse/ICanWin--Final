package org.automation;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;


public class Browser implements WrapsDriver {
  private static Browser instance;
  private static WebDriver driver;
  private static Wait<WebDriver> wait;
  private Browser() {
    System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
    DesiredCapabilities dc = new DesiredCapabilities();
    dc.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
    driver = new ChromeDriver();
    driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    wait = new WebDriverWait(driver, 30, 500)
        .withMessage("Element was not found in X seconds");
  }

  public static Browser getInstance() {
    if (instance == null || driver == null) {
      instance = new Browser();
    }
    return instance;
  }
  public WebDriver getWrappedDriver() {
    return driver;
  }

  public void stopBrowser() {
    try {
      getInstance().getWrappedDriver().quit();
      getInstance().driver = null;
      instance = null;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void open(String url) {
    driver.get(url);
  }
  public WebElement highlightElement(WebElement element) {
    if (driver instanceof JavascriptExecutor) {
      ((JavascriptExecutor) driver)
          .executeScript("arguments[0].style.border='3px solid red'", element);
    }
    return element;
  }


  public WebElement waitVisible(String xpathLocator) {
    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathLocator)));
    WebElement el = wait
        .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathLocator)));
    el = highlightElement(el);
    return el;
  }
}
