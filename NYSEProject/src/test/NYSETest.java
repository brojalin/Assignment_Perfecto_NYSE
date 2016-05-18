package test;


import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.perfectomobile.selenium.util.EclipseConnector;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import pages.MainPage;
import pages.StockDetailPage;

import util.ExcelReader;
import util.PerfectoLabUtils;
import util.ProjectCommonUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;



public class NYSETest {
	
	private RemoteWebDriver driver;
	private DesiredCapabilities capabilities;
	private String appURL;
	private String reportPath;
	
	 @Test(dataProvider="nyseInput")
	 public void compareNYSEStocks(String code1, String code2) throws MalformedURLException {
	   
		launchAppURL();
		
		StockDetailPage stock1 = new MainPage(driver).enterStockCode(code1).clickSearchButton();
		
		String companyName1 = stock1.verifyStockDetailsLoaded(code1).getCompanyName();
		String currentPrice1 = stock1.getCurrentPrice();
		String w52High1 = stock1.get52WeekHigh();
		String w52Low1 = stock1.get52WeekLow();
		String eps1 = stock1.getEPS();
     	
		String consoleOut = "********"+ProjectCommonUtil.compareCurrntPriceAnd52Week(companyName1, code1, currentPrice1, w52Low1, w52High1);
		Reporter.log(consoleOut);
		System.out.println(consoleOut);
		
		
		StockDetailPage stock2 = new MainPage(driver).enterStockCode(code2).clickSearchButton();
		String companyName2 = stock2.verifyStockDetailsLoaded(code2).getCompanyName();
		String eps2 = stock2.getEPS();
		
		consoleOut = "********"+ProjectCommonUtil.compareEPS(companyName1, code1, companyName2, code2, eps1, eps2);
		Reporter.log(consoleOut);
		System.out.println(consoleOut);
     	     	     	
	 }
	   
	 	 
	   @Parameters({ "host", "user" , "password", "persona", "platformName", "manufacturer", "model", "appURL"})
	   @BeforeMethod 
	   public void beforeClass(String host, String user, String password, String persona, String platformName, String manufacturer, String model, String appURL) throws IOException{
		   System.out.println("Run started");

	        String browserName = "mobileOS";
	        capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
	       
	        capabilities.setCapability("user", user);
	        capabilities.setCapability("password", password);	       
	  
	        // Framework to be used
	        capabilities.setCapability("automationName", "Appium");
	        
	        //Device settings
	        capabilities.setCapability("platformName", platformName);	        
	        capabilities.setCapability("manufacturer", manufacturer);
	        capabilities.setCapability("model", model);
	        
	       // capabilities.setCapability("deviceName", "92BDC4BC0F439F99CB5FA7F80E255E5519BAC021");
	        
	        // Call this method if you want the script to share the devices with the Perfecto Lab plugin.
	        setExecutionIdCapability(capabilities, host);
	
	        capabilities.setCapability("windTunnelPersona", persona);
	        
	        // Name your script
	        capabilities.setCapability("scriptName", "NYSE");

  
            
	        driver = new RemoteWebDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
	        
	        //driver = new IOSDriver<WebElement>(new java.net.URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
		    driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
				 
			setReportPath(getDeviceID());			
			this.appURL = appURL;
	       
	   }



	@AfterMethod
	   public void afterClass() {
	     try{
	         // Close the browser
	         driver.close();
	             
	         // Download a pdf version of the execution report
	         //PerfectoLabUtils.downloadReport(driver, "pdf", "C:\\temp\\report.pdf");
	         PerfectoLabUtils.downloadReport(driver, "pdf", reportPath+"\\report.pdf");
	         }
	         catch(Exception e){
	             e.printStackTrace();
	         }
	     driver.quit();
	   }

	   
	   @DataProvider(name="nyseInput")
		public Iterator<Object[]> nyseInputDataWorkbook() throws FileNotFoundException, IOException {	
			
			String filePath = "input/NYSEInput.xlsx";
			String sheetName = "NYSEInputData";
			ExcelReader exrd = new ExcelReader(filePath);
			ArrayList<Object[]> da = new ArrayList<Object[]>();
			
			for(int i=1; i<exrd.getRowCounts(sheetName)+1; i++)
			{
				Map<String, String> rowData = exrd.getSheetData(i, sheetName);
				if(rowData.get("RunFlag").equalsIgnoreCase("ON"))
				{
					if(!rowData.get("StockCode1").isEmpty() && !rowData.get("StockCode2").isEmpty() )
						da.add(new Object[]{(Object)"NYSE:"+rowData.get("StockCode1").trim(), (Object)"NYSE:"+rowData.get("StockCode2").trim()});
				}
			}
			
			return da.iterator();	
		}
	   
	   
	    private static void setExecutionIdCapability(DesiredCapabilities capabilities, String host) throws IOException  {
	        EclipseConnector connector = new EclipseConnector();
	        String eclipseHost = connector.getHost();
	        if ((eclipseHost == null) || (eclipseHost.equalsIgnoreCase(host))) {
	            String executionId = connector.getExecutionId();
	            capabilities.setCapability(EclipseConnector.ECLIPSE_EXECUTION_ID, executionId);
	        }
	    }
	    

	    
	    
	   private void setReportPath(String deviceID) {
		   reportPath = System.getProperty("user.dir")+"\\reports\\DeviceID-"+deviceID+"_TimeStamp-"+ProjectCommonUtil.getTimeStamp();
	        new File(reportPath).mkdirs();	
	   }
	    
	   private String getDeviceID()
	   {
		   Map<String, Object> params = new HashMap<>();
		   params.put("property", "deviceId");
		   return (String)driver.executeScript("mobile:handset:info", params);	
	   }
	   
	   private void launchAppURL()
	   {
		   driver.get(appURL);		   
		   try { Thread.sleep(2000); } catch (InterruptedException e){}
	   }
}
