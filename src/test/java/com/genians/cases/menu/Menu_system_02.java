package com.genians.cases.menu;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.genians.action.menu.메뉴확인_시스템;


@RunWith(Suite.class)
@SuiteClasses({ 
    메뉴확인_시스템.class,
})

public class Menu_system_02 {
    
    @BeforeClass
    public static void setUp() {
        System.setProperty("메뉴확인_시스템_메인메뉴", "업데이트 관리");
      
    }
}
