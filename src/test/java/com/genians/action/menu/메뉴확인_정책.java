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


public class 메뉴확인_정책 extends GnTestBase {
    
    @Test
    public void test메뉴확인_정책() throws Exception {
        String 메인메뉴 = System.getProperty("메뉴확인_정책_메인메뉴", "");
        
        logInfo("관리콘솔의 [정책] 메뉴에서 메인메뉴(" + 메인메뉴 + ")를 클릭하여 페이지의 화면이 정상 표시되는지 확인한다.<br>");
        
        String[] subPolicyMenus = {
            // 정책
            "노드 정책",
            "노드 액션",
            "위험 감지",
            "Windows 업데이트 정책",
            "장치 제어 정책",
            "장치그룹",
            "RADIUS 정책",
            "제어 정책",
            "제어 액션",
            "무선랜 정책",
            "AP 프로파일",
            "Client 프로파일",
            "클라우드 보안그룹 정책",
            "규제 정책"
        };
        String [] subGroupMenus = {
            // 그룹
            "노드",
            "무선랜",
            "사용자"
        };
        String[] subObjectMenus = {
            // 객체
            "권한",
            "네트워크 객체",
            "서비스 객체",
            "시간 객체",
            "어플리케이션 객체",
            "어플리케이션",
        };
        
        // 각 부메뉴와 그에 대한 ID를 매핑하는 Map
        Map<String, String> subMenuIds = new HashMap<>();
        // 정책
        subMenuIds.put("노드 정책", System.getProperty("menu_정책_정책_노드정책"));
        subMenuIds.put("노드 액션", System.getProperty("menu_정책_정책_노드액션"));
        subMenuIds.put("위험 감지", System.getProperty("menu_정책_정책_위험감지"));
        subMenuIds.put("Windows 업데이트 정책", System.getProperty("menu_정책_정책_Windows업데이트정책"));
        subMenuIds.put("장치 제어 정책", System.getProperty("menu_정책_정책_장치제어정책"));
        subMenuIds.put("장치그룹", System.getProperty("menu_정책_정책_장치그룹"));
        subMenuIds.put("RADIUS 정책", System.getProperty("menu_정책_정책_RADIUS정책"));
        subMenuIds.put("제어 정책", System.getProperty("menu_정책_정책_제어정책"));
        subMenuIds.put("제어 액션", System.getProperty("menu_정책_정책_제어액션"));
        subMenuIds.put("무선랜 정책", System.getProperty("menu_정책_정책_무선랜정책"));
        subMenuIds.put("AP 프로파일", System.getProperty("menu_정책_정책_AP프로파일"));
        subMenuIds.put("Client 프로파일", System.getProperty("menu_정책_정책_Client프로파일"));
        subMenuIds.put("클라우드 보안그룹 정책", System.getProperty("menu_정책_정책_클라우드보안그룹정책"));//
        subMenuIds.put("규제 정책", System.getProperty("menu_정책_정책_규제정책"));
        // 그룹
        subMenuIds.put("노드", System.getProperty("menu_정책_그룹_노드"));
        subMenuIds.put("무선랜", System.getProperty("menu_정책_그룹_무선랜"));
        subMenuIds.put("사용자", System.getProperty("menu_정책_그룹_사용자"));
        // 객체
        subMenuIds.put("권한", System.getProperty("menu_정책_객체_권한"));
        subMenuIds.put("네트워크 객체", System.getProperty("menu_정책_객체_네트워크"));
        subMenuIds.put("서비스 객체", System.getProperty("menu_정책_객체_서비스"));
        subMenuIds.put("시간 객체", System.getProperty("menu_정책_객체_시간"));
        subMenuIds.put("어플리케이션 객체", System.getProperty("menu_정책_객체_어플리케이션객체"));
        subMenuIds.put("어플리케이션", System.getProperty("menu_정책_객체_어플리케이션"));
        
        for (int k = 0; k <= setup.retryCount(); k++) {
            String currentSubMenu = "";   // 현재 클릭한 하위 메뉴를 저장할 변수
            try {
                driver.get(System.getProperty("testCloudURL") + "/mc2/faces/mainFrame.xhtml");
                String windowHandle = driver.getWindowHandle();
                
                focuseTop(windowHandle);

                mouseMoveTopMenu(System.getProperty("menu_정책"));
                
                // [관리] 메뉴의 하위 메뉴 선택
                switch (메인메뉴) {
                    case "정책":
                        selectTopSubMenu(System.getProperty("menu_정책_정책"));
                        for (String subPolicyMenu : subPolicyMenus) {
                            focuseTree(windowHandle);
                            // [정책]의 부메뉴 클릭
                            String subMenuId = subMenuIds.get(subPolicyMenu);
                            // 부메뉴가 표시될 때까지 대기
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.id(subMenuId)));
                            // 부메뉴 클릭
                            driver.findElement(By.id(subMenuId)).click();
                            focuseMainBody(windowHandle);
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tit")));

                            assertTrue(driver.findElement(By.cssSelector(".tit")).isDisplayed());
                            if (subPolicyMenu != "노드 정책") {
                                driver.navigate().back(); // 페이지 뒤로가기
                            }
                            currentSubMenu = subPolicyMenu; // 현재 클릭한 하위 메뉴 저장
                        }
                        break;
                    case "그룹":
                        selectTopSubMenu(System.getProperty("menu_정책_그룹"));
                        for (String subGroupMenu : subGroupMenus) {
                            focuseTree(windowHandle);
                            // [그룹]의 부메뉴 클릭
                            String subMenuId = subMenuIds.get(subGroupMenu);
                            // 부메뉴가 표시될 때까지 대기
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.id(subMenuId)));
                            // 부메뉴가 표시되면 클릭
                            driver.findElement(By.id(subMenuId)).click();
                            focuseMainBody(windowHandle);
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tit")));
                            
                            assertTrue(driver.findElement(By.cssSelector(".tit")).isDisplayed());
                            if (subGroupMenu != "노드") {
                                driver.navigate().back(); // 페이지 뒤로가기
                            }
                            currentSubMenu = subGroupMenu; // 현재 클릭한 하위 메뉴 저장
                        }
                        break;
                    case "객체":
                        selectTopSubMenu(System.getProperty("menu_정책_객체"));
                        for (String subObjectMenu : subObjectMenus) {
                            Thread.sleep(3000); //페이지 로딩 시간
                            focuseTree(windowHandle);
                            // [객체]의 부메뉴 클릭
                            String subMenuId = subMenuIds.get(subObjectMenu);
                            
                            if (subObjectMenu.equals("권한") || subObjectMenu.equals("어플리케이션 객체")) {
                                // 부메뉴가 표시될 때까지 대기
                                new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.linkText(subMenuId)));
                                // 부메뉴가 표시되면 클릭
                                driver.findElement(By.linkText(subMenuId)).click();
                            } else {
                                // 부메뉴가 표시될 때까지 대기
                                new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.id(subMenuId)));
                                // 부메뉴가 표시되면 클릭
                                driver.findElement(By.id(subMenuId)).click();
                            }
                            focuseMainBody(windowHandle);
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tit")));
                            
                            assertTrue(driver.findElement(By.cssSelector(".tit")).isDisplayed());
                            if (subObjectMenu != "권한") {
                                driver.navigate().back(); // 페이지 뒤로가기
                            }
                            currentSubMenu = subObjectMenu; // 현재 클릭한 하위 메뉴 저장
                        }
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