package com.genians.common;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

import com.genians.setup.setup;

public class ScreenCapture {

	public String takeScreenShot(WebDriver driver, String fileName) {
		try {
			WebDriver augmentedDriver = new Augmenter().augment(driver);
			File scrFile = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
			//String imgPath = "./reports/Screenshots/" + fileName + ".png";
			String imgPath = "screenshots/" + fileName;
			File path = new File(setup.reportDir + imgPath);
			FileUtils.copyFile(scrFile, path);
            return imgPath; // 파일 경로를 반환
		} catch (Exception e) {
			e.printStackTrace();
            return ""; // 예외 발생 시 빈 문자열 반환 또는 예외 처리 방식을 수정하여 반환
		}
	}
}