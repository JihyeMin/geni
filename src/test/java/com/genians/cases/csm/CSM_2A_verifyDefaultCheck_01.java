package com.genians.cases.csm;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.genians.action.etc.ZTNA_Login;
import com.genians.action.system.시스템관리_빌드번호확인;


@RunWith(Suite.class)
@SuiteClasses({ 
    ZTNA_Login.class,
    시스템관리_빌드번호확인.class
})

public class CSM_2A_verifyDefaultCheck_01 {
    
    @BeforeClass
    public static void setUp() {      
        System.setProperty("ZTNA_Login_timeZone1", "Asia");
        System.setProperty("ZTNA_Login_timeZone2", "Seoul (GMT +09:00)");
        System.setProperty("ZTNAC_Login_CountryCode", "대한민국 (KR +82)");

        System.setProperty("시스템관리_빌드번호확인_장비유형", "정책 서버");
        System.setProperty("시스템관리_빌드번호확인_빌드번호", System.getProperty("revision"));
    }
}
