package com.genians.cases.menu;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.genians.action.menu.메뉴확인_관리;


@RunWith(Suite.class)
@SuiteClasses({ 
    메뉴확인_관리.class,
})

public class Menu_management_05 {
    
    @BeforeClass
    public static void setUp() {
        System.setProperty("메뉴확인_관리_메인메뉴", "사용자");
      
    }
}
