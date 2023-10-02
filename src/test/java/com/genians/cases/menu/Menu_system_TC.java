package com.genians.cases.menu;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import com.genians.util.CustomExtentReports;
import com.genians.util.ExtentManager;
import com.genians.setup.setup;


@RunWith(Suite.class)
@SuiteClasses({ 
    Menu_system_01.class,
    Menu_system_02.class,
    Menu_system_03.class,
})

public class Menu_system_TC {
    private static ExtentReports extentReports;
    public static ExtentTest extentTest;

    @BeforeClass
    public static void setUp() throws Exception {
        try {
            throw new Exception();
        }
        catch(Exception e) {
            setup.main();
            
            StackTraceElement[] sTrace = e.getStackTrace();
            // sTrace[0] will be always there
            String className = sTrace[0].getClassName().replaceAll("\\.", "/");
            System.setProperty("className", className);
            
            // 이미 생성된 extentReports 인스턴스를 사용
            extentReports = ExtentManager.getInstance();
            extentTest = extentReports.createTest("Menu_system_TC", "관리콘솔의 [시스템] 메뉴를 클릭하여 페이지의 화면이 정상 표시되는지 확인한다.");
            extentTest.assignCategory("menu");
            
            //CustomExtentReports 클래스를 사용하여 extentTest를 설정
            CustomExtentReports.setExtentTest(extentTest);
        }
    }
    
    @AfterClass
    public static void tearDown() {
        //ExtentManager.closeInstance(); //리포트 인스턴스 종료
    }
}
