package com.genians.common;

import org.openqa.selenium.WebDriver;

public class WebDriverStatusCheck {
	
	public static boolean loopCheckElementByName(WebDriver driver, String pattern) throws InterruptedException {
		int loopCount = Integer.parseInt(System.getProperty("loopCount","10"));
		int waitingTime = Integer.parseInt(System.getProperty("waitingTime","1000"));
		
		for (int i = 0; i < loopCount ; i++) {
			// By.Name pattern check
			if(WebDriverElementCheck.isElementPresentByName(driver, pattern)) {
				System.out.println(pattern + " Found Success");
				return true;
			} else {
				if (i == (loopCount-1)) {
					System.out.println(pattern + " Failed");
					return false;
				} else {
					System.out.println(pattern + " Waiting");
					Thread.sleep(waitingTime);
				}
			}
		}
		return false;
	}

	public static boolean loopCheckElementByID(WebDriver driver, String pattern) throws InterruptedException {
		int loopCount = Integer.parseInt(System.getProperty("loopCount","10"));
		int waitingTime = Integer.parseInt(System.getProperty("waitingTime","1000"));
		
		for (int i = 0; i < loopCount ; i++) {
			// By.ID pattern check
			if(WebDriverElementCheck.isElementPresentByID(driver, pattern)) {
				System.out.println(pattern + " Found Success");
				return true;
			} else {
				if (i == (loopCount-1)) {
					System.out.println(pattern + " Failed");
					return false;
				} else {
					System.out.println(pattern + " Waiting");
					Thread.sleep(waitingTime);
				}
			}
		}
		return false;
	}

	public static boolean loopCheckElementByXpath(WebDriver driver, String pattern) throws InterruptedException {
		int loopCount = Integer.parseInt(System.getProperty("loopCount","10"));
		int waitingTime = Integer.parseInt(System.getProperty("waitingTime","1000"));
		
		for (int i = 0; i < loopCount ; i++) {
			// By.Xpath pattern check
			if(WebDriverElementCheck.isElementPresentByXpath(driver, pattern)) {
				System.out.println(pattern + " Found Success");
				return true;
			} else {
				if (i == (loopCount-1)) {
					System.out.println(pattern + " Failed");
					return false;
				} else {
					System.out.println(pattern + " Waiting");
					Thread.sleep(waitingTime);
				}
			}
		}
		return false;
	}

	public static boolean loopCheckElementByLinkText(WebDriver driver, String pattern) throws InterruptedException {
		int loopCount = Integer.parseInt(System.getProperty("loopCount","10"));
		int waitingTime = Integer.parseInt(System.getProperty("waitingTime","1000"));
		
		for (int i = 0; i < loopCount ; i++) {
			// By.LinkText pattern check
			if(WebDriverElementCheck.isElementPresentByLinkText(driver, pattern)) {
				System.out.println(pattern + " Found Success");
				return true;
			} else {
				if (i == (loopCount-1)) {
					System.out.println(pattern + " Failed");
					return false;
				} else {
					System.out.println(pattern + " Waiting");
					Thread.sleep(waitingTime);
				}
			}
		}
		return false;
	}

	public static boolean loopCheckElementNameByXpath(WebDriver driver, String pattern, String attribute, String containString) throws InterruptedException {
		int loopCount = Integer.parseInt(System.getProperty("loopCount","10"));
		int waitingTime = Integer.parseInt(System.getProperty("waitingTime","1000"));
		
		for (int i = 0; i < loopCount ; i++) {
			// By.Xpath pattern check
			if(WebDriverElementCheck.containElementByXpath(driver, pattern, attribute, containString)) {
				System.out.println(pattern + " , " + attribute + " , " + containString + " Found Success");
				return true;
			} else {
				if (i == (loopCount-1)) {
					System.out.println(pattern + " , " + attribute + " , " + containString + " Failed");
					return false;
				} else {
					System.out.println(pattern + " , " + attribute + " , " + containString + " Waiting");
					Thread.sleep(waitingTime);
				}
			}
		}
		return false;
	}
}
