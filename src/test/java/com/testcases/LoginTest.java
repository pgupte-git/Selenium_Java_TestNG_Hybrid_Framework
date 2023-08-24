package com.testcases;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.ITestContext;
//import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.pageobjects.HomePage;
import com.pageobjects.LoginPage;
import com.pageobjects.RegisterPage;
import com.testutility.BaseTest;
import com.utilities.Utility;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;


//@Test(dependsOnGroups = "register")
public class LoginTest extends BaseTest{
	
	public LoginTest()
	{
		super();
	}
	
	LoginPage login;
	HomePage home;
	RegisterPage register;
	
	ExtentTest extentTest;
	
	private String randomEmail;
	
	/*@BeforeClass
	public void getregisteremail(ITestContext context)
	{
		// Retrieve the registered email from the TestNG context
		randomEmail = (String) context.getAttribute("registeredEmail");
	}*/
	
	@Description("Verify the user can login using random email id")
	@Severity(SeverityLevel.CRITICAL)
	@Story("Login Using Random Email ID")
	@Test(priority=1, groups= {"regression","smoke","sanity","memberuser","parallel"})
	public void verifyloginwithValidDataUsingRandomEmails(ITestContext context)
	{
		home = new HomePage(getDriver());
		
		login = home.clickonloginlink();
		step("Click on the Login");
		
		// Retrieve the registered email from the TestNG context
		randomEmail = (String) context.getAttribute("registeredEmail");
		
		
		login.enterusername(randomEmail);
		step("add username");
		login.enterpassword(dataprop.getProperty("loginpassword"));
		step("Added Password");
		login.clickonloginbutton();
		step("Clicked on the Login button");
		
		Assert.assertTrue(home.verifymyaccountlink(),"My account link is not visible");
		step("My Account Link is verified");
		
		System.out.println(home.getmyaccountlink());
		
	}
	
	@Test(priority=2, groups= {"smoke"}, dataProvider = "getData")
	public void verifyloginwithValidDataUsingJSONData(HashMap<String,String>input)
	{
		home = new HomePage(getDriver());
		
		login = home.clickonloginlink();
		
		login.enterusername(input.get("email"));
		login.enterpassword(input.get("password"));
		login.clickonloginbutton();
		
		Assert.assertTrue(home.verifymyaccountlink(),"My account link is not visible");
		step("My Account Link is verified");
		
		System.out.println(home.getmyaccountlink());
		
		//home.clickonlogout();
		
		//Assert.assertTrue(home.verifyloginlink(), "Login link is not visible");
		
		
		//System.out.println("User is logout");
	}
	
	@Description("Verify the logout functionality")
	@Severity(SeverityLevel.CRITICAL)
	@Story("Logout functionality")
	@Test(priority=3, groups={"smoke","parallel"}, dependsOnMethods={"verifyloginwithValidDataUsingRandomEmails","verifyloginwithValidDataUsingJSONData"})
	public void verify_logout()
	{
		home.clickonlogout();
		step("Clicked on the Logout Link");
		
		Assert.assertTrue(home.verifyloginlink(), "Login link is not visible");
		step("User is logout");
		
		//System.out.println("User is logout");
	}
	
	
	
	@Description("Verify login with invalid data")
	@Severity(SeverityLevel.CRITICAL)
	@Story("Login with invalid data as nonmember")
	@Test(priority=4, groups= {"errorvalidation","parallel"}, dataProvider="nonmemberdata")
	public void verifyloginWithNonmember(String email, String password)
	{
		home = new HomePage(getDriver());
		
		login = home.clickonloginlink();
		
		login.enterusername(email);
		login.enterpassword(password);
		login.clickonloginbutton();
		String message ="No customer account found";
		Assert.assertEquals(login.getloginerrormsg(), message, "Error message is not matched");
	}
	
	@Description("Verify Login With Invalid Email and Password")
	@Severity(SeverityLevel.CRITICAL)
	@Story("Login With Invalid Email and Password")
	@Test(priority=5, groups= {"errorvalidation","parallel"},dataProvider="supplyinvaliddata")
	public void verifyLoginWithInvalidEmailandPassword(String email, String password)
	{
		home = new HomePage(getDriver());
		
		login = home.clickonloginlink();
		
		login.enterusername(email);
		login.enterpassword(password);
		login.clickonloginbutton();
		String message ="No customer account found";
		Assert.assertEquals(login.getloginerrormsg(), message, "Error message is not matched");
	}
	
	
	@Description("Verify Login With valid Email and Invalid Password")
	@Severity(SeverityLevel.CRITICAL)
	@Story("Login With valid Email and Invalid Password")
	@Test(priority=6, groups= {"errorvalidation","parallel"},dataProvider="supplyinvaliddata")
	public void verifyLoginWithvalidEmailandInvalidPassword(String email, String password)
	{
		login.enterusername(email);
		login.enterpassword(password);
		login.clickonloginbutton();
		String message ="No customer account found";
		Assert.assertEquals(login.getloginerrormsg(), message, "Error message is not matched");
	}
	
	
	@DataProvider
	public Object[][] getData() throws IOException
	{
		List<HashMap<String,String>> data = getJsondatatoMap(System.getProperty("user.dir")+dataprop.getProperty("registerdatafilepath"));
		return new Object[][] {{data.get(0)},{data.get(1)}};
	}
	
	// Data to read from excel
	@DataProvider(name = "nonmemberdata")
	public Object[][] supplydatafromexcel() throws IOException
	{
		Object[][] data = Utility.getDatafromExcelsheet("Nonmember");
		return data;
	}
	
	@DataProvider
	public Object[][] supplyinvaliddata() throws IOException
	{
		Object[][] data = Utility.getDatafromExcelsheet("InvalidData");
		
		return data;
	}
	
	

}
