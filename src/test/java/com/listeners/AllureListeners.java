package com.listeners;

import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.testutility.BaseTest;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;


public class AllureListeners implements ITestListener{
	
	private static String getTestMethodName(ITestResult iTestResult) {
		return iTestResult.getMethod().getConstructorOrMethod().getName();
	}
	
	@Attachment(value="Page screeshot", type = "image/png")
	public byte[] saveFailureScreenShot(WebDriver driver) {
		return ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
	}
	
	@Attachment(value ="Stacktrace", type = "text/plain")
	public static String saveTextLog(String message) {
		return message;
	}
	
	
	@Override
	public void onStart(ITestContext context) {
		System.out.println("I am in onStart method " + context.getName());
		context.setAttribute("WebDriver", BaseTest.getDriver());
	}
	
	
	@Override
	public void onTestStart(ITestResult result) {
		System.out.println("I am in onTestStart method " + getTestMethodName(result) + " start");
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		System.out.println("I am in onTestSuccess method " + getTestMethodName(result) + " succeed");
	}

	/*@Override
	public void onTestFailure(ITestResult iTestResult) {
		System.out.println("I am in onTestFailure method " + getTestMethodName(iTestResult) + " failed");
		
		Object testClass = iTestResult.getInstance();
		WebDriver driver = BaseTest2.getDriver();
		
		// Allure ScreenShot and SaveTestLog
		
		if (driver instanceof WebDriver) 
		{
			saveFailureScreenShot(BaseTest2.getDriver());
			System.out.println("Screenshot captured for test case:" + getTestMethodName(iTestResult));
			
		}
		
		saveTextLog(getTestMethodName(iTestResult) + " failed and screenshot taken!");	
		
		
	}*/
	
	@Override
	public void onTestFailure(ITestResult iTestResult) {
		captureScreenshot(iTestResult);
		saveTextLog(iTestResult.getMethod().getConstructorOrMethod().getName());
		System.out.println("Screenshot is taken " + getTestMethodName(iTestResult));
		
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		System.out.println("I am in onTestSkipped method " + getTestMethodName(result) + " skipped");
	}

	@Override
	public void onFinish(ITestContext context) {
		
		System.out.println("I am in onFinish method " + context.getName());
		
		
	}
	
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		System.out.println("Test failed but it is in defined success ratio " + getTestMethodName(result));
	}
	
	private void captureScreenshot(ITestResult result) {
        Object testClass = result.getInstance();
        WebDriver driver = BaseTest.getDriver(); // Replace with your WebDriver setup
        
        if (driver instanceof TakesScreenshot) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot", new ByteArrayInputStream(screenshot));
        }
    }

}
