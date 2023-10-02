package com.genians.action.system;

import static org.junit.Assert.assertTrue;

import java.time.Duration;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.genians.common.ScreenCapture;
import com.genians.action.GnTestBase;
import com.genians.setup.setup;


public class 시스템관리_미승인센서승인 extends GnTestBase {

	@Test
	public void test시스템관리_미승인센서승인() throws Exception {

        logInfo(
            "[시스템 관리 > 시스템 > 시스템목록]에서 미승인 센서 유무를 체크한다. <br>" +
            "미승인 센서가 존재하면, 작업선택메뉴에서 '미승인 센서 승인'을  클릭한다.<br>");
		
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
                
                if (this.isElementPresent(By.linkText("미승인 센서")) == true) {
                    driver.findElement(By.xpath("//*[@id='form1:condDevType']/span")).click(); //장비 유형 클릭
                    int rowCount = driver.findElements(By.xpath("//*[@id='form1:condDevType_panel']/div[2]/ul/li")).size();
                    for (int i = 1; i <= rowCount; i++) {
                        if (driver.findElement(By.xpath("//*[@id='form1:condDevType_panel']/div[2]/ul/li[" + i +"]/label")).getText().matches("네트워크 센서")) {
                            driver.findElement(By.xpath("//*[@id='form1:condDevType_panel']/div[2]/ul/li[" + i +"]/label")).click();
                            driver.findElement(By.id("form1:btnSearch")).click();
                            Thread.sleep(setup.testdelaytime);
                            break;
                        }
                        if (i == rowCount) {
                            throw new Exception("장비유형 선택 목록에서 '네트워크 센서' 장비유형이 존재하지 않음");
                        }
                    } 

                    int rowCount2 = driver.findElements(By.xpath("//*[@id='form1:dataTable1_data']/tr")).size();
                    for (int i = 0; i < rowCount2; i++) {
                        if (driver.findElement(By.id("form1:dataTable1:" + i + ":txt_devtypeCol_DL_CONFIRM")).getText().matches("미승인")) {
                            driver.findElement(By.id("form1:dataTable1:" + i + ":checkBox")).click();
                        }
                    }
                    driver.findElement(By.id("form1:dynaCmdButton")).click();
                    driver.findElement(By.xpath("//a[contains(@onclick, 'menuDevConfirm')]")).click();
                    Thread.sleep(setup.testdelaytime);
                    String actualText = driver.findElement(By.cssSelector("span.ui-messages-info-summary")).getText();
                    String expectedText = "총 .*개의 장비가 승인되었습니다.";
                    assertTrue(actualText.matches(expectedText));
                    Thread.sleep(setup.testdelaytime);
                    
                    /*
                    * 센서 인터페이스(eth0) 확인을 위해 센서설정으로 이동
                    */
                    String ipLink = "form1:dataTable1:0:lnk_activeipCol";
                    driver.findElement(By.id(ipLink)).click();
                    Thread.sleep(setup.testdelaytime);
                    driver.findElement(By.xpath("//a[contains(@href, '#form1:genianDevTabView:ssSetupTab')]")).click();
                    
                    /*
                    * 센서설정에 eth0 가 존재하는지 확인한다.
                    * 기본대기 5초, 5초 후 존재하지 않으면 새로고침 후 다시 확인 (5회 까지 반복, 25초)
                    */
                    
                    for (int i = 0; i <= 5; i++) {
                        String getText = driver.findElement(By.xpath("//*[@id='form1:genianDevTabView:tblSsSetup_data']/tr/td")).getText();
                        if (getText.equals("eth0")) {
                            break;
                        } else {
                            Thread.sleep(5000);
                            driver.findElement(By.xpath("//*[@id='form1:pnlLastUpdateDate']/a/span")).click(); //새로 고침 클릭
                        }
                        if (i == 5 ) {
                            throw new Exception("목록에 인터페이스(eth0)가 존재하지 않음");
                        }
                    }
                
                } else {
                    throw new Exception("미승인 센서 링크 텍스트가 확인되지 않음");
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