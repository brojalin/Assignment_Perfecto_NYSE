package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class BasePages {

	protected final RemoteWebDriver driver;
	

	// constructor verifies that the page objects have pointer to the driver instance
	public BasePages(RemoteWebDriver driver) {
		this.driver = driver;
	}



}
