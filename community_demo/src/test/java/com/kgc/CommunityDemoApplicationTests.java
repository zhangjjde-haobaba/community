package com.kgc;

import com.kgc.entity.SysMenu;
import com.kgc.mapper.SysMenuMapper;
import com.kgc.mapper.SysRoleMapper;
import com.kgc.service.SysMenuService;
import com.kgc.vo.MenuRouterVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CommunityDemoApplicationTests {

    @Autowired(required = false)
    private SysRoleMapper sysRoleMapper;
    @Autowired(required = false)
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysMenuService sysMenuService;

    @Test
    void contextLoads() {
//        System.out.println(this.sysRoleMapper.getRuleNameByUserId(1));
//        List<SysMenu> menusByUserId = sysMenuMapper.getMenusByUserId(1);
//        for(SysMenu sysMenu : menusByUserId){
//            System.out.println(sysMenu);
//        }
        List<MenuRouterVo> routerByUserId = this.sysMenuService.getRouterByUserId(1);
        for(MenuRouterVo menu : routerByUserId){
            System.out.println(menu);
        }


    }

}
