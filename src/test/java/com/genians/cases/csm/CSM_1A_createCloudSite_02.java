package com.genians.cases.csm;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.genians.action.etc.ZTNA_Login;
import com.genians.action.etc.명령어실행;


@RunWith(Suite.class)
@SuiteClasses({ 
    //ZTNA_Login.class,
    명령어실행.class,
})

public class CSM_1A_createCloudSite_02 {
    
    @BeforeClass
    public static void setUp() {
        System.setProperty("ZTNA_Login_timeZone1", "Asia");
        System.setProperty("ZTNA_Login_timeZone2", "Seoul (GMT +09:00)");
        System.setProperty("ZTNAC_Login_CountryCode", "대한민국 (KR +82)");
        
        // 레지스트리 값 변경을 통한 Proxy server 연결 해제
        System.setProperty("명령어실행_명령어", "REG ADD \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyEnable /d 0 /t REG_DWORD /f");
        System.setProperty("명령어실행_출력내용확인", "On");
        System.setProperty("명령어실행_출력", "작업을 완료했습니다.");
    }
}
