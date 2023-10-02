package com.genians.action.csm;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.genians.common.ScreenCapture;
import com.genians.common.WebDriverStatusCheck;
import com.genians.action.GnTestBase;
import com.genians.setup.setup;


public class CSM_Login extends GnTestBase {
    
    @Test
    public void testCSM_Login() throws Exception {
        String baseUrl = System.getProperty("CSM_Login_baseUrl", "https://" + System.getProperty("testTarget"));
        String ID = System.getProperty("CSM_Login_ID", "");
        String PW = System.getProperty("CSM_Login_PW", "");
        String role = System.getProperty("CSM_Login_role", "");
        String errorMessage = System.getProperty("CSM_Login_errorMessage", "");
        
        logInfo(
            "CSM 로그인 페이지(" +  baseUrl +  "/login)로 접속한다.<br>" +
            "Email(" + ID + ") 및 Password(" + PW + ")를 입력하고 로그인 버튼을 클릭한다.<br>");
            
            
        for (int k = 0; k <= setup.retryCount(); k++) {
            try {
                if (role == "") {
                    throw new Exception("CSM 인증사용자 권한 정의되지 않음");
                }
                
                driver.get(baseUrl + "/login");
                
                // CSM 로그인
                this.tryCSMLogin(ID, PW, errorMessage);	
                if (role.equals("admin")) {
                    tryOTPLogin("normal", errorMessage);
                }
                
                // 중복로그인 창이 발생하는 경우 확인.
                isDuplicateLogin();
                
                // 로그인 성공 check
                isCSMLoginSuccess();
                logPass("결과: 기대 결과대로 동작");
                break;
            } catch (Throwable e) {
                if (k == setup.retryCount()) {
                    new ScreenCapture().takeScreenShot(driver, System.getProperty("className") + "__" + getClass().getSimpleName() + ".png");
                    String screenshotPath = "screenshots/" + System.getProperty("className") + "__" + getClass().getSimpleName() + ".png";
                    logFail(e, screenshotPath);;
                }
            }
            setup.doRetrySetup();
        }
    }
    
    private void tryCSMLogin(String ID, String PW, String errorMessage) throws Exception {
        if (!WebDriverStatusCheck.loopCheckElementByID(driver, "user_login")) {
            throw new Exception("로그인 페이지 출력되지 않음");
        }
        driver.findElement(By.id("user_login")).clear();
        driver.findElement(By.id("user_login")).sendKeys(ID);
        driver.findElement(By.id("user_pass")).clear();
        driver.findElement(By.id("user_pass")).sendKeys(PW);
        int random = new Random().nextInt(2);
        doLogin(random);
        
        if (errorMessage != "") {
            boolean checkFlag = false;
            driver.manage().timeouts().implicitlyWait(setup.implicitlyWait, TimeUnit.SECONDS);
            String element = "//div[contains(@class, 'alert alert-danger')]";
            if (WebDriverStatusCheck.loopCheckElementByXpath(driver, element)) {
                if (driver.findElement(By.xpath(element)).getText().matches(errorMessage)) {
                    checkFlag = true;
                }
            }
            driver.manage().timeouts().implicitlyWait(setup.implicitlyWait, TimeUnit.SECONDS);
            if (checkFlag == false) {
                throw new Exception("로그인 실패 메시지(" + errorMessage + ") 출력되지 않음");
            }
        }
    }
    
    private void tryOTPLogin(String testType, String errorMessage) throws Exception {
        String otpSecret = System.getProperty("testAdminOTP");
        if (!WebDriverStatusCheck.loopCheckElementByID(driver, "otp_code")) {
            throw new Exception("OTP 입력창 출력되지 않음.");
        }
        driver.findElement(By.id("otp_code")).clear();
        if (testType.equals("5failtest")) {
            driver.findElement(By.id("otp_code")).sendKeys("111111");
        } else {
            driver.findElement(By.id("otp_code")).sendKeys(setup.getTwoFactorCode(otpSecret));
        }
        driver.findElement(By.id("otp_submit")).click();
        
        if (errorMessage != "") {
            boolean checkFlag = false;
            driver.manage().timeouts().implicitlyWait(setup.implicitlyWait, TimeUnit.SECONDS);
            String element = "//div[contains(@class, 'alert alert-danger')]";
            if (WebDriverStatusCheck.loopCheckElementByXpath(driver, element)) {
                if (driver.findElement(By.xpath(element)).getText().matches(errorMessage)) {
                    checkFlag = true;
                }
            }
            driver.manage().timeouts().implicitlyWait(setup.implicitlyWait, TimeUnit.SECONDS);
            if (checkFlag == false) {
                throw new Exception("오류메시지(" + errorMessage + ") 출력되지 않음");
            }
        }
    }
    
    private void isDuplicateLogin() throws Exception {
        // User login duplicate check
        String duplicateElement = "duplicate_login_bt";
        if (WebDriverStatusCheck.loopCheckElementByID(driver, duplicateElement)) {
            driver.findElement(By.id("duplicate_login_y")).click();
            driver.findElement(By.id("duplicate_login_bt")).click();
        }
    }
    
    private void isCSMLoginSuccess() throws Exception {
        driver.manage().timeouts().implicitlyWait(setup.implicitlyWait, TimeUnit.SECONDS);
        String element = "//*[@id='try_cloud']";
        if (!WebDriverStatusCheck.loopCheckElementByXpath(driver, element)) {
            throw new Exception("CSM 사용자 로그인되지 않음.");
        }
    }
    
    public void doLogin (int random) {
        if (random == 0) {
            driver.findElement(By.id("user_pass")).sendKeys(Keys.ENTER);
        } else {
            driver.findElement(By.id("login")).click();
        }
    }
    
}
