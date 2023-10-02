package com.genians.action;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.genians.setup.setup;
import com.genians.util.ExtentManager;
import com.genians.util.CustomExtentReports;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.genians.common.ScreenCapture;


public class GnTestBase extends GnActionTestBase {
    protected WebDriver driver;
    protected ExtentReports extentReports;
    protected ExtentTest extentTest;
    
    @Before
    public void Setup() throws Exception {
        setup.main();
        extentReports = ExtentManager.getInstance(); // Extent Report를 공유 객체로 가져옴
        //extentTest = extentReports.createTest(getClass().getSimpleName()); //새로운 Extent 테스트 생성
        
        //테스트 클래스에 따라 테스트 항목 생성
        extentTest = CustomExtentReports.getExtentTest(); //이미 생성된 항목 가져오기
        
        driver = setup.getDriver();
        setup.doBeforeSetup();
    }

    @After
    public void tearDown() throws Exception {
        //리포트 저장
        extentReports.flush();
        
        //driver.quit();
    }
    
    /*
     * Extent Report에 기록한 테스트 상태에 대한 사용자 정의 로그 메서드 (테스트 클래스에서 공통적으로 사용)
     */

    public void logInfo(String message) {
        //extentTest.log(Status.INFO, message);
        String className = this.getClass().getSimpleName();
        String taggedMessage = "<B>" + className + "</B><br>" + message;
        extentTest.log(Status.INFO, taggedMessage);
    }
    
    public void logPass(String message) {
        //extentTest.log(Status.PASS, message);
        String className = this.getClass().getSimpleName();
        String taggedMessage = "<B>" + className + "</B><br>" + message;
        extentTest.log(Status.PASS, taggedMessage);
    }
    
    public void logFail(Throwable throwable, String screenshotPath) {
        String stackTrace = getStackTrace(throwable);
        String formattedTrace = "<B>" + getClass().getSimpleName() + "</B><br>" + "<details>\n" +
                "    <summary>Click Here To See Exception Logs</summary>\n" +
                "    " + stackTrace + "\n" +
                "</details>\n";
                
        extentTest.log(Status.FAIL, formattedTrace, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
    }

    private String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
 
    protected void screenCapture(String methodName) {
        if (System.getProperty("screenshot_dir") != null && System.getProperty("className") != null) {
            new ScreenCapture().takeScreenShot(driver, System.getProperty("screenshot_dir")
                    + System.getProperty("className") + "__" + methodName + ".png");
        } else {
            new ScreenCapture().takeScreenShot(driver,
                    this.getClass().getName() + "__" + methodName  + ".png");
        }
    }

    protected boolean isElementPresentByXpath(String xpath) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        try {
            driver.findElement(By.xpath(xpath));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected boolean isElementPresent(By arg0) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        try {
            driver.findElement(arg0);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected void focuseTop(String windowHandle) {
        driver.switchTo().window(windowHandle);
    }

    protected void focuseTree(String windowHandle) {
        driver.switchTo().window(windowHandle);
        driver.switchTo().frame("mainIframe");
        driver.switchTo().frame("mainTree");
    }

    protected void focuseMainBody(String windowHandle) {
        driver.switchTo().window(windowHandle);
        driver.switchTo().frame("mainIframe");
        driver.switchTo().frame("mainBody");
    }

    protected void focuseMainFrame(String WindowHandle) {
        driver.switchTo().window(WindowHandle);
        driver.switchTo().frame("mainIframe");
    }

    protected void focuseframe(String frameId) {
        driver.switchTo().frame(frameId);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
    }

    protected void selectTopMenu(String id) {
        driver.findElement(By.id(id)).click();
    }

    protected void mouseMoveTopMenu(String id) {
        new Actions(driver).moveToElement(driver.findElement(By.id(id))).build().perform();
    }

    protected void selectTopSubMenu(String id) {
        driver.findElement(By.id(id)).click();
    }

    protected void selectTreeNode(String id) {
        selectTreeNode(id, driver.findElement(By.id(id)));
    }

    private void selectTreeNode(String id, WebElement webElem) {
        if (webElem == null) {
            webElem = driver.findElement(By.id(id));
        }
        if (webElem != null) {
            WebElement elem = webElem.findElement(By.id(id));
            if (elem != null) {
                if (elem.getTagName().equals("a")) {
                    elem.click();
                } else {
                    selectTreeNode(id, elem);
                }
            }
        }
    }

    protected void doubleClickTreebMenu(String id) {
        Actions actions = new Actions(driver);
		WebElement elementLocator = driver.findElement(By.id(id));
		actions.doubleClick(elementLocator).perform();
    }

    protected void moveToElement(String id) {
        new Actions(driver).moveToElement(driver.findElement(By.id(id))).build().perform();
    }

    protected boolean isAttribtuePresent(String attribute, WebElement webElem) {
        try {
            String value = webElem.getAttribute(attribute);
            if (value != null){
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    // clickRadioTypeButton - 관리자페이지의 라디오버튼 선택시 사용 (라디오버튼의 value 속성을 기준으로 선택)
    protected void clickRadioTypeButton(String objName, String setName) throws Exception {
        List<WebElement> radioButton = driver.findElements(By.name(objName));
        int size = radioButton.size();
        if (size == 0) {
            throw new Exception("라디오 타입 버튼( " + objName + " )이 존재하지 않음");
        }
        for(int i = 0; i < size; i++) {
            String val = radioButton.get(i).getAttribute("value");
            if (val.equalsIgnoreCase(setName)) {
                if (!isAttribtuePresent("checked", radioButton.get(i))) {
                    //radioButton.get(i).sendKeys(Keys.ENTER); //GN-26245
                    Actions action = new Actions(driver);
                    action.moveToElement(radioButton.get(i)).click().perform();
                    break;
                } else {
                    break;
                }
            } else {
                if (i == size) {
                    throw new Exception("라디오 타입 버튼( " + objName + " )의 설정이름( " + setName + " )이 존재하지 않음");
                }
            } 
        }
    }

    // clickRadioTypeButtonByText - 관리자페이지의 라디오버튼 선택시 사용 (라디오버튼의 텍스트를 기준으로 선택)
    protected void clickRadioTypeButtonByText(String objName, String setName) throws Exception {
        List<WebElement> radioButton = driver.findElements(By.name(objName));
        int size = radioButton.size();
        if (size == 0) {
            throw new Exception("라디오 타입 버튼( " + objName + " )이 존재하지 않음");
        }
        for(int i = 0; i < size; i++) {
            String val = driver.findElement(By.xpath("//*[@id='" + objName + "']/div[" + (i + 1) + "]/span")).getText();
            if (val.equals(setName)) {
                if (!isAttribtuePresent("checked", radioButton.get(i))) {
                    //radioButton.get(i).sendKeys(Keys.ENTER); //GN-26245
                    Actions action = new Actions(driver);
                    action.moveToElement(radioButton.get(i)).click().perform();
                    break;
                } else {
                    break;
                }
            } else {
                if (i == size) {
                    throw new Exception("라디오 타입 버튼( " + objName + " )의 설정이름( " + setName + " )이 존재하지 않음");
                }
            } 
        }
    }

    // 변경정책적용 적용시 사용
    protected void applyPolicy() throws Exception {
    	String Apply_ChangePolicy_Check = System.getProperty("변경정책적용_여부", "");
    	
        String windowHandle = driver.getWindowHandle();
        focuseTop(windowHandle);
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.id("form1:txtApply")));
        if(Apply_ChangePolicy_Check.equals("내용확인")) {
			driver.findElement(By.id("form1:txtApply"));
        }
        else {
			driver.findElement(By.id("form1:txtApply")).click();
			focuseMainBody(windowHandle);
			focuseframe("applyPolicy");
			driver.findElement(By.id("form1:applyBtn")).click();
			if (this.isElementPresent(By.xpath("form1:closeBtn")) == true) {
				throw new Exception("팝업창이 닫히지 않음");
			}
        }
        Thread.sleep(setup.testdelaytime);
    }

    /*  검색시 Enter 와 검색 버튼을 모두 검증하기 위해 사용
        고객사에서 Enter가 동작하지 않는 다는 TS 이슈가 많아 해당 문제를 검증하기 위해 추가 됨
    */
    
    protected void doEnter () {
        int random = new Random().nextInt(2);
		if (random == 0) {
			driver.findElement(By.id("form1:searchBar:searchDisp")).sendKeys(Keys.RETURN);
		} else {
			driver.findElement(By.id("form1:btnSearch")).click();
		}
	}

    protected String getNodeCountBypgTitle() {
        String pgTitle = driver.findElement(By.id("pageTitle")).getText();
        // pgTitle 문자열에서 노드수 정보 확인
        String nodeCount = pgTitle.substring(pgTitle.lastIndexOf("/")+1, pgTitle.lastIndexOf(")")).trim();
        return nodeCount; 
    }
}
