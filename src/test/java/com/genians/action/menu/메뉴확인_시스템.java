package com.genians.action.menu;

import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.genians.setup.setup;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.genians.action.GnTestBase;
import com.genians.common.ScreenCapture;


public class 메뉴확인_시스템 extends GnTestBase {
    
    @Test
    public void test메뉴확인_시스템() throws Exception {
        String 메인메뉴 = System.getProperty("메뉴확인_시스템_메인메뉴", "");
        
        logInfo("관리콘솔의 [시스템] 메뉴에서 메인메뉴(" + 메인메뉴 + ")를 클릭하여 페이지의 화면이 정상 표시되는지 확인한다.<br>");
        
        String[] subSystemMenus = {
            // 시스템
            "사이트",
            "시스템 관리",
            "센서 관리",
            "Cloud Provider 관리",
            // 시스템 초기설정을 펼쳐야 하위 메뉴가 표시
            "환경설정",
            "센서 설정",
            "무선센서 환경설정",
            "세션 관리",
            "라이선스"
        };
        String [] subUpdateMenus = {
            // 업데이트 관리
            "소프트웨어",
            "에이전트 플러그인",
            "정책서버 플러그인",
            "운영정보 데이터",
        };
        
        // 각 부메뉴와 그에 대한 ID를 매핑하는 Map
        Map<String, String> subMenuIds = new HashMap<>();
        // 시스템
        subMenuIds.put("사이트", System.getProperty("menu_시스템관리_사이트"));
        subMenuIds.put("시스템 관리", System.getProperty("menu_시스템관리_시스템관리"));
        subMenuIds.put("센서 관리", System.getProperty("menu_시스템관리_센서관리"));
        subMenuIds.put("Cloud Provider 관리", System.getProperty("menu_시스템관리_CloudProvider관리"));
        subMenuIds.put("환경설정", System.getProperty("menu_시스템관리_환경설정"));
        subMenuIds.put("센서 설정", System.getProperty("menu_시스템관리_센서설정"));
        subMenuIds.put("무선센서 환경설정", System.getProperty("menu_시스템관리_무선센서환경설정"));
        subMenuIds.put("세션 관리", System.getProperty("menu_시스템관리_세션관리"));
        subMenuIds.put("라이선스", System.getProperty("menu_시스템관리_라이선스"));

        // 업데이트 관리
        subMenuIds.put("소프트웨어", System.getProperty("menu_시스템관리_소프트웨어"));
        subMenuIds.put("에이전트 플러그인", System.getProperty("menu_시스템관리_에이전트플러그인"));
        subMenuIds.put("정책서버 플러그인", System.getProperty("menu_시스템관리_정책서버플러그인"));
        subMenuIds.put("운영정보 데이터", System.getProperty("menu_시스템관리_운영정보데이터"));
        
        for (int k = 0; k <= setup.retryCount(); k++) {
            String currentSubMenu = "";   // 현재 클릭한 하위 메뉴를 저장할 변수
            try {
                driver.get(System.getProperty("testCloudURL") + "/mc2/faces/mainFrame.xhtml");
                String windowHandle = driver.getWindowHandle();
                
                focuseTop(windowHandle);

                mouseMoveTopMenu(System.getProperty("menu_시스템"));
                
                // [관리] 메뉴의 하위 메뉴 선택
                switch (메인메뉴) {
                    case "시스템":
                        selectTopSubMenu(System.getProperty("menu_시스템_시스템"));
                        focuseTree(windowHandle);
                        // 하위 메뉴 펼침
                        driver.findElement(By.xpath("//*[@id='node_EAE']/i")).click();
                        Thread.sleep(setup.testdelaytime);
                        for (String subSystemMenu : subSystemMenus) {
                            focuseTree(windowHandle);
                            // [시스템]의 부메뉴 클릭
                            String subMenuId = subMenuIds.get(subSystemMenu);
                            // 부메뉴가 표시될 때까지 대기
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.id(subMenuId)));
                            // 부메뉴 클릭
                            driver.findElement(By.id(subMenuId)).click();
                            focuseMainBody(windowHandle);
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tit")));

                            assertTrue(driver.findElement(By.cssSelector(".tit")).isDisplayed());
                            if (subSystemMenu != "시스템 관리") {
                                driver.navigate().back(); // 페이지 뒤로가기
                            }
                            currentSubMenu = subSystemMenu; // 현재 클릭한 하위 메뉴 저장
                        }
                        break;
                    case "업데이트 관리":
                        selectTopSubMenu(System.getProperty("menu_시스템_업데이트관리"));
                        for (String subUpdateMenu : subUpdateMenus) {
                            focuseTree(windowHandle);
                            // [업데이트 관리]의 부메뉴 클릭
                            String subMenuId = subMenuIds.get(subUpdateMenu);
                            // 부메뉴가 표시될 때까지 대기
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.id(subMenuId)));
                            // 부메뉴가 표시되면 클릭
                            driver.findElement(By.id(subMenuId)).click();
                            focuseMainBody(windowHandle);
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tit")));
                            
                            assertTrue(driver.findElement(By.cssSelector(".tit")).isDisplayed());
                            if (subUpdateMenu != "소프트웨어") {
                                driver.navigate().back(); // 페이지 뒤로가기
                            }
                            currentSubMenu = subUpdateMenu; // 현재 클릭한 하위 메뉴 저장
                        }
                        break;
                    case "서비스 관리":
                        selectTopSubMenu(System.getProperty("menu_시스템_서비스관리"));
                        focuseMainBody(windowHandle);
                        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tit")));
                        
                        assertTrue(driver.findElement(By.cssSelector(".tit")).isDisplayed());
                        break;
                    default:
                        // 메인메뉴 이름이 일치하지 않는 경우 예외 발생
                        throw new IllegalArgumentException("메인메뉴 이름으로 정의된 값이 아님: " + 메인메뉴);
                    
                }
                logPass("결과: 기대 결과대로 동작");
                break;
            } catch (Throwable e) {
                if (k == setup.retryCount()) {
                    new ScreenCapture().takeScreenShot(driver, System.getProperty("className") + "__" + getClass().getSimpleName() + ".png");
                    String screenshotPath = "screenshots/" + System.getProperty("className") + "__" + getClass().getSimpleName() + ".png";
                    logFailWithMenuInfo(e, screenshotPath, currentSubMenu);
                }
            }
            setup.doRetrySetup();
		}
    }

    // 새로 정의한 logFail 메소드, menuInfo 파라미터를 통해 어떤 메뉴에서 예외 발생되었는지 확인
    public void logFailWithMenuInfo(Throwable throwable, String screenshotPath, String menuInfo) {
        String stackTrace = getStackTrace(throwable);
        String formattedTrace = "<B>" + getClass().getSimpleName() + "</B><br>" + "<details>\n" +
                "    <summary>Click Here To See Exception Logs</summary>\n" +
                "    " + stackTrace + "\n" +
                "    <br>Menu Information: " + menuInfo + "\n" + // 메뉴 정보 추가
                "</details>\n";

        extentTest.log(Status.FAIL, formattedTrace, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
    }

    private String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}