package com.genians.util;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.genians.setup.setup;

public class ExtentManager {
    private static ExtentReports extentReports;
    private static ExtentSparkReporter sparkReporter;

    private ExtentManager() {
    }
    
    public synchronized static ExtentReports getInstance() {
        if (extentReports == null) {
            // ExtentSparkReporter 생성 및 설정
            String reportFileName = setup.reportDir + System.getProperty("targetJiraNum") + "_" + System.getProperty("revision") + "_extent.html"; // 리포트 파일 이름 설정
            sparkReporter = new ExtentSparkReporter(reportFileName);
            sparkReporter.config().setEncoding("UTF-8");
            
            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
        }
        return extentReports;
    }
    
    public synchronized static void closeInstance() {
        if (extentReports != null) {
            extentReports.flush(); //보고서 작성 종료
            extentReports = null; //인스턴스 null로 설정
        }
    }
}
