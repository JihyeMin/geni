package com.genians.action.csm;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.genians.action.GnTestBase;
import com.genians.common.ScreenCapture;
import com.genians.setup.setup;

import javax.net.ssl.HttpsURLConnection;

import java.net.URL;
import java.time.Duration;


public class CSM_createSite extends GnTestBase {
	
	@Test
	public void testCSM_createSite() throws Exception {
		String siteName = System.getProperty("CSM_createSite_siteName", System.getProperty("targetJiraNum"));
		String product = System.getProperty("CSM_createSite_product", "Genian ZT-NAC");
		String edition = System.getProperty("CSM_createSite_edition", "Enterprise Edition");
		String systemLanguage = System.getProperty("CSM_createSite_systemLanguage", "Korean");
		String region = System.getProperty("CSM_createSite_region", "Asia Pacific (Seoul)");
		String branch = System.getProperty("CSM_createSite_branch","CURRENT");
		String license = System.getProperty("CSM_createSite_license","300");
		String userEmail = System.getProperty("CSM_createSite_userEmail",System.getProperty("testAdminLoginID"));
		String targetVer = System.getProperty("CSM_createSite_targetVer",System.getProperty("targetVer"));
		if(System.getProperty("testTarget").equals("my.genians.com")){
			region = System.getProperty("CSM_createSite_region", "Asia Pacific (Singapore)");
		}
		
		logInfo(
			"CSM 관리 페이지(https://" + System.getProperty("testTarget") +  "/wp-admin/admin.php?page=genians-admin)에서 Add New 버튼을 클릭한다.<br>" +
			"출력되는 사이트 생성 화면에서 다음과 같이 파라미터를 설정하고 지금 생성 버튼을 클릭한다.<br>" +
			"<TABLE cellspacing='1' bgcolor='gray' style='MARGIN-TOP:2;MARGIN-LEFT:5'>" +
			"<tr><td>Site Name</td><td>" + siteName + "</td>" +
			"<tr><td>Edition</td><td>" + edition + "</td>" +
			"<tr><td>System Default Language</td><td>" + systemLanguage + "</td>" +
			"<tr><td>Region</td><td>" + region + "</td>" +
			"<tr><td>Branch</td><td>" + branch + "</td>" +
			"<tr><td>License</td><td>" + license + "</td>" +
			"<tr><td>UserEmail</td><td>" + userEmail + "</td>" +
			"<tr><td>TargetVersion</td><td>" + targetVer + "</td>" +
			"</TABLE>");
			
		for (int k = 0; k <= setup.retryCount(); k++) {
			try {	
				// wp-admin 페이지 진입 (배포/데모 URL testTarget쪽만 다르고 동일)
				driver.get("https://" + System.getProperty("testTarget") + "/wp-admin/admin.php?page=genians-admin");
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='wpbody-content']//a[text()='Add New']")));
				driver.findElement(By.xpath("//*[@id='wpbody-content']//a[text()='Add New']")).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sitename")));
				
				// Create Site
				driver.findElement(By.id("sitename")).clear();
				driver.findElement(By.id("sitename")).sendKeys(siteName);
				new Select(driver.findElement(By.id("product"))).selectByVisibleText(product);
				new Select(driver.findElement(By.id("edition"))).selectByVisibleText(edition);
				new Select(driver.findElement(By.id("region"))).selectByVisibleText(region);
				new Select(driver.findElement(By.id("branch"))).selectByVisibleText(branch);
				new Select(driver.findElement(By.id("language"))).selectByVisibleText(systemLanguage);
				driver.findElement(By.id("numberofdevices")).clear();
				driver.findElement(By.id("numberofdevices")).sendKeys(license);
				driver.findElement(By.id("customeremail")).clear();
				driver.findElement(By.id("customeremail")).sendKeys(userEmail);
				driver.findElement(By.id("targetVersion")).clear();
				driver.findElement(By.id("targetVersion")).sendKeys(targetVer);
				
				driver.manage().timeouts().implicitlyWait(setup.implicitlyWait, TimeUnit.SECONDS);
				if(System.getProperty("testTarget").equals("my.genians.com")){
					driver.findElement(By.xpath("//*[@id='register']/button[text()='Create Now']")).click();
				}
				else if(System.getProperty("testTarget").equals("my.demo.genians.co.kr")){
					driver.findElement(By.xpath("//*[@id='register']/button[text()='지금 생성']")).click();
				}
				WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(180));
				// 사이트 생성 버튼 클릭 후 최대 3분 대기(사이트 목록 화면으로 돌아 올 때까지)
				String create_site_text = "사이트가 준비 되면 이메일로 알려 드리겠습니다.";
				if(System.getProperty("testTarget").equals("my.genians.com")){
					create_site_text = "Your site will be available shortly. When ready, we will notify you via email.";
					wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='post-35507']//b[text()='" + create_site_text + "']")));
				}
				else if(System.getProperty("testTarget").equals("my.demo.genians.co.kr")){
					wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='post-36']//b[text()='" + create_site_text + "']")));
				}
				String findXpath = "//*[text()='" + System.getProperty("testCloudURL").replaceAll("https://","") + "']";
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(findXpath)));
				if (isElementPresentByXpath(findXpath) != true) {
					throw new Exception("CSM 사이트 생성되지 않음");
				}
				// Wait 10min for Cloud site creation
				int waitTime = setup.siteCreateWaitTime;
				for ( int i=0; i <= waitTime; i++) {
					String curlCheckURL = System.getProperty("testCloudURL") + "/mc2/faces/mainFrame.xhtml";
					URL urlcheck = new URL(curlCheckURL);
					HttpsURLConnection check = (HttpsURLConnection)urlcheck.openConnection();
					check.setRequestMethod("GET");
					try{
						check.connect();
						if ( check.getResponseCode() == 200 ) {
							break;
						}
					} catch(Exception e){
						Thread.sleep(1000);
						if(i==waitTime){
							throw new Exception("CSM 사이트 응답 확인 불가(사이트 생성 확인 필요)");
						}
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
			setup.doRetrySetup();
		}
	}
}
