package com.testcases;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.pageobjects.HomePage;
import com.pageobjects.SearchPage;
import com.testutility.BaseTest;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

public class SearchProductTest extends BaseTest{
	
	HomePage home;
	SearchPage searchp;
	
	public SearchProductTest()
	{
		super();
	}
	
	
	@Description("Verify the user can search the product using homepage search textbox")
	@Severity(SeverityLevel.CRITICAL)
	@Story("HomePage Search")
	@Test(priority=1, groups= {"regression","smoke","parallel"})
	public void verify_search_functionality() throws InterruptedException, IOException
	{
		//home = new HomePage(getDriver());
		
		searchp = new SearchPage(getDriver());
		
		step("Go to the homepage");
		
		searchp.clickonSearchTextInput();
		Thread.sleep(2000);
		step("Click on search input text");
		
		searchp.entersearchValue("Digital");
		Thread.sleep(2000);
		step("Enter the search keyterm : Digital");
		
		searchp.select_value_from_dropdown("Digital Storm VANQUISH 3 Custom Performance PC");
		step("Select Digital Storm VANQUISH 3 Custom Performance PC");
		
		Thread.sleep(2000);
		
		String producttitle = searchp.getProductTitle();
		
		try
		{
			Assert.assertEquals(producttitle, dataprop.getProperty("desktop_pcname"));
			step("Product name is found");
		}
		
		catch(AssertionError e)
		{
			searchp.addRedBordertoMessage();
			Assert.assertEquals(producttitle, dataprop.getProperty("desktop_pcname"), "Search result Title not matched");
			step("Product name not found");
		}
		
		
		
		
	}
	
	

}
