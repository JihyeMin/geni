package com.genians.action.csm;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.genians.action.GnTestBase;
import com.genians.common.ScreenCapture;
import com.genians.setup.setup;


public class CSM_deleteSite extends GnTestBase {
    
    @Test
    public void testCSM_deleteSite() throws Exception {
        String LANG = System.getProperty("CSM_deleteSite_LANG", "");
        String baseUrl = System.getProperty("CSM_deleteSite_baseUrl", "");
        String siteName = System.getProperty("CSM_deleteSite_siteName", "");
        String cloudDomain = System.getProperty("CSM_deleteSite_cloudDomain", "");
        
        logInfo(
            "CSM 관리 페이지(" +  baseUrl +  "/service)에서 사이트이름(" + siteName + ")을 입력하고 Search 버튼을 클릭한다.<br>" +
            "검색된 사이트 이름의 Delete Site 링크를 클릭한다.<br>");
            
        for (int k = 0; k <= setup.retryCount(); k++) {
            try {
                driver.get(baseUrl + "/services");	
                driver.manage().timeouts().implicitlyWait(setup.implicitlyWait, TimeUnit.SECONDS);
                
                // Search Site 
                driver.findElement(By.name("license_search")).clear();
                driver.findElement(By.name("license_search")).sendKeys(siteName);
                driver.findElement(By.xpath("//button[@type='submit']")).click();
                
                // Delete Site
                driver.findElement(By.xpath("//i[@class='fa fa-angle-right']")).click();
                String findLink = "//*[text() = 'Delete Site']";
                if (LANG.matches("KO")) {
                    findLink = "//*[text() = '사이트 삭제']";
                }
                
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(findLink)));
                
                if (this.isElementPresentByXpath(findLink) == true) {
                    driver.findElement(By.xpath(findLink)).click();
                } else {
                    throw new Exception("Can't found 'Delete Site' link!!!");
                }
                
                // Type confirm text
                wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("typename")));
                
                driver.findElement(By.id("typename")).clear();
                driver.findElement(By.id("typename")).sendKeys(siteName + "." + cloudDomain);
                findLink = "//*[text() = 'Confirm']";
                if (LANG.matches("KO")) {
                    findLink = "//*[text() = '확인']";
                }
                
                driver.findElement(By.xpath(findLink)).click();
                findLink = "//button[contains(.,' Try Cloud')]";
                wait = new WebDriverWait(driver, Duration.ofSeconds(120));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(findLink)));					
                
                // Test cloud site deleted check
                String findText = "//*[text() = '" + siteName + "." + cloudDomain + "']";
                if (this.isElementPresentByXpath(findText) == true) {
                    new Exception("Do not deleted test cloud site!!!");
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