package com.genians.action.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.genians.common.ScreenCapture;
import com.genians.action.GnTestBase;
import com.genians.setup.setup;


public class 시스템관리_빌드번호확인 extends GnTestBase {
    
	@Test
	public void test시스템관리_빌드번호확인() throws Exception {
        String 장비유형 = System.getProperty("시스템관리_빌드번호확인_장비유형", "");
        String 빌드번호 = System.getProperty("시스템관리_빌드번호확인_빌드번호", System.getProperty("revision"));
        
        logInfo(
            "[시스템 관리 > 시스템 > 시스템목록]에서 빌드번호를 확인할 장비유형(" + 장비유형 + ")을 선택한다. <br>" +
            "장비유형(" + 장비유형 + ")에 대한 빌드번호가 테스트 타겟버전(" + 빌드번호 + ")과 동일한지 확인한다.<br>");

		for (int k = 0; k <= setup.retryCount(); k++) {
            try {
                driver.get(System.getProperty("testCloudURL") + "/mc2/faces/mainFrame.xhtml");
                String windowHandle = driver.getWindowHandle();
                
                focuseTop(windowHandle);
                
                selectTopMenu(System.getProperty("menu_시스템"));
                
                focuseTree(windowHandle);
                
                selectTreeNode(System.getProperty("menu_시스템관리_시스템관리")); //시스템 관리
                
                focuseMainBody(windowHandle);
                
                new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(By.linkText("IP주소")));
                
                if (장비유형 != "") {
                    driver.findElement(By.xpath("//*[@id='form1:condDevType']/span")).click();
                    int rowCount = driver.findElements(By.xpath("//*[@id='form1:condDevType_panel']/div[2]/ul/li")).size();
                    for (int i = 1; i <= rowCount; i++) {
                        if (driver.findElement(By.xpath("//*[@id='form1:condDevType_panel']/div[2]/ul/li[" + i +"]/label")).getText().matches(장비유형)) {
                            driver.findElement(By.xpath("//*[@id='form1:condDevType_panel']/div[2]/ul/li[" + i +"]/label")).click();
                            driver.findElement(By.id("form1:btnSearch")).click();
                            Thread.sleep(setup.testdelaytime);
                            break;
                        }
                        if (i == rowCount) {
                            throw new Exception("장비유형 선택 목록에서 " + 장비유형 + "이(가) 존재하지 않음");
                        }
                    }
                } else {
                    throw new Exception("장비유형을 설정하지 않음");
                }

                if (빌드번호 != "") {
                    int rowCount2 = driver.findElements(By.xpath("//*[@id='form1:dataTable1_data']/tr")).size();
                    for (int j = 0; j < rowCount2; j++ ) {
                        assertTrue(this.isElementPresent(By.xpath("//*[@id='form1:dataTable1:" + j + ":txt_devtypeCol1']/img[@oldtitle='" + 장비유형 + "']")));
                        assertTrue(driver.findElement(By.xpath("//span[@id='form1:dataTable1:" + j + ":txt_swversionCol1']")).getText().matches("(.*)" + 빌드번호 + "(.*)"));
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