package com.genians.action.node;

import java.time.Duration;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.genians.action.GnTestBase;
import com.genians.common.ScreenCapture;
import com.genians.setup.setup;


public class 노드존재여부확인 extends GnTestBase {

	@Test
	public void test노드존재여부확인() throws Exception {
		String 검색방법 = System.getProperty("노드존재여부확인_검색방법", "IP");
		String 노드IP = System.getProperty("노드존재여부확인_노드IP", System.getProperty("host"));
		String 노드MAC = System.getProperty("노드존재여부확인_노드MAC", "");
		String 플랫폼 = System.getProperty("노드존재여부확인_노드플랫폼", "");
		String 호스트명 = System.getProperty("노드존재여부확인_호스트명", "");
		String 인증사용자 = System.getProperty("노드존재여부확인_인증사용자", "");
		String 존재 = System.getProperty("노드존재여부확인_존재", "");
		String 미존재 = System.getProperty("노드존재여부확인_미존재", "");
        
        logInfo(
            "[관리 > 노드관리]로 이동한다.<br>" +
            "노드목록에 " + 검색방법 + "가(이) '" + 존재 + "'인 노드가 존재하는 것을 확인한다.<br>" +
            "노드목록에 " + 검색방법 + "가(이) '" + 미존재 + "'인 노드가 존재하지 않는 것을 확인한다.<br>");
			
		for (int k = 0; k <= setup.retryCount(); k++) {
			try {
				driver.get(System.getProperty("testCloudURL") + "/mc2/faces/mainFrame.xhtml");
				String windowHandle = driver.getWindowHandle();
				
				focuseTop(windowHandle);
				
				mouseMoveTopMenu(System.getProperty("menu_관리"));
				
				selectTopSubMenu(System.getProperty("menu_관리_노드"));
				
				focuseTree(windowHandle);
				
				selectTreeNode(System.getProperty("menu_관리_전체노드"));
				
				focuseMainBody(windowHandle);
				
				new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.and(
					ExpectedConditions.presenceOfElementLocated(By.id("pageTitle")),
					ExpectedConditions.presenceOfElementLocated(By.cssSelector(".detailInfoText"))
				));
					
				if (존재.equals("") && 미존재.equals("")) {
					throw new Exception("파라미터 설정 오류");
				}
				if (검색방법.equals("IP")) {
					if (존재 != "") {
						if (this.checkIpExist(존재) == false) {
							throw new Exception("노드목록에 IP가 '" + 존재 + "'인 노드가 존재하지 않음");
						}
					}
					if (미존재 != "") {
						if (this.checkIpExist(미존재) == true) {
							throw new Exception("노드목록에 IP가 '" + 미존재 + "'인 노드가 존재함");
						}
					}
				} else if (검색방법.equals("MAC")) {
					if (존재 != "") {
						if (this.checkMacExist(존재) == false) {
							throw new Exception("노드목록에 MAC이 '" + 존재 + "'인 노드가 존재하지 않음");
						}
					}
					if (미존재 != "") {
						if (this.checkMacExist(미존재) == true) {
							throw new Exception("노드목록에 MAC이 '" + 미존재 + "'인 노드가 존재함");
						}
					}
				} else if (검색방법.equals("switchName")) {
					if (존재 != "") {
						if (this.checkswitchNameExist(존재, 노드IP, 노드MAC) == false) {
							throw new Exception("노드목록에 접속장치 '" + 존재 + "'인 노드가 존재하지 않음");
						}
					}
					if (미존재 != "") {
						if (this.checkswitchNameExist(미존재, 노드IP, 노드MAC) == true) {
							throw new Exception("노드목록에 접속장치 '" + 미존재 + "'인 노드가 존재함");
						}
					}
				} else if (검색방법.equals("switchPort")) {
					if (존재 != "") {
						if (this.checkswitchPortExist(존재, 노드IP, 노드MAC) == false) {
							throw new Exception("노드목록에 접속포트 '" + 존재 + "'인 노드가 존재하지 않음");
						}
					}
					if (미존재 != "") {
						if (this.checkswitchPortExist(미존재, 노드IP, 노드MAC) == true) {
							throw new Exception("노드목록에 접속포트 '" + 미존재 + "'인 노드가 존재함");
						}
					}
				} else if (검색방법.equals("플랫폼")) {
					if (존재 != "") {
						if (this.checkplatformHExist(노드IP, 플랫폼) == false) {
							throw new Exception("노드목록에 " + 노드IP + " 의 " + 플랫폼 + " 이 존재하지 않음");
						}
					}
					if (미존재 != "") {
						if (this.checkplatformHExist(노드IP, 플랫폼) == true) {
							throw new Exception("노드목록에 " + 노드IP + " 의 " + 플랫폼 + " 이 존재함");
						}
					}
				} else if (검색방법.equals("호스트명")) {
					if (존재 != "") {
						if (this.checkhostnameExist(노드IP, 호스트명) == false) {
							throw new Exception("노드목록에 " + 노드IP + " 의 " + 호스트명 + " 이 존재(일치)하지 않음");
						}
					}
					if (미존재 != "") {
						if (this.checkhostnameExist(노드IP, 호스트명) == true) {
							throw new Exception("노드목록에 " + 노드IP + " 의 " + 호스트명 + " 이 존재(일치)함");
						}
					}
				} else if (검색방법.equals("인증사용자")) {
					if (존재 != "") {
						if (this.checkauthUserExist(노드IP, 인증사용자) == false) {
							throw new Exception("노드목록에 " + 노드IP + " 의 " + 인증사용자 + " 이 존재하지 않음");
						}
					}
					if (미존재 != "") {
						if (this.checkauthUserExist(노드IP, 인증사용자) == true) {
							throw new Exception("노드목록에 " + 노드IP + " 의 " + 인증사용자 + " 이 존재함");
						}
					}
				} else {
					throw new Exception("파라미터 설정 오류");
				}
				logPass("결과: 기대 결과대로 동작");
				break;
			} catch (Throwable e) {
				if (k == setup.retryCount()) {
					new ScreenCapture().takeScreenShot(driver, System.getProperty("className") + "__" + getClass().getSimpleName() + ".png");
					String screenshotPath = "screenshots/" + System.getProperty("className") + "__" + getClass().getSimpleName() + ".png";
					logFail(e, screenshotPath);
				}
			}
			setup.doRetrySetup();
		}
	}

	private boolean checkIpExist(String IP) {
		int rowCount = driver.findElements(By.xpath("//tbody[@id='form1:dataTable1_data']/tr")).size();
		boolean checkFlag_match = false;
		for (int i = 0; i <= rowCount; i++ ) {
			String element = "form1:dataTable1:" + i + ":txtIp";
			if (this.isElementPresent(By.id(element)) == true) {
				if (IP.equals(driver.findElement(By.id(element)).getText())) {
					checkFlag_match = true;
					break;
				}
			}
		}
		return checkFlag_match;
	}
	
	private boolean checkMacExist(String MAC) {
		int rowCount = driver.findElements(By.xpath("//tbody[@id='form1:dataTable1_data']/tr")).size();
		boolean checkFlag_match = false;
		for (int i = 0; i <= rowCount; i++ ) {
			String element = "form1:dataTable1:" + i + ":txt_macCol";
			String getTextFromMacCol = null;
			if (this.isElementPresent(By.id(element + "2")) == true) {
				getTextFromMacCol = driver.findElement(By.id(element + "2")).getText();
			} else if (this.isElementPresent(By.id(element + "1")) == true) {
				getTextFromMacCol = driver.findElement(By.id(element + "1")).getText();
			}
			if (MAC.equals(getTextFromMacCol)) {
				checkFlag_match = true;
				break;
			}
		}
		return checkFlag_match;
	}
	// 스위치 명을 기준으로 노드리스트에서 입력받은 스위치 명이 존재하는지 확인, Element의 존재여부를 확인하고, 존재할 경우 텍스트를 검사
	private boolean checkswitchNameExist(String switchName, String IP, String MAC) { 
		int rowCount = driver.findElements(By.xpath("//tbody[@id='form1:dataTable1_data']/tr")).size();
		boolean checkFlag_match = false;
		for (int i = 0; i <= rowCount; i++ ) {
			String element = "form1:dataTable1:" + i + ":lnk_switchCol1";
			String element_ipCol = "form1:dataTable1:" + i + ":txtIp";
			String element_macCol = "form1:dataTable1:" + i + ":txt_macCol2";
			if (IP != "") {
				if (this.isElementPresent(By.id(element_ipCol)) == true) {
					if (IP.equals(driver.findElement(By.id(element_ipCol)).getText())) {
						if (this.isElementPresent(By.id(element)) == true) {
							if (switchName.equals(driver.findElement(By.id(element)).getText())) {
								checkFlag_match = true;
								break;
							}
						}
					}
				}
			} else if (MAC != "") {
				if (MAC.equals(driver.findElement(By.id(element_macCol)).getText())) {
					if (MAC.equals(driver.findElement(By.id(element_macCol)).getText())) {
						if (this.isElementPresent(By.id(element)) == true) {
							if (switchName.equals(driver.findElement(By.id(element)).getText())) {
								checkFlag_match = true;
								break;
							}
						}
					}
				}
			}
			
		}
		return checkFlag_match;
	}
	// 스위치 포트을 기준으로 노드리스트에서 입력받은 스위치 포트이 존재하는지 확인, Element의 존재여부를 확인하고, 존재할 경우 텍스트를 검사
	private boolean checkswitchPortExist(String switchPort, String IP, String MAC) {
		int rowCount = driver.findElements(By.xpath("//tbody[@id='form1:dataTable1_data']/tr")).size();
		boolean checkFlag_match = false;
		for (int i = 0; i <= rowCount; i++ ) {
			String element = "form1:dataTable1:" + i + ":lnk_portCol1";
			String element_ipCol = "form1:dataTable1:" + i + ":txtIp";
			String element_macCol = "form1:dataTable1:" + i + ":txt_macCol2";
			if (IP != "") {
				if (this.isElementPresent(By.id(element_ipCol)) == true) {
					if (IP.equals(driver.findElement(By.id(element_ipCol)).getText())) {
						if (this.isElementPresent(By.id(element)) == true) {
							if (switchPort.equals(driver.findElement(By.id(element)).getText())) {
								checkFlag_match = true;
								break;
							}
						}
					}
				}
			} else if (MAC != "") {
				if (MAC.equals(driver.findElement(By.id(element_macCol)).getText())) {
					if (MAC.equals(driver.findElement(By.id(element_macCol)).getText())) {
						if (this.isElementPresent(By.id(element)) == true) {
							if (switchPort.equals(driver.findElement(By.id(element)).getText())) {
								checkFlag_match = true;
								break;
							}
						}
					}
				}
			}
			
		}
		return checkFlag_match;
	}
	
	private boolean checkplatformHExist(String IP, String 플랫폼) {
		int rowCount = driver.findElements(By.xpath("//tbody[@id='form1:dataTable1_data']/tr")).size();
		boolean checkFlag_match = false;
		for (int i = 0; i <= rowCount; i++ ) {
			String element = "form1:dataTable1:" + i + ":lnk_platformCol";
			String element_ipCol = "form1:dataTable1:" + i + ":txtIp";
			if (this.isElementPresent(By.id(element_ipCol)) == true) {
				if (IP.equals(driver.findElement(By.id(element_ipCol)).getText())) {
					if (this.isElementPresent(By.id(element)) == true) {
						if (플랫폼.equals(driver.findElement(By.id(element)).getText())) {
							checkFlag_match = true;
							break;
						}
					}
				}
			}
		}
		return checkFlag_match;
	}
	
	private boolean checkauthUserExist(String IP, String 인증사용자) {
		int rowCount = driver.findElements(By.xpath("//tbody[@id='form1:dataTable1_data']/tr")).size();
		boolean checkFlag_match = false;
		for (int i = 0; i <= rowCount; i++ ) {
			String element = "form1:dataTable1:" + i + ":txt_authusername2";
			String element_ipCol = "form1:dataTable1:" + i + ":txtIp";
			if (this.isElementPresent(By.id(element_ipCol)) == true) {
				if (IP.equals(driver.findElement(By.id(element_ipCol)).getText())) {
					if (this.isElementPresent(By.id(element)) == true) {
						if (인증사용자.equals(driver.findElement(By.id(element)).getText())) {
							checkFlag_match = true;
							break;
						}
					}
				}
			}
		}
		return checkFlag_match;
	}
	
	private boolean checkhostnameExist(String IP, String 호스트명) {
		int rowCount = driver.findElements(By.xpath("//tbody[@id='form1:dataTable1_data']/tr")).size();
		boolean checkFlag_match = false;
		for (int i = 0; i <= rowCount; i++ ) {
			String element = "form1:dataTable1:" + i + ":txt_nameCol";
			String element_ipCol = "form1:dataTable1:" + i + ":txtIp";
			if (this.isElementPresent(By.id(element_ipCol)) == true) {
				if (IP.equals(driver.findElement(By.id(element_ipCol)).getText())) {
					if (this.isElementPresent(By.id(element)) == true) {
						if (호스트명.equals(driver.findElement(By.id(element)).getText())) {
							checkFlag_match = true;
							break;
						}
					}
				}
			}
		}
		return checkFlag_match;
	}
}