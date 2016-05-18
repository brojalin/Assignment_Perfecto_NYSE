package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class MainPage extends BasePages {
	//By searchTextView = By.xpath("//android.widget.EditText[@resource-id='com.google.android.apps.maps:id/search_omnibox_text_box']/android.widget.TextView");
	By searchTextbox = By.name("q");
	By searchButton = By.xpath("//input[@value='Get quotes']");
	
	
	public MainPage(RemoteWebDriver driver) {
		super(driver);
		// validate that we are on the Main page by looking for the "Search query" textbox.
		try {
			driver.findElement(searchTextbox);
		} catch (NoSuchElementException e) {
			Assert.fail("Main page not loaded\n"+e.toString());
		}
	}

	public MainPage enterStockCode(String code)
	{
		driver.findElement(searchTextbox).clear();
		driver.findElement(searchTextbox).sendKeys(code);
		return new MainPage(driver);
	} 
	
	public StockDetailPage clickSearchButton()
	{
		driver.findElement(searchButton).click();
		return new StockDetailPage(driver);
	} 
}
