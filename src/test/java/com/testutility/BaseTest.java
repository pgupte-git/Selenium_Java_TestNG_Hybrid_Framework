package com.testutility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.qameta.allure.Step;

public class BaseTest {

		//public static String browsername=System.getProperty("browser","chrome");
		//public static String enviornment = System.getProperty("env","local");
	
		public static Properties dataprop;
		public static Properties configprop;
		public static Properties commonfileprop;
		public static WebDriver driver;
		
		//used ThreadLocal java class for running the tests in parallel execution without any error 
		public static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<WebDriver>();
		
		public BaseTest()
		{
			dataprop = new Properties();
			File datafile = new File(System.getProperty("user.dir")+"\\src\\test\\java\\com\\testresources\\testdata.properties");
			try 
			{
				FileInputStream fis = new FileInputStream(datafile);
				dataprop.load(fis);
			} 
			catch (Throwable e) 
			{
				e.printStackTrace();
			}
			
			
			commonfileprop = new Properties();
			File commonfilepath = new File(System.getProperty("user.dir")+"\\src\\test\\java\\com\\testresources\\CommonFilePath.properties");
			try 
			{
				FileInputStream fis = new FileInputStream(commonfilepath);
				commonfileprop.load(fis);
			} 
			catch (Throwable e) 
			{
				e.printStackTrace();
			}
				
		}
		
		public static String default_browser = getconfigdata().getProperty("browser");
		public static String local_enviornment = getconfigdata().getProperty("env");
		public static String browserstack_enviornment = getconfigdata().getProperty("env2");
		
		
		public static String browsername=System.getProperty("browser",default_browser);
		public static String enviornment = System.getProperty("env",local_enviornment);
		
		@BeforeTest(alwaysRun=true)
		public void initializeBrowsers() throws MalformedURLException
		{
			browsername = System.getProperty("browser") != null ? System.getProperty("browser")
					:System.getProperty("browser",default_browser);
			
			//chrome-options to run the scripts in headless mode
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless=new"); //This option will run chrome in headless browser
					
			//Firefox-options to run the scripts in headless mode
			FirefoxOptions option = new FirefoxOptions();
			option.addArguments("--headless");
			
			if(enviornment.equals(local_enviornment))
			{
				if(browsername.contains("chrome"))
				{
					driver = new ChromeDriver();
					System.out.println("Chrome is selected");
				}
				
				else if(browsername.contains("firefox"))
				{
					driver = new FirefoxDriver();
					System.out.println("Firefox is selected");
				}
				
				else if(browsername.contains("Edge"))
				{
					driver = new EdgeDriver();
					System.out.println("MS Edge is selected");
				}
				
				else
				{
					System.out.println("Browser is not available");
				}
				
			}
			
			if(enviornment.equals(browserstack_enviornment))  // Used for Cross browser Testing
			{
				MutableCapabilities caps = new MutableCapabilities();
				driver = new RemoteWebDriver(new URL("https://hub.browserstack.com/wd/hub"),caps);
			}
			
			//set Driver
			threadLocalDriver.set(driver);
			
			//get URL
		    getDriver().get(dataprop.getProperty("url"));
			
		    getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		    getDriver().manage().window().maximize();
		    getDriver().manage().deleteAllCookies();
		}
		
		//get syncronized threadLocalDriver driver
	    public static synchronized  WebDriver getDriver(){
	    	//return threadLocalDriver.get();
	    	return driver;
	    }
	    
	    
	    public static Properties getconfigdata()
	  	{
	  		configprop = new Properties();
			File configfile = new File(System.getProperty("user.dir")+"\\src\\test\\java\\com\\testutility\\config.properties");
			try 
			{
				FileInputStream fis = new FileInputStream(configfile);
				configprop.load(fis);
			} 
			catch (Throwable e) 
			{
				e.printStackTrace();
			}
			
			return configprop;
	  	}
	    
	    @AfterTest(alwaysRun=true)
	    public void tearDown()
	    {
	        getDriver().quit();
	    	threadLocalDriver.remove();
	    }
	    
	  //Method to read the data from json files
	  	public List<HashMap<String,String>> getJsondatatoMap(String Filepath) throws IOException
	  	{
	  			//read file from json to string
	  			String jsonContent=FileUtils.readFileToString(new File(Filepath),StandardCharsets.UTF_8);
	  			
	  			// String to HashMap Jackson Databind
	  			ObjectMapper mapper = new ObjectMapper();
	  			
	  			List<HashMap<String,String>> data = mapper.readValue(jsonContent, new TypeReference<List<HashMap<String,String>>>(){});
	  			
	  			return data;
	  		
	  	}
	  	
	  	@Step("{0}")
	    public void step(String description) {
	        // This method is used to add a step with the provided description
	    }
	  	
	  	

}
