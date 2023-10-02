package com.genians.action.csm;

import org.junit.Test;
import org.openqa.selenium.By;

import com.genians.action.GnTestBase;
import com.genians.common.ScreenCapture;
import com.genians.setup.setup;


public class CSM_Logout extends GnTestBase {
    
    @Test
    public void testCSM_Logout() throws Exception {
        String role = System.getProperty("CSM_Logout_role", "");
        String baseUrl = System.getProperty("CSM_Logout_baseUrl", "https://" + System.getProperty("testTarget"));
        
        logInfo(
            "CSM에서 로그아웃을 수행한다.<br>");
            
        for (int k = 0; k <= setup.retryCount(); k++) {
            try {
                driver.get(baseUrl + "/services");
                Thread.sleep(2000);
                String logOuturl = null;
                
                if (role.equals("admin")) {
                    logOuturl = driver.findElement(By.xpath("//nav/ul/li[6]/ul/li[3]/a")).getAttribute("href");	
                } else {
                    if (baseUrl.contains("my.genians.com")) {
                        logOuturl = driver.findElement(By.xpath("//nav/ul/li[4]/ul/li[3]/a")).getAttribute("href");	
                    } else {
                        logOuturl = driver.findElement(By.xpath("//nav/ul/li[5]/ul/li[3]/a")).getAttribute("href");	
                    }
                } 
                driver.get(logOuturl); 
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