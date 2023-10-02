package com.genians.util;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class CustomExtentReports  {
    
    private static ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    public static ExtentTest getExtentTest() {
        return extentTestThreadLocal.get();
    }

    public static void setExtentTest(ExtentTest extentTest) {
        extentTestThreadLocal.set(extentTest);
    }

    public void logInfo(String message) {
        ExtentTest extentTest = getExtentTest();
        if (extentTest != null) {
            extentTest.log(Status.INFO, message);
        }
    }

}
