package com.testcases;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.pageobjects.HomePage;
import com.pageobjects.RegisterPage;
import com.testutility.BaseTest;

//@Test(groups = "register")
public class RegisterTest extends BaseTest{
		
		
		public RegisterTest()
		{
			super();
		}
	
		private HomePage home;
		private RegisterPage register;
		
		private String randomEmail;
		
		@Test(priority=1,groups= {"regression","smoke","memberuser","parallel"})
		public void verifyRegisterwithValidDataUsingRamdomEmail(ITestContext context)
		{
			home = new HomePage(getDriver());
			//register = new RegisterPage(driver);
			randomEmail = com.utilities.Utility.generateRandomEmail();
			
			//click on register link on homepage
			register = home.clickonregisterlink();
			step("Click on the registerlink");
			
			register.selectmalegenderradiobutton();
			step("Select male radio button");
			
			register.addfirstnameandlastname(dataprop.getProperty("firstname"),dataprop.getProperty("lastname"));
			step("Firstname and lastname entered");
			register.selectdateofbirth(dataprop.getProperty("day"),dataprop.getProperty("month"),dataprop.getProperty("year"));
			step("Enter date of birth");
			
			register.addEmailaddress(randomEmail);
			step("added random email");
			
			// Store the registered email in the TestNG context
	        context.setAttribute("registeredEmail", randomEmail);
	        
			register.addpassword(dataprop.getProperty("password"));
			step("add password");
			
			register.clickonregisterbutton();
			step("Click on register button");
			
			Assert.assertTrue(register.validateRegisterSuccessMessage(),"Register success message is not visible");
			
			Assert.assertEquals(register.getRegisterSuccessMessage(),dataprop.getProperty("registrationsuccessmsg"),"success msg not matched");
			
			register.clickoncontinuebutton();
			Assert.assertTrue(home.verifyloginlink(), "Login link is not visible");
			
		}
		
		@Test(priority=2, groups= {"regression","smoke"}, dataProvider="getData")
		public void VerifyRegisterwithValidDataUsingJSONData(HashMap<String,String>input)
		{
			home = new HomePage(getDriver());
			//register = new RegisterPage(driver);
			
			//click on register link on homepage
			register = home.clickonregisterlink();
			
			
			register.selectmalegenderradiobutton();
			
			register.addfirstnameandlastname(input.get("name"),input.get("lname"));
			register.selectdateofbirth(input.get("day"),input.get("month"),input.get("year"));
			register.addEmailaddress(input.get("email"));
			register.addpassword(input.get("password"));
			register.clickonregisterbutton();
			
			Assert.assertTrue(register.validateRegisterSuccessMessage(),"Register success message is not visible");
			
			register.clickoncontinuebutton();
			Assert.assertTrue(home.verifyloginlink(), "Login link is not visible");
			
			
		}
		
		@DataProvider
		public Object[][] getData() throws IOException
		{
			List<HashMap<String,String>> data = getJsondatatoMap(System.getProperty("user.dir")+ dataprop.getProperty("registerdatafilepath"));
			return new Object[][] {{data.get(0)},{data.get(1)}};
		}

}
