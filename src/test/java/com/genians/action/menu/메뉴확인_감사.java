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


public class 메뉴확인_감사 extends GnTestBase {
    
    @Test
    public void test메뉴확인_감사() throws Exception {
        String 메인메뉴 = System.getProperty("메뉴확인_감사_메인메뉴", "");
        
        logInfo("관리콘솔의 [감사] 메뉴에서 메인메뉴(" + 메인메뉴 + ")를 클릭하여 페이지의 화면이 정상 표시되는지 확인한다.<br>");
        
        String[] subLogMenus = {
            // 로그 검색
            "로그 검색",
            "경고",
            "에러",
            "위험",
        };
        String [] subLogFilterMenus = {
            // 검색 필터
            "검색 필터",
            "Internal AP 감지됨",
            "IP 충돌",
            "Malware 감지됨",
            "관리자 로그인 실패",
            "관리자 로그인 인증 한계치 초과",
            "관리자활동",
            "단말 무결성 실패",
            "디스크 용량 부족 경고",
            "사용자인증 한계치 초과",
            "새로운 MAC 감지됨",
            "새로운 플랫폼 감지됨",
            "센서 동작상태",
            "에이전트 무결성 검증 실패",
            "주요 프로세스 강제 종료",
        };
        String[] subReportMenus = {
            // 리포트
            "노드 리포트",
            "로그 리포트",
            "무선랜 리포트",
            "사용자정의 리포트",
            "이메일 발송",
        };
        
        // 각 부메뉴와 그에 대한 ID를 매핑하는 Map
        Map<String, String> subMenuIds = new HashMap<>();
        // 로그 검색
        subMenuIds.put("로그 검색", System.getProperty("menu_감사_로그_로그검색"));
        subMenuIds.put("경고", System.getProperty("menu_감사_로그_경고"));
        subMenuIds.put("에러", System.getProperty("menu_감사_로그_에러"));
        subMenuIds.put("위험", System.getProperty("menu_감사_로그_위험"));
        // 검색 필터
        subMenuIds.put("검색 필터", System.getProperty("menu_감사_로그_검색필터"));
        subMenuIds.put("Internal AP 감지됨", "node_logFilterList_31");
        subMenuIds.put("IP 충돌", "node_logFilterList_35");
        subMenuIds.put("Malware 감지됨", "node_logFilterList_33");
        subMenuIds.put("관리자 로그인 실패", "node_logFilterList_37");
        subMenuIds.put("관리자 로그인 인증 한계치 초과", "node_logFilterList_36");
        subMenuIds.put("관리자활동", "node_logFilterList_34");
        subMenuIds.put("단말 무결성 실패", "node_logFilterList_39");
        subMenuIds.put("디스크 용량 부족 경고", "node_logFilterList_40");
        subMenuIds.put("사용자인증 한계치 초과", "node_logFilterList_38");
        subMenuIds.put("새로운 MAC 감지됨", "node_logFilterList_30");
        subMenuIds.put("새로운 플랫폼 감지됨", "node_logFilterList_32");
        subMenuIds.put("센서 동작상태", "node_logFilterList_4");
        subMenuIds.put("에이전트 무결성 검증 실패", "node_logFilterList_42");
        subMenuIds.put("주요 프로세스 강제 종료", "node_logFilterList_41");
        //리포트
        subMenuIds.put("노드 리포트", System.getProperty("menu_감사_리포트_노드리포트"));
        subMenuIds.put("로그 리포트", System.getProperty("menu_감사_리포트_로그리포트"));
        subMenuIds.put("무선랜 리포트", System.getProperty("menu_감사_리포트_무선랜리포트"));
        subMenuIds.put("사용자정의 리포트", System.getProperty("menu_감사_리포트_사용자정의리포트"));
        subMenuIds.put("이메일 발송", System.getProperty("menu_감사_리포트_이메일발송"));
       
       
        for (int k = 0; k <= setup.retryCount(); k++) {
            String currentSubMenu = "";   // 현재 클릭한 하위 메뉴를 저장할 변수
            try {
                driver.get(System.getProperty("testCloudURL") + "/mc2/faces/mainFrame.xhtml");
                String windowHandle = driver.getWindowHandle();
                
                focuseTop(windowHandle);

                mouseMoveTopMenu(System.getProperty("menu_감사"));
                
                // [관리] 메뉴의 하위 메뉴 선택
                switch (메인메뉴) {
                    case "로그":
                        selectTopSubMenu(System.getProperty("menu_감사_로그"));
                        for (String subLogMenu : subLogMenus) {
                            focuseTree(windowHandle);
                            // [로그]의 부메뉴 클릭
                            String subMenuId = subMenuIds.get(subLogMenu);
                            // 부메뉴가 표시될 때까지 대기
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.id(subMenuId)));
                            // 부메뉴 클릭
                            driver.findElement(By.id(subMenuId)).click();
                            focuseMainBody(windowHandle);
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tit")));

                            assertTrue(driver.findElement(By.cssSelector(".tit")).isDisplayed());
                            if (subLogMenu != "로그 검색") {
                                driver.navigate().back(); // 페이지 뒤로가기
                            }
                            currentSubMenu = subLogMenu; // 현재 클릭한 하위 메뉴 저장
                        }
                        break;
                    case "검색 필터":
                        selectTopSubMenu(System.getProperty("menu_감사_검색필터"));
                        for (String subLogFilterMenu : subLogFilterMenus) {
                            focuseTree(windowHandle);
                            // [검색 필터]의 부메뉴 클릭
                            String subMenuId = subMenuIds.get(subLogFilterMenu);
                            // 부메뉴가 표시될 때까지 대기
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.id(subMenuId)));
                            // 부메뉴가 표시되면 클릭
                            driver.findElement(By.id(subMenuId)).click();
                            focuseMainBody(windowHandle);
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tit")));
                            
                            assertTrue(driver.findElement(By.cssSelector(".tit")).isDisplayed());
                            if (subLogFilterMenu != "검색 필터") {
                                driver.navigate().back(); // 페이지 뒤로가기
                            }
                            currentSubMenu = subLogFilterMenu; // 현재 클릭한 하위 메뉴 저장
                        }
                        break;
                    case "RADIUS":
                        selectTopSubMenu(System.getProperty("menu_감사_RADIUS"));
                        focuseMainBody(windowHandle);
                        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tit")));
                        
                        assertTrue(driver.findElement(By.cssSelector(".tit")).isDisplayed());
                        break;
                    case "Flow":
                        selectTopSubMenu(System.getProperty("menu_감사_Flow"));
                        focuseMainBody(windowHandle);
                        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tit")));
                        
                        assertTrue(driver.findElement(By.cssSelector(".tit")).isDisplayed());
                        break;
                    case "디버그 로그":
                        selectTopSubMenu(System.getProperty("menu_감사_로그"));
                        focuseTree(windowHandle);
                        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.id("node_GAF")));
                        // 부메뉴가 표시되면 클릭
                        driver.findElement(By.id("node_GAF")).click();
                        focuseMainBody(windowHandle);
                        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tit")));
                        
                        assertTrue(driver.findElement(By.cssSelector(".tit")).isDisplayed());
                        break;
                    case "리포트":
                        selectTopSubMenu(System.getProperty("menu_감사_리포트"));
                        for (String subReportMenu : subReportMenus) {
                            focuseTree(windowHandle);
                            // [리포트]의 부메뉴 클릭
                            String subMenuId = subMenuIds.get(subReportMenu);
                            // 부메뉴가 표시될 때까지 대기
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.id(subMenuId)));
                            // 부메뉴 클릭
                            driver.findElement(By.id(subMenuId)).click();
                            focuseMainBody(windowHandle);
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tit")));

                            assertTrue(driver.findElement(By.cssSelector(".tit")).isDisplayed());
                            if (subReportMenu != "노드 리포트") {
                                driver.navigate().back(); // 페이지 뒤로가기
                            }
                            currentSubMenu = subReportMenu; // 현재 클릭한 하위 메뉴 저장
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