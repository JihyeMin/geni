package com.genians.cases.csm;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.genians.action.system.시스템관리_KA상태확인;
import com.genians.action.system.시스템관리_미승인센서승인;
import com.genians.action.system.시스템관리_빌드번호확인;


@RunWith(Suite.class)
@SuiteClasses({ 
    시스템관리_빌드번호확인.class,
    시스템관리_미승인센서승인.class,
    시스템관리_KA상태확인.class,
})

public class CSM_2A_verifyDefaultCheck_02 {
    
    @BeforeClass
    public static void setUp() {      
        System.setProperty("시스템관리_빌드번호확인_장비유형", "네트워크 센서");
        System.setProperty("시스템관리_빌드번호확인_빌드번호", System.getProperty("revision"));

        System.setProperty("시스템관리_KA상태확인_장비유형", "네트워크 센서");
        System.setProperty("시스템관리_KA상태확인_KA상태", "UP");
        
    }
}
