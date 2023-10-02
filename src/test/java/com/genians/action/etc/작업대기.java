package com.genians.action.etc;

import org.junit.Test;

import com.genians.action.GnTestBase;
import com.genians.common.ScreenCapture;

public class 작업대기 extends GnTestBase {

	@Test
	public void test작업대기() throws Exception {
		String time = System.getProperty("작업대기_time", "2000");
		
		try {
			int i = java.lang.Integer.parseInt(time);
			logInfo(i/1000 + "초 동안 대기한다.<br>");
			Thread.sleep(i);
			logPass("결과: 기대 결과대로 동작");
		} catch (Exception e) {
			new ScreenCapture().takeScreenShot(driver, System.getProperty("className") + "__" + getClass().getSimpleName() + ".png");
			String screenshotPath = "screenshots/" + System.getProperty("className") + "__" + getClass().getSimpleName() + ".png";
			logFail(e, screenshotPath);
		}
	}
}