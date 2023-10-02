package com.genians.cases.csm;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.genians.action.etc.명령어실행;
import com.genians.action.csm.CSM_Login;
import com.genians.action.csm.CSM_createSite;
import com.genians.action.csm.CSM_Logout;
import com.genians.setup.setup;

@RunWith(Suite.class)
@SuiteClasses({ 
    명령어실행.class,
    CSM_Login.class,
    CSM_createSite.class,
    CSM_Logout.class,
    
})
public class CSM_1A_createCloudSite_01 {
    
    @BeforeClass
    public static void setUp() {
        //레지스트리 값 변경을 통한 Proxy server 연결
        System.setProperty("명령어실행_명령어", "REG ADD \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyServer /d 3.37.83.64:3128 /t REG_SZ /f && reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyEnable /d 1 /t REG_DWORD /f");
        System.setProperty("명령어실행_출력내용확인", "On");
        System.setProperty("명령어실행_출력", "작업을 완료했습니다.");
        
        System.setProperty("CSM_Login_baseUrl", "https://" + System.getProperty("testTarget"));
        System.setProperty("CSM_Login_ID", setup.testAdminLoginID);
        System.setProperty("CSM_Login_PW", setup.testAdminLoginPASS);
        System.setProperty("CSM_Login_role", "admin");
        
        System.setProperty("CSM_createSite_siteName", System.getProperty("targetJiraNum"));
        System.setProperty("CSM_createSite_branch", "BETA");
        System.setProperty("CSM_createSite_targetVer", System.getProperty("targetVersion"));
        
        System.setProperty("CSM_Logout_baseUrl", "https://" + System.getProperty("testTarget"));
        System.setProperty("CSM_Logout_role", "admin");
    }
}
