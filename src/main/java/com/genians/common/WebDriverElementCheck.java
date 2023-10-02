package com.genians.common;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import com.genians.setup.setup;


public class WebDriverElementCheck {
	
	public static boolean isElementPresentByXpath(WebDriver driver, String xpath) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		try {
			driver.findElement(By.xpath(xpath));
			return true;
		} catch (NoSuchElementException e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(setup.implicitlyWait, TimeUnit.SECONDS);
		}
	}
	
	public static boolean isElementPresentByID(WebDriver driver, String ID) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		try {
			driver.findElement(By.id(ID));
			return true;
		} catch (NoSuchElementException e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(setup.implicitlyWait, TimeUnit.SECONDS);
		}
	}
	
	public static boolean isElementPresentByName(WebDriver driver, String name) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		try {
			driver.findElement(By.name(name));
			return true;
		} catch (NoSuchElementException e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(setup.implicitlyWait, TimeUnit.SECONDS);
		}
	}
	
	public static boolean isElementPresentByLinkText(WebDriver driver, String LinkText) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		try {
			driver.findElement(By.partialLinkText(LinkText));
			return true;
		} catch (NoSuchElementException e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(setup.implicitlyWait, TimeUnit.SECONDS);
		}
	}

	public static boolean containElementByXpath(WebDriver driver, String xpath, String attribute, String containString) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		try {
			driver.findElement(By.xpath(xpath)).getAttribute(attribute).contains(containString);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(setup.implicitlyWait, TimeUnit.SECONDS);
		}
	}
}
