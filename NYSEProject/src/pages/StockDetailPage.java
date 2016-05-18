package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class StockDetailPage extends BasePages {
	
	By companyName = By.xpath("//*[@id='companyheader']/div[1]/h3");
	String currNYSECode = "//*[@id='companyheader']/div[1]/h3[contains(following-sibling::text(), '$nysecode$')]";
	By currentPrice = By.xpath("//div[@id='price-panel']//span[@class='pr']");
	By weekLowHigh52 = By.xpath("//tr[td[@data-snapfield='range_52week']]/td[@class='val']");
	By eps = By.xpath("//tr[td[@data-snapfield='eps']]/td[@class='val']");
	
	public StockDetailPage(RemoteWebDriver driver) {
		super(driver);
		// validate that we are on the Stock Detail Page by looking for the "Company header element".
		try {
			driver.findElement(companyName);
		} catch (NoSuchElementException e) {
			Assert.fail("Stock Detail Page not loaded\n"+e.toString());
		}
	}
	
   public StockDetailPage verifyStockDetailsLoaded(String code)
   {
	   currNYSECode = currNYSECode.replace("$nysecode$", code);
	   // validate that we are on the Stock Detail for queried code
	   try {
			driver.findElement(By.xpath(currNYSECode));
		} catch (NoSuchElementException e) {
			Assert.fail("Stock Detail for the company code '"+code+"' seems to be not loaded\n"+e.toString());
		}
	   
	   return new StockDetailPage(driver);
   }

   public String getCompanyName()
   {
	   return driver.findElement(companyName).getText().trim();
   }
	
   public String getCurrentPrice()
   {
	   return driver.findElement(currentPrice).getText().trim();
   }
   
	private String get52WeekHighLow()
	{
		return driver.findElement(weekLowHigh52).getText().trim();
	}
	
	public String get52WeekHigh()
	{
		return get52WeekHighLow().split("-")[1].trim();
	}
	
	public String get52WeekLow()
	{
		return get52WeekHighLow().split("-")[0].trim();
	}
	
	public String getEPS()
	{
		return driver.findElement(eps).getText().trim();
	}
	

	
}
