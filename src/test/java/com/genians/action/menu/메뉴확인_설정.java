package com.genians.action.menu;

import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.genians.setup.setup;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.genians.action.GnTestBase;
import com.genians.common.ScreenCapture;


public class 메뉴확인_설정 extends GnTestBase {
    
    @Test
    public void test메뉴확인_설정() throws Exception {
        String 메인메뉴 = System.getProperty("메뉴확인_설정_메인메뉴", "");
        
        logInfo("관리콘솔의 [설정] 메뉴에서 메인메뉴(" + 메인메뉴 + ")를 클릭하여 페이지의 화면이 정상 표시되는지 확인한다.<br>");
        
        String[] subPropertiesMenus = {
            // 속성 관리
            "노드타입 관리",
            "태그 관리",
            // 속성 관리 > 추가 필드 관리 (펼침 메뉴)
            "노드",
            "장비",
            "사용자",
            "IP 신청서",
            "장비 수명주기 관리",
            "IP",
            "MAC",
            // 속성 관리 > 용도 관리 (펼침 메뉴)
            "사용자 용도",
            // 속성 관리 > 용도 관리 (펼침 메뉴) > IP 용도 (펼침 메뉴)
            "IP 신규",
            "IP 반납",
            "장비 변경",
            "사용자 변경",
        };
        String [] subCWPMenus = {
            // 접속 인증 페이지
            "디자인 템플릿",
            "공지사항",
            "메시지 관리",
            // 동의 페이지 (펼침 메뉴)
            "사용자정보",
            "보안서약",
            "사용자 정의 버튼",
            "CWP 설정"
        };
        String[] subUserAuthMenus = {
            // 사용자 인증
            "사용자 관리",
            "사용자 인증",
            "인증 연동",
            "정보 동기화",
            "관리 역할"
        };
        String[] subGeneralMenus = {
            // 노드 관리 (펼침 메뉴)
            "노드 관리",
            "포트 스캔",
            // IP 관리 (펼침 메뉴)
            "IP 관리",
            "IP 신청 시스템 공지사항",

            "무선랜 관리",
            "감사기록",
            "관리 콘솔",
            "리포트",
            "에이전트",
            "운영체제 업데이트",
            "기타 설정"
        };
        
        // 각 부메뉴와 그에 대한 ID를 매핑하는 Map
        Map<String, String> subMenuIds = new HashMap<>();
        // 속성 관리
        subMenuIds.put("노드타입 관리", System.getProperty("menu_설정_노드타입관리"));
        subMenuIds.put("태그 관리", System.getProperty("menu_설정_태그관리"));
        // 속성 관리 > 추가 필드 관리 (펼침 메뉴)
        subMenuIds.put("노드", System.getProperty("menu_설정_노드추가필드"));
        subMenuIds.put("장비", System.getProperty("menu_설정_장비추가필드")); //??
        subMenuIds.put("사용자", System.getProperty("menu_설정_사용자추가필드"));
        subMenuIds.put("IP 신청서", System.getProperty("menu_설정_IP신청서추가필드"));
        subMenuIds.put("장비 수명주기 관리", System.getProperty("menu_설정_장비수명주기관리추가필드"));
        subMenuIds.put("IP", System.getProperty("menu_설정_IP추가필드"));
        subMenuIds.put("MAC", System.getProperty("menu_설정_MAC추가필드"));
        // 속성 관리 > 용도 관리 (펼침 메뉴)
        subMenuIds.put("사용자 용도", System.getProperty("menu_설정_사용자용도"));
        // 속성 관리 > 용도 관리 (펼침 메뉴) > IP 용도 (펼침 메뉴)
        subMenuIds.put("IP 신규", System.getProperty("menu_설정_IP용도_IP신규"));
        subMenuIds.put("IP 반납", System.getProperty("menu_설정_IP용도_IP반납"));
        subMenuIds.put("장비 변경", System.getProperty("menu_설정_IP용도_장비변경"));
        subMenuIds.put("사용자 변경", System.getProperty("menu_설정_IP용도_사용자변경"));
        // 접속 인증 페이지
        subMenuIds.put("디자인 템플릿", System.getProperty("menu_설정_디자인템플릿"));
        subMenuIds.put("공지사항", System.getProperty("menu_설정_공지사항"));
        subMenuIds.put("메시지 관리", System.getProperty("menu_설정_메시지관리"));
        // 동의 페이지 (펼침 메뉴)
        subMenuIds.put("사용자정보", System.getProperty("menu_설정_사용자정보"));
        subMenuIds.put("보안서약", System.getProperty("menu_설정_보안서약"));
        subMenuIds.put("사용자 정의 버튼", System.getProperty("menu_설정_사용자정의버튼"));
        subMenuIds.put("CWP 설정", System.getProperty("menu_설정_CWP설정"));
        // 서비스
        subMenuIds.put("RADIUS 서버", System.getProperty("menu_설정_RADIUS서버"));
        // 사용자 인증
        subMenuIds.put("사용자 관리", System.getProperty("menu_설정_사용자관리"));
        subMenuIds.put("사용자 인증", System.getProperty("menu_설정_사용자인증_사용자인증"));
        subMenuIds.put("인증 연동", System.getProperty("menu_설정_인증연동"));
        subMenuIds.put("정보 동기화", System.getProperty("menu_설정_사용자정보동기화"));
        subMenuIds.put("관리 역할", System.getProperty("menu_설정_관리역할"));
        // 환경설정 > 노드 관리
        subMenuIds.put("노드 관리", System.getProperty("menu_설정_노드관리"));
        subMenuIds.put("포트 스캔", System.getProperty("menu_설정_포트스캔"));
        // 환경설정 > IP 관리 (펼침 메뉴)
        subMenuIds.put("IP 관리", System.getProperty("menu_설정_IP관리"));
        subMenuIds.put("IP 신청 시스템 공지사항", System.getProperty("menu_설정_IP신청시스템공지사항"));
        subMenuIds.put("무선랜 관리", System.getProperty("menu_설정_무선랜관리"));
        subMenuIds.put("감사기록", System.getProperty("menu_설정_감사기록"));
        subMenuIds.put("관리 콘솔", System.getProperty("menu_설정_관리콘솔"));
        subMenuIds.put("리포트", System.getProperty("menu_설정_리포트"));
        subMenuIds.put("에이전트", System.getProperty("menu_설정_에이전트"));
        subMenuIds.put("운영체제 업데이트", System.getProperty("menu_설정_운영체제업데이트"));
        subMenuIds.put("기타 설정", System.getProperty("menu_설정_기타설정"));
        
        for (int k = 0; k <= setup.retryCount(); k++) {
            String currentSubMenu = "";   // 현재 클릭한 하위 메뉴를 저장할 변수
            try {
                driver.get(System.getProperty("testCloudURL") + "/mc2/faces/mainFrame.xhtml");
                String windowHandle = driver.getWindowHandle();
                
                focuseTop(windowHandle);

                mouseMoveTopMenu(System.getProperty("menu_설정"));
                
                // [설정] 메뉴의 하위 메뉴 선택
                switch (메인메뉴) {
                    case "속성 관리":
                        selectTopSubMenu(System.getProperty("menu_설정_속성관리"));
                        focuseTree(windowHandle);
                        // 하위 메뉴 펼침
                        new Actions(driver).doubleClick(driver.findElement(By.xpath("//a[text()='추가 필드 관리']"))).build().perform();
                        Thread.sleep(setup.testdelaytime);
                        new Actions(driver).doubleClick(driver.findElement(By.xpath("//a[text()='용도 관리']"))).build().perform();
                        Thread.sleep(setup.testdelaytime);
                        new Actions(driver).doubleClick(driver.findElement(By.xpath("//a[text()='IP 용도']"))).build().perform();
                        Thread.sleep(setup.testdelaytime);

                        for (String subPropertiesMenu : subPropertiesMenus) {
                            focuseTree(windowHandle);
                            // [속성 관리]의 부메뉴 클릭
                            String subMenuId = subMenuIds.get(subPropertiesMenu);
                            // 부메뉴가 표시될 때까지 대기
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.linkText(subMenuId)));
                            // 부메뉴 클릭
                            driver.findElement(By.linkText(subMenuId)).click();

                            focuseMainBody(windowHandle);
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tit")));

                            assertTrue(driver.findElement(By.cssSelector(".tit")).isDisplayed());

                            if (subPropertiesMenu != "노드타입 관리") {
                                driver.navigate().back(); // 페이지 뒤로가기
                            }
                            currentSubMenu = subPropertiesMenu; // 현재 클릭한 하위 메뉴 저장
                        }
                        break;
                    case "CWP":
                        selectTopSubMenu(System.getProperty("menu_설정_접속인증페이지"));
                        focuseTree(windowHandle);
                        // 하위 메뉴 펼침
                        //new Actions(driver).doubleClick(driver.findElement(By.xpath("//a[text()='동의 페이지']"))).build().perform();
                        driver.findElement(By.xpath("//*[@id='node_DCF']/i")).click();
                        Thread.sleep(setup.testdelaytime);
                        for (String subCWPMenu : subCWPMenus) {
                            focuseTree(windowHandle);
                            // [CWP]의 부메뉴 클릭
                            String subMenuId = subMenuIds.get(subCWPMenu);
                            // 부메뉴가 표시될 때까지 대기
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.linkText(subMenuId)));
                            // 부메뉴가 표시되면 클릭
                            driver.findElement(By.linkText(subMenuId)).click();
                            focuseMainBody(windowHandle);
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tit")));
                            
                            assertTrue(driver.findElement(By.cssSelector(".tit")).isDisplayed());
                            if (subCWPMenu != "디자인 템플릿") {
                                driver.navigate().back(); // 페이지 뒤로가기
                            }
                            currentSubMenu = subCWPMenu; // 현재 클릭한 하위 메뉴 저장
                        }
                        break;
                    case "서비스":
                        selectTopSubMenu(System.getProperty("menu_설정_서비스"));
                        focuseMainBody(windowHandle);
                        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tit")));
                        assertTrue(driver.findElement(By.cssSelector(".tit")).isDisplayed());
                        currentSubMenu = "서비스"; // 현재 클릭한 하위 메뉴 저장
                        break;
                    case "사용자 인증":
                        selectTopSubMenu(System.getProperty("menu_설정_사용자인증"));
                        Thread.sleep(2000);
                        for (String subUserAuthMenu : subUserAuthMenus) {
                            focuseTree(windowHandle);
                            // [사용자 인증]의 부메뉴 클릭
                            String subMenuId = subMenuIds.get(subUserAuthMenu);
                            if (subUserAuthMenu != "사용자 인증") {
                                // 부메뉴가 표시될 때까지 대기
                                new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.linkText(subMenuId)));
                                // 부메뉴가 표시되면 클릭
                                driver.findElement(By.linkText(subMenuId)).click();
                            } else {
                                // 사용자 인증 이름으로 동일한 이름이 존재함
                                new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='node_DGB']")));
                                driver.findElement(By.xpath("//*[@id='node_DGB']")).click();
                            }
                            
                            focuseMainBody(windowHandle);
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tit")));
                            
                            assertTrue(driver.findElement(By.cssSelector(".tit")).isDisplayed());
                            if (subUserAuthMenu != "사용자 관리") {
                                driver.navigate().back(); // 페이지 뒤로가기
                            }
                            currentSubMenu = subUserAuthMenu; // 현재 클릭한 하위 메뉴 저장
                        }
                        break;
                    case "환경 설정":
                        selectTopSubMenu(System.getProperty("menu_설정_환경설정"));
                        Thread.sleep(2000);
                        focuseTree(windowHandle);
                        // 하위 메뉴 펼침
                        //new Actions(driver).doubleClick(driver.findElement(By.xpath("//a[text()='노드 관리']"))).build().perform();
                        driver.findElement(By.xpath("//*[@id='node_DEA']/i")).click();
                        Thread.sleep(setup.testdelaytime);
                        //new Actions(driver).doubleClick(driver.findElement(By.xpath("//a[text()='IP 관리']"))).build().perform();
                        driver.findElement(By.xpath("//*[@id='node_DEB']/i")).click();
                        Thread.sleep(setup.testdelaytime);
                        for (String subGeneralMenu : subGeneralMenus) {
                            focuseTree(windowHandle);

                            // [환경 설정]의 부메뉴 클릭
                            String subMenuId = subMenuIds.get(subGeneralMenu);
                            // 부메뉴가 표시될 때까지 대기
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.linkText(subMenuId)));
                            // 부메뉴가 표시되면 클릭
                            driver.findElement(By.linkText(subMenuId)).click();
                            focuseMainBody(windowHandle);
                            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tit")));
                            
                            assertTrue(driver.findElement(By.cssSelector(".tit")).isDisplayed());
                            if (subGeneralMenu != "노드 관리") {
                                driver.navigate().back(); // 페이지 뒤로가기
                            }
                            currentSubMenu = subGeneralMenu; // 현재 클릭한 하위 메뉴 저장
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