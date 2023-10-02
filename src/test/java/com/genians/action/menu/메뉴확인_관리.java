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


public class 메뉴확인_관리 extends GnTestBase {
    
    @Test
    public void test메뉴확인_관리() throws Exception {
        String 메인메뉴 = System.getProperty("메뉴확인_관리_메인메뉴", "");
        
        logInfo("관리콘솔의 [관리] 메뉴에서 메인메뉴(" + 메인메뉴 + ")를 클릭하여 페이지의 화면이 정상 표시되는지 확인한다.<br>");

        String [] subUserMenus = {
            // 사용자 관리
            "전체사용자",
            "부서별",
            "전체 관리자",
            "부서 관리",
            "직급 관리"
        };
        String [] subRequestMenus = {
            // 신청 관리
            "사용자 신청 신규 등록", //이름 중복으로 테스트 이름 변경
            "사용자 신청 결과 조회", //이름 중복으로 테스트 이름 변경
            "IP 신규/반납",
            "IP 장비 변경",
            "사용자 변경",
            "IP 사용 신청 결과 조회", //이름 중복으로 테스트 이름 변경
            "단계별 승인",
            "단계별 IP 신규",
            "단계별 IP 반납",
            "단계별 IP 장비 변경",
            "단계별 사용자 변경",
            "장치 사용 신청 신규 등록", //이름 중복으로 테스트 이름 변경
            "장치 사용 신청 결과 조회", //이름 중복으로 테스트 이름 변경
            "에이전트 인증코드 발급",
            "에이전트 인증 결과 조회" //이름 중복으로 테스트 이름 변경
        };

        // 각 부메뉴와 그에 대한 ID를 매핑하는 Map
        Map<String, String> subMenuIds = new HashMap<>();
        // 사용자 관리
        subMenuIds.put("전체사용자", System.getProperty("menu_관리_사용자_전체사용자"));
        subMenuIds.put("부서별", System.getProperty("menu_관리_사용자_부서별"));
        subMenuIds.put("전체 관리자", System.getProperty("menu_관리_사용자_전체관리자"));
        subMenuIds.put("부서 관리", System.getProperty("menu_관리_사용자_부서관리"));
        subMenuIds.put("직급 관리", System.getProperty("menu_관리_사용자_직급관리"));
        
        // 신청
        subMenuIds.put("사용자 신청 신규 등록", System.getProperty("menu_관리_신청서_사용자_신규등록"));
        subMenuIds.put("사용자 신청 결과 조회", System.getProperty("menu_관리_신청서_사용자_결과조회"));
        subMenuIds.put("IP 신규/반납", System.getProperty("menu_관리_신청서_IP_IP신규반납"));
        subMenuIds.put("IP 장비 변경", System.getProperty("menu_관리_신청서_IP_장비변경"));
        subMenuIds.put("사용자 변경", System.getProperty("menu_관리_신청서_IP_사용자변경"));
        subMenuIds.put("IP 사용 신청 결과 조회", System.getProperty("menu_관리_신청서_IP_결과조회"));
        subMenuIds.put("단계별 승인", System.getProperty("menu_관리_신청서_IP_단계별승인"));
        subMenuIds.put("단계별 IP 신규", System.getProperty("menu_관리_신청서_IP_단계별승인_IP신규"));
        subMenuIds.put("단계별 IP 반납", System.getProperty("menu_관리_신청서_IP_단계별승인_IP반납"));
        subMenuIds.put("단계별 IP 장비 변경", System.getProperty("menu_관리_신청서_IP_단계별승인_장비변경"));
        subMenuIds.put("단계별 사용자 변경", System.getProperty("menu_관리_신청서_IP_단계별승인_사용자변경"));
        subMenuIds.put("장치 사용 신청 신규 등록", System.getProperty("menu_관리_신청서_장치사용_신규등록"));
        subMenuIds.put("장치 사용 신청 결과 조회", System.getProperty("menu_관리_신청서_장치사용_결과조회"));
        subMenuIds.put("에이전트 인증코드 발급", System.getProperty("menu_관리_신청서_에이전트인증코드_발급"));
        subMenuIds.put("에이전트 인증 결과 조회", System.getProperty("menu_관리_신청서_에이전트인증코드_결과조회"));
        
        for (int k = 0; k <= setup.retryCount(); k++) {
            String currentSubMenu = "";   // 현재 클릭한 하위 메뉴를 저장할 변수
            try {
                driver.get(System.getProperty("testCloudURL") + "/mc2/faces/mainFrame.xhtml");
                String windowHandle = driver.getWindowHandle();
                
                focuseTop(windowHandle);

                mouseMoveTopMenu(System.getProperty("menu_관리"));
                
                // [관리] 메뉴의 하위 메뉴 선택
                switch (메인메뉴) {
                    case "노드":
                        selectTopSubMenu(System.getProperty("menu_관리_노드"));
                        focuseMainBody(windowHandle);
                        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(By.id("pageTitle")));
                        assertTrue(driver.findElement(By.id("pageTitle")).isDisplayed());
                        break;
                    case "IP 주소":
                        selectTopSubMenu(System.getProperty("menu_관리_IP주소"));
                        focuseMainBody(windowHandle);
                        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(By.id("pageTitle")));
                        assertTrue(driver.findElement(By.id("pageTitle")).isDisplayed());
                        break;
                    case "스위치":
                        selectTopSubMenu(System.getProperty("menu_관리_스위치"));
                        focuseMainBody(windowHandle);
                        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(By.id("pageTitle")));
                        assertTrue(driver.findElement(By.id("pageTitle")).isDisplayed());
                        break;
                    case "무선랜":
                        selectTopSubMenu(System.getProperty("menu_관리_무선랜"));
                        focuseMainBody(windowHandle);
                        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(By.id("pageTitle")));
                        assertTrue(driver.findElement(By.id("pageTitle")).isDisplayed());
                        break;
                    case "사용자":
                        selectTopSubMenu(System.getProperty("menu_관리_사용자"));
                        focuseTree(windowHandle);
                        // 하위 메뉴 펼침
                        driver.findElement(By.xpath("//*[@id='node_BEB']/i")).click();
                        Thread.sleep(setup.testdelaytime);
                        for (String subUserMenu : subUserMenus) {
                            focuseTree(windowHandle);
                            // [사용자 관리]의 부메뉴 클릭
                            String subMenuId = subMenuIds.get(subUserMenu);
                            // 부메뉴가 표시될 때까지 대기
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.linkText(subMenuId)));
                            // 부메뉴가 표시되면 클릭
                            driver.findElement(By.linkText(subMenuId)).click();
                            
                            focuseMainBody(windowHandle);
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tit")));
                            
                            assertTrue(driver.findElement(By.cssSelector(".tit")).isDisplayed());
                            if (subUserMenu != "전체사용자") {
                                driver.navigate().back(); // 페이지 뒤로가기
                            }
                            currentSubMenu = subUserMenu; // 현재 클릭한 하위 메뉴 저장
                        }
                        break;
                    case "신청":
                        selectTopSubMenu(System.getProperty("menu_관리_신청서"));
                        focuseTree(windowHandle);
                        for (String subRequestMenu : subRequestMenus) {
                            focuseTree(windowHandle);
                            // [사용자 관리]의 부메뉴 클릭
                            String subMenuId = subMenuIds.get(subRequestMenu);
                            // 부메뉴가 표시될 때까지 대기
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.id(subMenuId)));
                            // 부메뉴가 표시되면 클릭
                            driver.findElement(By.id(subMenuId)).click();
                            
                            focuseMainBody(windowHandle);
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tit")));
                            
                            assertTrue(driver.findElement(By.cssSelector(".tit")).isDisplayed());
                            if (subRequestMenu != "사용자 신청 신규 등록") {
                                driver.navigate().back(); // 페이지 뒤로가기
                            }
                            currentSubMenu = subRequestMenu; // 현재 클릭한 하위 메뉴 저장
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
                    logFailWithMenuInfo(e, screenshotPath, currentSubMenu);}
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