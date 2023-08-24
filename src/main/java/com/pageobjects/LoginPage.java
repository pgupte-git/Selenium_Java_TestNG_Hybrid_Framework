package com.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.utilities.Utility;

public class LoginPage extends Utility{
	
		
	static WebDriver driver;
	
	// HomePage Constructor to initialize driver and pagefactory 
	@SuppressWarnings("static-access")
	public LoginPage(WebDriver driver)
	{
		this.driver=driver;
		PageFactory.initElements(driver,this);
	}
	
	//Locators finded using @FinBy method
	@FindBy (xpath="//input[@id='Email']") WebElement emailfield;
	@FindBy (xpath="//input[@id='Password']") WebElement passwordfield;
	@FindBy (xpath="//div[@class='buttons']/button[@class='button-1 login-button']") 
	WebElement loginbutton;
	
	@FindBy (xpath="//div[@class='message-error validation-summary-errors']/ul/li") WebElement loginerrormsg;
	@FindBy (xpath="//span[@class='field-validation-error']") WebElement blankemailmsg;
	
	@FindBy (xpath="//div[@class='new-wrapper checkout-as-guest-or-register-block']/div[@class='title']")
	WebElement checkoutguestusertitle;
	@FindBy (xpath="//button[@class='button-1 checkout-as-guest-button']") WebElement checkoutbuttonguestuser;
	
	
	
	
	// Methods to perform action on login page
	
	public String verifytitleofpage()
	{
		return driver.getTitle();
	}
	
	public void enterusername(String value)
	{
		emailfield.clear();
		emailfield.sendKeys(value);
	}
	
	public void enterpassword(String value)
	{
		passwordfield.clear();
		passwordfield.sendKeys(value);
	}
	
	public HomePage clickonloginbutton()
	{
		loginbutton.click();
		return new HomePage(driver);
	}
	
	public void clicklogin()
	{
		loginbutton.click();
	}
	
	//Unsuccessfull login message
	public String getloginerrormsg()
	{
		return loginerrormsg.getText();
	}
	
	public String getblankemailerrormessage()
	{
		return blankemailmsg.getText();
	}
	
	public String validate_checkoutpage_as_guestuser_page_heading()
	{
		return checkoutguestusertitle.getText();
	}
	
	public CheckoutPage click_on_checkout_guest_user_button()
	{
		checkoutbuttonguestuser.click();
		return new CheckoutPage(driver);
	}
	

}
