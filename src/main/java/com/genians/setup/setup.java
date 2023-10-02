package com.genians.setup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.io.InputStreamReader; // 테스트
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.jboss.aerogear.security.otp.Totp;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class setup {

	private static setup instance = null;
	
	/** settings.properties에서 설정 가능한 값 **/
	public static String testTarget = setup.getInstance().getSettings("testTarget");
	public static String testLanguage = setup.getInstance().getSettings("testLanguage");
	public static String testCloudDomain = setup.getInstance().getSettings("testCloudDomain");
	public static String testAdminLoginID = setup.getInstance().getSettings("testAdminLoginID");
	public static String testAdminLoginPASS = setup.getInstance().getSettings("testAdminLoginPASS");
	public static String testAdminOTP = setup.getInstance().getSettings("testAdminOTP");
	public static String testPublicIP = setup.getInstance().getSettings("testPublicIP");
	public static String host = setup.getInstance().getSettings("test.host");
	public static String host_mac = setup.getInstance().getSettings("test.mac");

	
	// 아래 삭제 예정 (고정환경에서 테스트)
	public static String center = setup.getInstance().getSettings("test.center"); //삭제
	public static String sensor = setup.getInstance().getSettings("test.sensor"); //삭제
	public static String baseUrl = "https://" + center + ":8443"; //삭제
	public static String ID = setup.getInstance().getSettings("test.adminid"); //삭제
	public static String PW = setup.getInstance().getSettings("test.adminpw"); //삭제
	/** settings.properties에서 설정 가능한 값 **/


	// csm
	public static int siteCreateWaitTime = 600;

	// 대기시간
	public static long implicitlyWait = 3; //테스트오류 예외처리에 대한 대기시간
	public static int testdelaytime = 800; //테스트 수행 중 타이밍 오류를 해결하기 위한 대기시간
	public static int testBeforeDelay = 200; //스크립트 수행전 Delay
	public static int frameMoveDeleay = 1000; // 프레임 이동 후 (GN-26546)
	public static int retryDelay = 1000; //테스트 재수행 시 기본 대기시간
	
	private static Properties settings;
	private static WebDriver driver;
	
	public static String reportDir;
	public static String screenshotDir;

	private setup() {
	}
	
	public static setup getInstance() {
        if (instance == null) {
            instance = new setup();
        }
        return instance;
    }

	public static void main() throws Exception {
		// mvn test 시 -D 옵션을 사용하여 파라미터(targetJiraNum, Jira 이슈번호) 전달
		String jiraNum = System.getProperty("targetJiraNum");
		if (jiraNum != null) {
			// 대문자를 소문자로 변경하고 하이픈 문자를 제거
			jiraNum = jiraNum.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""); //알파벳과 숫자를 제외한 모든 문자를 빈 문자열로 대체함 (Jira 번호의 하이픈 문자 제거가 필요)
			String testCloudURL = "https://"+ jiraNum + "." + testCloudDomain;  //Site로 생성한 테스트 대상 도메인
			System.setProperty("targetJiraNum", jiraNum);
			System.setProperty("testCloudURL", testCloudURL);
		}

		// mvn test 시 -D 옵션을 사용하여 파라미터(targetVersion, 대상 제품 이미지) 전달
		String targetVersion = System.getProperty("targetVersion");
		if (targetVersion != null) {
			System.setProperty("targetVersion", targetVersion);

			// targetVersion에서 revision 번호 추출, Report 저장시 revision 번호로 구분할때 사용
			String[] parts = targetVersion.split("-");
			String revision = null;
			if (parts.length >= 5) {
				revision = parts[3];
			}
			System.setProperty("revision", revision);
		}

		//settings.properties (Cloud 환경, 고정환경 아님)
		if (testTarget != null) {
			System.setProperty("testTarget", testTarget);
		}

		if (testLanguage != null) {
			System.setProperty("testLanguage", testLanguage);
		}

		if (testAdminLoginID != null) {
			System.setProperty("testAdminLoginID", testAdminLoginID);
		}

		if (testAdminLoginPASS != null) {
			System.setProperty("testAdminLoginPASS", testAdminLoginPASS);
		}

		if (testAdminOTP != null) {
			System.setProperty("testAdminOTP", testAdminOTP);
		}

		if (testCloudDomain != null) {
			System.setProperty("testCloudDomain", testCloudDomain);
		}

		// 아래 삭제 예정 (고정환경에서 테스트)
		if (host == null || host.isEmpty()) {
			host = HostIP();
		}
		System.setProperty("host", host);
		
		if (host_mac == null || host_mac.isEmpty()) {
			host_mac = HostMac();
		}
		System.setProperty("host_mac", host_mac);

		if (center != null) {
			System.setProperty("center", center);
		}

		if (sensor != null) {
			System.setProperty("sensor", sensor);
		}
		
		String network = center.substring(0,center.lastIndexOf(".")); //center IP를 기준으로 network id를 정함
		if (network != null) {
			System.setProperty("network", network);
		} 

		if (ID != null) {
			System.setProperty("관리자인증_ID", ID);
		}

		if (PW != null) {
			System.setProperty("관리자인증_PW", PW);
		}
		
		
		// 공인 IP(외부 IP)
		if (testPublicIP == null || testPublicIP.isEmpty()) {
			testPublicIP = PublicIP();
			System.setProperty("공인_IP", testPublicIP);
		}

		// 리포트파일 및 Screenshot파일 저장경로 설정
		reportDir = System.getProperty("user.dir") + "/report/" + System.getProperty("revision") + "/";
		System.setProperty("report_dir", reportDir);
		System.setProperty("screenshot_dir", reportDir + "screenshots/");

		
		// 관리콘솔 메뉴
		System.setProperty("menu_관리", "mtab2");
		System.setProperty("menu_관리_노드", "_menulink2_0");
		System.setProperty("menu_관리_IP주소", "_menulink2_1");
		System.setProperty("menu_관리_스위치", "_menulink2_2");
		System.setProperty("menu_관리_무선랜", "_menulink2_3");
		System.setProperty("menu_관리_사용자", "_menulink2_4");
		System.setProperty("menu_관리_신청서", "_menulink2_5");
		System.setProperty("menu_감사", "mtab3");
		System.setProperty("menu_감사_로그", "_menulink3_0");
		System.setProperty("menu_감사_검색필터", "_menulink3_1");
		System.setProperty("menu_감사_RADIUS", "_menulink3_2");
		System.setProperty("menu_감사_Flow", "_menulink3_3");
		System.setProperty("menu_감사_리포트", "_menulink3_4");

		System.setProperty("menu_정책", "mtab4");
		System.setProperty("menu_정책_정책", "_menulink4_0");
		System.setProperty("menu_정책_그룹", "_menulink4_1");
		System.setProperty("menu_정책_객체", "_menulink4_2");
		System.setProperty("menu_설정", "mtab5");
		System.setProperty("menu_설정_속성관리", "_menulink5_0");
		System.setProperty("menu_설정_접속인증페이지", "_menulink5_1");
		System.setProperty("menu_설정_서비스", "_menulink5_2");
		System.setProperty("menu_설정_사용자인증", "_menulink5_3"); //클릭X
		System.setProperty("menu_설정_환경설정", "_menulink5_4");
		System.setProperty("menu_시스템", "mtab6");
		System.setProperty("menu_시스템_시스템", "_menulink6_0");
		System.setProperty("menu_시스템_업데이트관리", "_menulink6_1");
		System.setProperty("menu_시스템_서비스관리", "_menulink6_2");

		System.setProperty("menu_관리_노드_센서별", "S-" + sensor);
		System.setProperty("menu_관리_전체노드", "node_BBA");
		System.setProperty("menu_관리_노드바구니", "nodeBucket");

		// [관리 > 사용자]의 하위메뉴
		System.setProperty("menu_관리_사용자_전체사용자", "전체사용자");
		System.setProperty("menu_관리_사용자_부서별", "부서별");
		System.setProperty("menu_관리_사용자_전체관리자", "전체 관리자");
		System.setProperty("menu_관리_사용자_부서관리", "부서 관리");
		System.setProperty("menu_관리_사용자_직급관리", "직급 관리");

		// [관리 > 신청]의 하위메뉴
		System.setProperty("menu_관리_신청서_사용자_신규등록", "node_BDAA");
		System.setProperty("menu_관리_신청서_사용자_결과조회", "node_BDAB");
		System.setProperty("menu_관리_신청서_IP_IP신규반납", "node_BDBA");
		System.setProperty("menu_관리_신청서_IP_장비변경", "node_BDBB");
		System.setProperty("menu_관리_신청서_IP_사용자변경", "node_BDBF");
		System.setProperty("menu_관리_신청서_IP_결과조회", "node_BDBE");
		System.setProperty("menu_관리_신청서_IP_단계별승인", "node_BDBC");
		System.setProperty("menu_관리_신청서_IP_단계별승인_IP신규", "node_BDBCA");
		System.setProperty("menu_관리_신청서_IP_단계별승인_IP반납", "node_BDBCB");
		System.setProperty("menu_관리_신청서_IP_단계별승인_장비변경", "node_BDBCC");
		System.setProperty("menu_관리_신청서_IP_단계별승인_사용자변경", "node_BDBCD");
		System.setProperty("menu_관리_신청서_장치사용_신규등록", "node_BDCA"); //장치 사용 신청서 > 신규 등록
		System.setProperty("menu_관리_신청서_장치사용_결과조회", "node_BDCB"); //장치 사용 신청서 > 결과 조회
		System.setProperty("menu_관리_신청서_에이전트인증코드_발급", "node_BDDA");
		System.setProperty("menu_관리_신청서_에이전트인증코드_결과조회", "node_BDDB");

		// [감사]의 하위메뉴
		System.setProperty("menu_감사_로그_로그검색", "node_GAA"); //로그 검색
		System.setProperty("menu_감사_로그_경고", "node_logFilterListOnlyStatic_3");
		System.setProperty("menu_감사_로그_에러", "node_logFilterListOnlyStatic_1");
		System.setProperty("menu_감사_로그_위험", "node_logFilterListOnlyStatic_2");
		System.setProperty("menu_감사_로그_검색필터", "node_GAB");
		System.setProperty("menu_감사_로그_IP이력검색", "IP이력검색");
		System.setProperty("menu_감사_리포트_노드리포트", "node_GBC");
		System.setProperty("menu_감사_리포트_로그리포트", "node_GBD");
		System.setProperty("menu_감사_리포트_무선랜리포트", "node_GBE");
		System.setProperty("menu_감사_리포트_사용자정의리포트", "node_GBA");
		System.setProperty("menu_감사_리포트_이메일발송", "node_GBB");

		// [정책]의 하위메뉴
		System.setProperty("menu_정책_정책_노드정책", "node_CGA"); //노드 정책
		System.setProperty("menu_정책_정책_노드액션", "node_CGAA");
		System.setProperty("menu_정책_정책_위험감지", "node_CGAB");
		System.setProperty("menu_정책_정책_Windows업데이트정책", "node_CGAAB");
		System.setProperty("menu_정책_정책_장치제어정책", "node_CGAAD");
		System.setProperty("menu_정책_정책_장치그룹", "node_CHD"); //장치그룹
		System.setProperty("menu_정책_정책_RADIUS정책", "node_CGC");
		System.setProperty("menu_정책_정책_제어정책", "node_CGB");
		System.setProperty("menu_정책_정책_제어액션", "node_CGBB");
		System.setProperty("menu_정책_정책_무선랜정책", "node_CGD");
		System.setProperty("menu_정책_정책_AP프로파일", "node_CGDA");
		System.setProperty("menu_정책_정책_Client프로파일", "node_CGDB");
		System.setProperty("menu_정책_정책_클라우드보안그룹정책", "node_CGF");
		System.setProperty("menu_정책_정책_규제정책", "node_CGE");
		
		System.setProperty("menu_정책_그룹_노드", "node_CHA");
		System.setProperty("menu_정책_그룹_무선랜", "node_CHB");
		System.setProperty("menu_정책_그룹_사용자", "node_CHC");

		System.setProperty("menu_정책_객체_권한", "권한");
		System.setProperty("menu_정책_객체_네트워크", "node_CIDB");
		System.setProperty("menu_정책_객체_서비스", "node_CIDC");
		System.setProperty("menu_정책_객체_시간", "node_CIDD");
		System.setProperty("menu_정책_객체_어플리케이션객체", "어플리케이션 객체");
		System.setProperty("menu_정책_객체_어플리케이션", "node_CIDEA");

		

		System.setProperty("menu_설정_관리자등록", "관리자등록");
		System.setProperty("menu_설정_관리역할", "관리 역할");
		System.setProperty("menu_설정_세션관리", "세션관리");

		// [설정 > 속성 관리]의 하위메뉴
		System.setProperty("menu_설정_노드타입관리", "노드타입 관리");
		System.setProperty("menu_설정_태그관리", "태그 관리");
		System.setProperty("menu_설정_노드추가필드", "노드");
		System.setProperty("menu_설정_장비추가필드", "장비"); //장비
		System.setProperty("menu_설정_사용자추가필드", "사용자"); //사용자
		System.setProperty("menu_설정_IP신청서추가필드", "IP 신청서");
		System.setProperty("menu_설정_장비수명주기관리추가필드", "장비 수명주기 관리");
		System.setProperty("menu_설정_IP추가필드", "IP");
		System.setProperty("menu_설정_MAC추가필드", "MAC");
		
		System.setProperty("menu_설정_용도관리", "node_DBI"); //용도 관리
		System.setProperty("menu_설정_사용자용도", "사용자 용도");
		System.setProperty("menu_설정_IP용도_IP신규", "IP 신규");
		System.setProperty("menu_설정_IP용도_IP반납", "IP 반납");
		System.setProperty("menu_설정_IP용도_장비변경", "장비 변경");
		System.setProperty("menu_설정_IP용도_사용자변경", "사용자 변경");

		// [설정 > CWP]의 하위메뉴
		System.setProperty("menu_설정_디자인템플릿", "디자인 템플릿");
		System.setProperty("menu_설정_공지사항", "공지사항");
		System.setProperty("menu_설정_메시지관리", "메시지 관리");
		System.setProperty("menu_설정_사용자정보", "사용자정보");
		System.setProperty("menu_설정_보안서약", "보안서약"); 
		System.setProperty("menu_설정_사용자정의버튼", "사용자 정의 버튼");
		System.setProperty("menu_설정_CWP설정", "CWP 설정");
		
		// [설정 > 서비스]의 하위메뉴
		System.setProperty("menu_설정_RADIUS서버", "node_DFA"); //RADIUS 서버

		// [설정 > 사용자 인증]의 하위메뉴
		System.setProperty("menu_설정_사용자관리", "사용자 관리");
		System.setProperty("menu_설정_사용자인증_사용자인증", "사용자 인증");
		System.setProperty("menu_설정_인증연동", "인증 연동");
		System.setProperty("menu_설정_사용자정보동기화", "정보 동기화");
		System.setProperty("menu_설정_관리역할", "관리 역할");

		// [설정 > 환경설정]의 하위메뉴
		System.setProperty("menu_설정_노드관리", "노드 관리");
		System.setProperty("menu_설정_포트스캔", "포트 스캔");
		System.setProperty("menu_설정_IP관리", "IP 관리");
		System.setProperty("menu_설정_IP신청시스템공지사항", "IP 신청 시스템 공지사항");
		System.setProperty("menu_설정_무선랜관리", "무선랜 관리");
		System.setProperty("menu_설정_감사기록", "감사기록");
		System.setProperty("menu_설정_관리콘솔", "관리 콘솔");
		System.setProperty("menu_설정_리포트", "리포트");
		System.setProperty("menu_설정_에이전트", "에이전트");
		System.setProperty("menu_설정_운영체제업데이트", "운영체제 업데이트");
		System.setProperty("menu_설정_기타설정", "기타 설정");


		System.setProperty("menu_설정_패치관리", "패치관리");
		System.setProperty("menu_설정_SSL인증서", "SSL 인증서");
		System.setProperty("menu_설정_백업", "node_DEJ"); //백업
		System.setProperty("menu_설정_DHCP서버", "DHCP서버");
		
		System.setProperty("menu_설정_실험실", "실험실");

		System.setProperty("menu_시스템관리_사이트", "node_EAG"); // ZTNA 신규 추가 메뉴
		System.setProperty("menu_시스템관리_시스템관리", "node_EAA");
		System.setProperty("menu_시스템관리_센서관리", "node_EAC");
		System.setProperty("menu_시스템관리_CloudProvider관리", "node_EAH"); // ZTNA 신규 추가 메뉴
		System.setProperty("menu_시스템관리_환경설정", "node_EAEA");
		System.setProperty("menu_시스템관리_센서설정", "node_EAEB");
		System.setProperty("menu_시스템관리_무선센서환경설정", "node_EAEC");
		System.setProperty("menu_시스템관리_세션관리", "node_EAD");
		System.setProperty("menu_시스템관리_라이선스", "node_EAB");
		System.setProperty("menu_시스템관리_소프트웨어", "node_EBG");
		System.setProperty("menu_시스템관리_에이전트플러그인", "node_EBGA");
		System.setProperty("menu_시스템관리_정책서버플러그인", "node_EBGB");
		System.setProperty("menu_시스템관리_운영정보데이터", "node_EBE");
		System.setProperty("menu_시스템관리_서비스제어", "node_EDA");
		System.setProperty("menu_시스템관리_접속포트", "node_EDB");
	}

	/*
	 * 공인 IP(외부 IP) 확인
	 */
	public static String PublicIP() throws IOException {
		String ipAddress = null;
		try {
			// 외부 서비스를 사용하여 공인 IP 주소 가져오기
			URL url = new URL("https://api64.ipify.org?format=text"); // ipify.org 서비스 사용
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				// 응답을 읽어들임
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				ipAddress = reader.readLine();
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return ipAddress;
	}


	/*
	 * before: 자동화 테스트 환경 메인 host의 IP주소는 network + ".29" 고정
	 * after: 소켓 연결로 현재 실행중인 시스템의 IP 주소를 반환
	 */
	public static String HostIP() throws IOException {
		String host = null;
		Socket s = null;
		try {
			s = new Socket("google.com", 80);
			host = s.getLocalAddress().getHostAddress();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (s != null) {
				s.close();
			}
		}
		return host;
	}

	/*
	 * before: 자동화 테스트 환경 메인 host의 MAC주소는 "00:00:00:00:00:" + host.substring(host.length()-2) 고정
	 * after: 소켓 연결로 현재 실행중인 시스템의 MAC 주소를 반환
	 */
	public static String HostMac() throws IOException {
		String host_mac = null;
		Socket s = null;
		try {
			s = new Socket("google.com", 80);
			InetAddress address = InetAddress.getLocalHost();
			NetworkInterface nwi = NetworkInterface.getByInetAddress(address);
			byte[] macAddressBytes = nwi.getHardwareAddress();
			
			// MAC주소를 String으로 변환
			StringBuilder macAddressBuilder = new StringBuilder();
			for (int i = 0; i < macAddressBytes.length; i++) {
				macAddressBuilder.append(String.format("%02X%s", macAddressBytes[i], (i < macAddressBytes.length - 1) ? ":" : ""));
			}
			host_mac = macAddressBuilder.toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} finally {
			if (s != null) {
				s.close();
			}
		}
		return host_mac;
	}
	
	public String getSettings(String key) {
		if (settings == null) {
			loadProperties();
		}
		return settings.getProperty(key);
	}
	
	public String getPropertys(String key, String dir) {
		getProperties(dir);
		return settings.getProperty(key);
	}
	
	public static WebDriver getDriver() {
		if (driver == null || driver.toString().contains("(null)")) {
			//WebDriverManager.chromedriver().setup();
			System.setProperty("webdriver.chrome.driver", "D://chromedriver/chromedriver.exe");
			Map<String, Object> prefs = new HashMap<String, Object>();
			prefs.put("download.default_directory", "D:\\etc"); //chrome 에서 다운로드 시 파일저장 위치 지정
			ChromeOptions options = new ChromeOptions();
			WebDriverManager.chromedriver().setup();
			options.setExperimentalOption("prefs", prefs);
			options.addArguments("--start-maximized", "-ignore-certificate-errors");
			options.addArguments("--remote-allow-origins=*");
			options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
			driver = new ChromeDriver(options);
		}
		driver.manage().timeouts().implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
		return driver;
	}
	
	public void loadProperties() {
		if (settings == null) {
			settings = new Properties();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			
			try (InputStream resourceStream = loader.getResourceAsStream("settings.properties")) {
				settings.load(resourceStream);
			} catch (IOException ex) {
				System.err.println(ex.getMessage());
			}
		}
	}
	
	public void getProperties(String dir) {
		if (settings == null) {
			settings = new Properties();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			
			try (InputStream resourceStream = loader.getResourceAsStream(dir)) {
				settings.load(resourceStream);
			} catch (IOException ex) {
				System.err.println(ex.getMessage());
			}
		}
	}

	public static int retryCount() {

		return 0;
	}

	public static void doBeforeSetup() throws Exception {
		Thread.sleep(testBeforeDelay);
		driver.manage().timeouts().implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
	}

	public static void doRetrySetup() throws Exception {
		Thread.sleep(retryDelay);
		driver.manage().timeouts().implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
	}

	public static String getTwoFactorCode(String otpSecret) {
		Totp totp = new Totp(otpSecret);
		String twoFactorCode = totp.now();
		return twoFactorCode;
	}

}