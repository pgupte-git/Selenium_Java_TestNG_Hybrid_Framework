package com.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.utilities.Utility;

public class HomePage extends Utility{
	
		static WebDriver driver;
		
		// HomePage Constructor to initialize driver and pagefactory 
		@SuppressWarnings("static-access")
		public HomePage(WebDriver driver)
		{
			this.driver=driver;
			PageFactory.initElements(driver,this);
		}
		
		//Locators finded using @FinBy method
		@FindBy (xpath="//div[@class='header-links']//li/a[@class='ico-login']") WebElement loginlink;
		@FindBy (xpath="//div[@class='header-links']//li/a[@class='ico-register']") WebElement registerlink;
		
		@FindBy (xpath="//a[@class='ico-account']") WebElement myaccountlink;
		
		@FindBy (xpath="//a[@class='ico-logout']") WebElement logoutlink;
		
		//Select product from the menus
		@FindBy (xpath="//ul[@class='top-menu notmobile']/li/a[contains(text(),'Apparel')]") 
		WebElement Apparel_Option;
		
		@FindBy (xpath="//ul[@class='top-menu notmobile']//a[normalize-space()='Shoes']") 
		WebElement ShoesMenuOption;
		
		//Methods to used a Action elements in testcases
		
		public String gethomepagetitle()
		{
			return driver.getTitle();
		}
		
		public LoginPage clickonloginlink()
		{
			loginlink.click();
			return new LoginPage(driver);
		}
		
		public boolean verifymyaccountlink()
		{
			return myaccountlink.isDisplayed();
		}
		
		public String getmyaccountlink()
		{
			return myaccountlink.getText();
		}
		
		public void clickonlogout()
		{
			logoutlink.click();
		}
		
		public boolean verifyloginlink()
		{
			return loginlink.isDisplayed();
		}
		
		public RegisterPage clickonregisterlink()
		{
			registerlink.click();
			return new RegisterPage(driver);
		}
		
		public ProductListingPage gotoshoesproductlistpage()
		{
			perform_mousehover(driver, Apparel_Option, ShoesMenuOption);
			return new ProductListingPage(driver);
		}
		
		public MyAccountPage click_on_my_account()
		{
			myaccountlink.click();
			return new MyAccountPage(driver);
		}

}
