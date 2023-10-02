package com.genians.action.etc;

import org.junit.Test;
import org.openqa.selenium.By;

import com.genians.action.GnTestBase;
import com.genians.common.ScreenCapture;
import com.genians.setup.setup;

public class 관리자로그아웃 extends GnTestBase {
    
    @Test
    public void test관리자로그아웃() throws Exception {
        
        logInfo(
            "우측 상단의 'LOGOUT' 버튼을 클릭하고 관리자인증 화면이 출력되는 것을 확인한다.<br>");
            
        for (int k = 0; k <= setup.retryCount(); k++) {
            try {
                driver.get(setup.baseUrl + "/mc2");
                String windowHandle = driver.getWindowHandle();
                
                focuseTop(windowHandle);
                
                driver.findElement(By.xpath("//*[@id='form1:logoffLink']")).click();
                Thread.sleep(1000);
                driver.findElement(By.id("form1:authPanel"));
                logPass("결과: 기대 결과대로 동작"); //GnTestBase 클래스에서 정의한 사용자 지정 로그 메서드를 사용하여 로그 기록 (테스트 클래스에서 일관되게 사용)
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
}