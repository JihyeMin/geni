package com.genians.action.etc;

import static org.junit.Assert.*;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.genians.action.GnTestBase;
import com.genians.common.ScreenCapture;
import com.genians.setup.setup;


public class ZTNA_Login extends GnTestBase {

	@Test
	public void testZTNA_Login() throws Exception {
		String URL = System.getProperty("ZTNA_Login_URL", System.getProperty("testCloudURL"));
		String ID = System.getProperty("ZTNA_Login_ID", System.getProperty("testAdminLoginID"));
		String PW = System.getProperty("ZTNA_Login_PW", System.getProperty("testAdminLoginPASS"));
		String LANG = System.getProperty("ZTNA_Login_LANG", System.getProperty("testLanguage"));
		String errorMessage = System.getProperty("ZTNA_Login_errorMessage", "");
		String timeZone1 = System.getProperty("ZTNA_Login_timeZone1", "");
		String timeZone2 = System.getProperty("ZTNA_Login_timeZone2", "");
		String CountryCode = System.getProperty("ZTNA_Login_CountryCode", "");
		String SNMPCOMMUNITY = System.getProperty("ZTNA_Login_SNMPCOMMUNITY", "");
		String SSID = System.getProperty("ZTNA_Login_SSID", "");
		
		logInfo(
			"관리자 로그인 페이지(" +  URL + ")로 접속한다.<br>" +
			"아이디(" + ID + ") 및 비밀번호(" + PW + ")를 입력하고 로그인 버튼을 클릭한다.<br>");
			
		for (int k = 0; k <= setup.retryCount(); k++) {
			try {
				
				driver.get(URL);
				// 사이트 생성 액션에서 HTTP 응답 코드 200(응답 성공)을 확인하지만 응답 코드를 받은 후에도 사이트 접근이 되지 않아 한 번 더 사이트 생성 시까지 대기(최대 10분)
				for(int m=1; m<=200; m++){
					if(this.isElementPresent(By.id("form1:userId")) == true) {
						break;
					}
					Thread.sleep(3000);
					driver.navigate().refresh();
				}
				
				driver.findElement(By.id("form1:userId")).clear();
				driver.findElement(By.id("form1:userId")).sendKeys(ID);
				driver.findElement(By.id("form1:passwd")).clear();
				driver.findElement(By.id("form1:passwd")).sendKeys(PW);
				int random = new Random().nextInt(2);
				doLogin(random);
				Thread.sleep(3000);
				
				String iframe = "//*[@id='allowIp']";
				if (this.isElementPresentByXpath(iframe) == true) {
					driver.switchTo().frame("allowIp");
					driver.findElement(By.id("form1:fldAllowIp1")).clear();
					//driver.findElement(By.id("form1:fldAllowIp1")).sendKeys("172.29.0.0/16");
					driver.findElement(By.id("form1:fldAllowIp1")).sendKeys(System.getProperty("공인_IP"));
					driver.findElement(By.id("form1:actionBtn")).click();
				}
				
				String iframe2 = "//*[@id='answer']";
				if (this.isElementPresentByXpath(iframe2) == true) {
					driver.switchTo().frame("answer");
					driver.findElement(By.id("form1:homeBtn")).click();
				}
				
				// NAC Login duplicate process
				for (int i = 1; i <= 3; i++) {
					String element = "//*[@id='message']/ul/li[" + i + "]";
					if (this.isElementPresentByXpath(element) == true) {
						String duplicateLoginMsg = "";
						if (LANG.matches("KO")) {
							duplicateLoginMsg = "이미 IP=[\\s\\S]* 에서 접속하여 사용중입니다\\.$";
						} else {
							duplicateLoginMsg = "An account is already in use by the device with IP address [\\s\\S]*\\.$";
						}
						
						System.out.println(duplicateLoginMsg);
						if (driver.findElement(By.xpath(element)).getText().matches(duplicateLoginMsg)) {
							//중복 로그인
							if (!driver.findElement(By.id("form1:forceLogin")).isSelected()) {
								driver.findElement(By.id("form1:forceLogin")).click();
								if (this.isElementPresentByXpath(iframe2) == true) {
									driver.switchTo().frame("answer");
									driver.findElement(By.id("form1:homeBtn")).click();
								}
							}
							doLogin(random);
							Thread.sleep(2000);
							break;
						}
					}
				}
				// OTP Login
				String iframe3 = "//*[@id='otpAuth']";
				Boolean otpPass = false;
				if (this.isElementPresentByXpath(iframe3) == true) {
					// OTP Login
					driver.switchTo().frame("otpAuth");
					String element = "form1:fldCheckAuthNumber";
					String otpSecret = System.getProperty("testAdminOTP");
					
					for (int i = 1; i <= 3; i++) {
						driver.findElement(By.id(element)).clear();
						driver.findElement(By.id(element)).sendKeys(setup.getTwoFactorCode(otpSecret));
						driver.findElement(By.id("form1:actionBtn")).click();
						Thread.sleep(2000);
						String otpFailClass = "//*[@class='ui-messages-error-summary']";
						if (this.isElementPresentByXpath(otpFailClass) == false) {
							break;
						} else {
							Thread.sleep(30000);
						}
					}
				}
				
				driver.manage().timeouts().implicitlyWait(setup.implicitlyWait, TimeUnit.SECONDS);

				if (errorMessage != "") {
					boolean checkFlag = false;
					driver.manage().timeouts().implicitlyWait(setup.implicitlyWait, TimeUnit.SECONDS);
					for (int i = 1; i <= 3; i++) {
						String element = "//*[@id='message']/ul/li[" + i + "]";
						if (this.isElementPresentByXpath(element) == true) {
							if (driver.findElement(By.xpath(element)).getText().matches(errorMessage)) {
								checkFlag = true;
								break;
							}
						}
					}
					driver.manage().timeouts().implicitlyWait(setup.implicitlyWait, TimeUnit.SECONDS);
					if (checkFlag == false) {
						throw new Exception("오류메시지(" + errorMessage + ") 출력되지 않음");
					}
				} else if (this.isElementPresentByXpath("//*[@id='form1:pnlUserAgreement']") == true) {
					driver.findElement(By.xpath("//*[@id='form1:pnlUserAgreement_content']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/input")).click();
					driver.findElement(By.xpath("//*[@id='form1:btnConfirm']")).click();
					Thread.sleep(1000);
					
					if (timeZone1 != "") {
						new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(By.id("form1:lbxRegion_SYSTIMEZONE")));
						new Select(driver.findElement(By.id("form1:lbxRegion_SYSTIMEZONE"))).selectByVisibleText(timeZone1);	
					}
					if (timeZone2 != "") {
						new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(By.id("lbxTimeZone_SYSTIMEZONE")));
						new Select(driver.findElement(By.id("lbxTimeZone_SYSTIMEZONE"))).selectByVisibleText(timeZone2);	
					}
					if (CountryCode != "") {
						new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(By.id("form1:DEFAULTCOUNTRYCODE_selItemE")));
						new Select(driver.findElement(By.id("form1:DEFAULTCOUNTRYCODE_selItem"))).selectByVisibleText(CountryCode);	
					}
					if (SNMPCOMMUNITY != "") {
						new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(By.id("form1:SNMPCOMMUNITY")));
						new Select(driver.findElement(By.id("form1:SNMPCOMMUNITY"))).selectByVisibleText(SNMPCOMMUNITY);	
					}
					if (SSID != "") {
						new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(By.id("form1:InternalSSID")));
						new Select(driver.findElement(By.id("form1:InternalSSID"))).selectByVisibleText(SSID);
					}
					driver.findElement(By.id("form1:btnConfirm")).click();
					Thread.sleep(setup.testdelaytime);
				} else {
					driver.manage().timeouts().implicitlyWait(setup.implicitlyWait, TimeUnit.SECONDS);
					for (int i = 0; i < 10; i++) {
						try {
							assertEquals(ID, driver.findElement(By.id("form1:editLink")).getAttribute("title"));
							break;
						} catch (StaleElementReferenceException | NoSuchElementException e) {
							e.printStackTrace();
						}
						Thread.sleep(200);
					}
				}

				String windowHandle = driver.getWindowHandle();
				focuseMainFrame(windowHandle);
				if (this.isElementPresent(By.name("noticeDlg")) == true) {
					driver.switchTo().frame(driver.findElement(By.name("noticeDlg")));
					driver.findElement(By.id("form1:chkNeverShow")).click();
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
			setup.doRetrySetup();
		}
	}
	
	public void doLogin (int random) {
		if (random == 0) {
			driver.findElement(By.id("form1:passwd")).sendKeys(Keys.ENTER);
		} else {
			driver.findElement(By.id("form1:dologin")).click();
		}
	}

}