package com.genians.action.etc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.genians.action.GnTestBase;
import com.genians.common.ScreenCapture;
import com.genians.setup.setup;


public class 명령어실행 extends GnTestBase {
	private Exception ex = null;
	
	@Test 
	public void test명령어실행() throws Exception {
		String 명령어 = System.getProperty("명령어실행_명령어", "");
		String 출력내용확인 = System.getProperty("명령어실행_출력내용확인", "Off");
		final String 출력 = System.getProperty("명령어실행_출력", "");
		final String 출력2 = System.getProperty("명령어실행_출력2", "");
		final String 출력3 = System.getProperty("명령어실행_출력3", "");
		final String 미출력 = System.getProperty("명령어실행_미출력", "");
		final String 미출력2 = System.getProperty("명령어실행_미출력2", "");
		final String 미출력3 = System.getProperty("명령어실행_미출력3", "");
		String timeout = System.getProperty("명령어실행_timeout", "30000");
		String Str = "";

		if (출력내용확인.equals("On")) {
			Str = "명령 프롬프트 창에 출력되는 내용을 " + Integer.parseInt(timeout) / 1000 + "초 동안 확인한다.<br>" +
					"<TABLE cellspacing='1' bgcolor='gray' style='MARGIN-TOP:2;MARGIN-LEFT:5'>" +
					"<tr><td>출력</td><td>" + 출력 + "<br>" + 출력2 + "</td>" +
					"<tr><td>미출력</td><td>" + 미출력 + "<br>" + 미출력2 + "</td>" +
					"</TABLE>";
		}
		logInfo(
			"명령 프롬프트 창에서 다음 명령어(" +  명령어 + ")를 실행한다.<br>" + Str);
			
		for (int k = 0; k <= setup.retryCount(); k++) {
			try {
				final Process pr = new ProcessBuilder("cmd", "/C", 명령어).redirectErrorStream(true).start();
				
				if (출력내용확인.equals("On")) {
					Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
						public void uncaughtException(Thread th, Throwable ex_t) {
							ex = (Exception) ex_t;
						}
					};
					Thread thread = new Thread(new Runnable() {
						public void run() {
							BufferedReader br = null;
							try {
								br = new BufferedReader(new InputStreamReader(pr.getInputStream(), "euc-kr"));
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							StringBuffer sb = new StringBuffer();
							String line = "";
							boolean checkFlag = false;
							boolean checkFlag2 = false;
							boolean checkFlag3 = false;
							try {
								while ((line = br.readLine()) != null) {
									sb.append(line + "\n");
									if (출력 != "" & 출력2 != "" & 출력3 != "") {
										if (line.matches(출력)) {
											checkFlag = true;
										}
										if (line.matches(출력2)) {
											checkFlag2 = true;
										}
										if (line.matches(출력3)) {
											checkFlag3 = true;
										}
									} else if (출력 != "" & 출력2 != "") {
										checkFlag3 = true;
										if (line.matches(출력)) {
											checkFlag = true;
										}
										if (line.matches(출력2)) {
											checkFlag2 = true;
										}
									} else if (출력 != "") {
										checkFlag2 = true;
										checkFlag3 = true;
										if (line.matches(출력)) {
											checkFlag = true;
										}
									} else {
										checkFlag = true;
										checkFlag2 = true;
										checkFlag3 = true;
									}
									if (미출력 != "") {
										if ((line.matches(미출력))) {
											throw new RuntimeException("'" + 미출력 + "' 출력됨. \n\n " + sb);
										}
									}
									if (미출력2 != "") {
										if ((line.matches(미출력2))) {
											throw new RuntimeException("'" + 미출력2 + "' 출력됨. \n\n " + sb);
										}
									}
									if (미출력3 != "") {
										if ((line.matches(미출력3))) {
											throw new RuntimeException("'" + 미출력3 + "' 출력됨. \n\n " + sb);
										}
									}
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
							if (checkFlag == false) {
								throw new RuntimeException("'" + 출력 + "' 출력되지 않음. \n\n " + sb);
							}
							if (checkFlag2 == false) {
								throw new RuntimeException("'" + 출력2 + "' 출력되지 않음. \n\n " + sb);
							}
							if (checkFlag3 == false) {
								throw new RuntimeException("'" + 출력3 + "' 출력되지 않음. \n\n " + sb);
							}
						}
					});
					thread.setUncaughtExceptionHandler(h);
					thread.start();
					thread.join(Integer.parseInt(timeout));
					if (ex != null) {
						throw ex;
					}
				} else if (출력내용확인 != "Off") {
					throw new Exception("파라미터 설정 오류");
				}
				logPass("결과: 기대 결과대로 동작");
			} catch (Exception e) {
				if (k == setup.retryCount()) {
					new ScreenCapture().takeScreenShot(driver, System.getProperty("className") + "__" + getClass().getSimpleName() + ".png");
					String screenshotPath = "screenshots/" + System.getProperty("className") + "__" + getClass().getSimpleName() + ".png";
					logFail(e, screenshotPath);
				}
			}
			setup.doRetrySetup();
		}
	}
}
	