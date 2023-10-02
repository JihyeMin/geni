package com.genians.cases.csm;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.genians.action.node.노드존재여부확인;


@RunWith(Suite.class)
@SuiteClasses({ 
    노드존재여부확인.class,
})

public class CSM_2A_verifyDefaultCheck_03 {
    
    @BeforeClass
    public static void setUp() {      
        System.setProperty("노드존재여부확인_검색방법", "IP");
        System.setProperty("노드존재여부확인_존재", System.getProperty("host"));
    }
}
