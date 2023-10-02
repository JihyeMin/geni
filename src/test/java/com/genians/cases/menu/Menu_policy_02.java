package com.genians.cases.menu;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.genians.action.menu.메뉴확인_정책;


@RunWith(Suite.class)
@SuiteClasses({ 
    메뉴확인_정책.class,
})

public class Menu_policy_02 {
    
    @BeforeClass
    public static void setUp() {
        System.setProperty("메뉴확인_정책_메인메뉴", "그룹");
      
    }
}
