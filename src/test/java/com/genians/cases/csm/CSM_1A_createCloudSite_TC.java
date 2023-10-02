package com.genians.cases.csm;

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
    CSM_1A_createCloudSite_01.class,
    CSM_1A_createCloudSite_02.class,
})

public class CSM_1A_createCloudSite_TC {
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
            extentTest = extentReports.createTest("CSM_1A_createCloudSite_TC", "CSM에서 TagetVersion 사이트 생성이 정상적으로 동작하는지 확인한다.");
            extentTest.assignCategory("csm");
            
            //CustomExtentReports 클래스를 사용하여 extentTest를 설정
            CustomExtentReports.setExtentTest(extentTest);
        }
    }
    
    @AfterClass
    public static void tearDown() {
        //extentReports.flush();
        //ExtentManager.closeInstance(); //리포트 인스턴스 종료
    }
}
