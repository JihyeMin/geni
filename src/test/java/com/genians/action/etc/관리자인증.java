package com.genians.action.etc;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.genians.action.GnTestBase;
import com.genians.common.ScreenCapture;
import com.genians.setup.setup;


public class 관리자인증 extends GnTestBase {
	
	@Test
	public void test관리자인증() throws Exception {
		String ID = System.getProperty("관리자인증_ID", System.getProperty("관리자인증_ID"));
		String PW = System.getProperty("관리자인증_PW", System.getProperty("관리자인증_PW"));
		String 포트변경 = System.getProperty("관리자인증_포트변경", "");
		String 오류메시지 = System.getProperty("관리자인증_오류메시지", "");
		
		logInfo(
			"관리자 로그인 페이지(" +  setup.baseUrl + "/mc)로 접속한다.<br>" +
			"아이디(" + ID + ") 및 비밀번호(" + PW + ")를 입력하고 로그인 버튼을 클릭한다.<br>");
			
		for (int k = 0; k <= setup.retryCount(); k++) {
			try {
				if (포트변경 == "") {
					driver.get(setup.baseUrl + "/mc2");	
				} else {
					driver.get("https://" + System.getProperty("center") + ":" + 포트변경 + "/mc2");	
				}
				driver.findElement(By.id("form1:userId")).clear();
				driver.findElement(By.id("form1:userId")).sendKeys(ID);
				driver.findElement(By.id("form1:passwd")).clear();
				driver.findElement(By.id("form1:passwd")).sendKeys(PW);
				int random = new Random().nextInt(2);
				doLogin(random);
				Thread.sleep(2000);
				
				String iframe = "//*[@id='allowIp']";
				if (this.isElementPresentByXpath(iframe) == true) {
					driver.switchTo().frame("allowIp");
					driver.findElement(By.id("form1:fldAllowIp1")).clear();
					driver.findElement(By.id("form1:fldAllowIp1")).sendKeys("172.29.0.0/16");
					driver.findElement(By.id("form1:actionBtn")).click();
				}
				
				String iframe2 = "//*[@id='answer']";
				if (this.isElementPresentByXpath(iframe2) == true) {
					driver.switchTo().frame("answer");
					driver.findElement(By.id("form1:homeBtn")).click();
				}
				
				for (int i = 1; i <= 3; i++) {
					String element = "//*[@id='message']/ul/li[" + i + "]";
					if (this.isElementPresentByXpath(element) == true) {
						if (driver.findElement(By.xpath(element)).getText().matches("^(|동일 역할의 관리자\\(.*\\)가 )이미 IP=[\\s\\S]*에서 접속하여 사용중입니다\\.$")) {
							if (!driver.findElement(By.id("form1:forceLogin")).isSelected()) {
								driver.findElement(By.id("form1:forceLogin")).click();
								if (this.isElementPresentByXpath(iframe2) == true) {
									driver.switchTo().frame("answer");
									driver.findElement(By.id("form1:homeBtn")).click();
								}
							}
							doLogin(random);
							break;
						}
					}
				}
				
				boolean checkFlag = false;
				if (오류메시지 != "") {
					for (int i = 1; i <= 3; i++) {
						String element = "//*[@id='message']/ul/li[" + i + "]";
						if (this.isElementPresentByXpath(element) == true) {
							if (driver.findElement(By.xpath(element)).getText().matches(오류메시지)) {
								checkFlag = true;
								break;
							}
						}
					}
					
					if (checkFlag == false) {
						throw new Exception("오류메시지(" + 오류메시지 + ") 출력되지 않음");
					}
				} else {
					for (int i = 1; i <= 10; i++) {
						String element = "//span[@class='login']//*[@id='form1:editLink']";
						if (this.isElementPresentByXpath(element) == true) {
							assertEquals(ID, driver.findElement(By.xpath(element)).getAttribute("title"));
							checkFlag = true;
							break;
						}
						Thread.sleep(1000);
					}
					
					if (checkFlag == false) {
						throw new Exception("관리자 로그인 실패");
					} 
				}
				logPass("결과: 기대 결과대로 동작");
				break;
			} catch (Throwable e) {
				if (k == setup.retryCount()) {
					new ScreenCapture().takeScreenShot(driver, System.getProperty("className") + "__" + getClass().getSimpleName() + ".png");
					String screenshotPath = "screenshots/" + System.getProperty("className") + "__" + getClass().getSimpleName() + ".png";
					logFail(e, screenshotPath);
				}
			} 	
		}
		setup.doRetrySetup();
	}
	
	public void doLogin (int random) {
		if (random == 0) {
			driver.findElement(By.id("form1:passwd")).sendKeys(Keys.ENTER);
		} else {
			driver.findElement(By.id("form1:dologin")).click();
		}
	}
}