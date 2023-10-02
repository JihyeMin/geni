package com.genians.cases;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import com.genians.util.CustomExtentReports;
import com.genians.util.ExtentManager;
import com.genians.setup.setup;

import com.genians.action.etc.관리자인증;
import com.genians.action.etc.관리자로그아웃;

@RunWith(Suite.class)
@SuiteClasses({ 
    관리자인증.class,
    관리자로그아웃.class,
})

public class Sampe_TC {
    private static ExtentReports extentReports;
    public static ExtentTest extentTest;
    
    @BeforeClass
    public static void setUp() throws Exception {
        setup.main();
        extentReports = ExtentManager.getInstance();
        extentTest = extentReports.createTest("Sampe_TC", "관리자 로그인/로그아웃이 정상동작함을 확인한다.");
        extentTest.assignCategory("Sample");
        
        //CustomExtentReports 클래스를 사용하여 extentTest를 설정
        CustomExtentReports.setExtentTest(extentTest);
    }
    
    @AfterClass
    public static void tearDown() {
        ExtentManager.closeInstance(); //리포트 인스턴스 종료
    }
    
    public static void logTestStatus(Status status, String message) {
        //CustomExtentReports 클래스의 logInfo 메소드를 사용하여 로그를 기록
        CustomExtentReports.getExtentTest().log(status, message);
    }
    
    public static void logTestInfo(String message) {
        //CustomExtentReports 클래스의 logInfo 메소드를 사용하여 로그를 기록
        CustomExtentReports.getExtentTest().info(message);
    }
    
    public static void logTestFail(String message) {
        //CustomExtentReports 클래스의 logInfo 메소드를 사용하여 로그를 기록
        CustomExtentReports.getExtentTest().fail(message);
    }
}
