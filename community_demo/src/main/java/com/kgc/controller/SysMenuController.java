package com.kgc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kgc.annotation.LogAnnotation;
import com.kgc.entity.SysMenu;
import com.kgc.entity.SysRoleMenu;
import com.kgc.service.SysMenuService;
import com.kgc.service.SysRoleMenuService;
import com.kgc.util.Result;
import com.kgc.vo.MenuRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 菜单管理 前端控制器
 * </p>
 *
 * @author admin
 * @since 2023-08-04
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @GetMapping("/list")
    public Result list(){
        List<MenuRoleVo> menuRoleList = this.sysMenuService.getMenuRoleList();
        return Result.ok().put("data",menuRoleList);
    }


    @LogAnnotation("添加菜单")
    @PostMapping("/add")
    public Result add(@RequestBody SysMenu sysMenu){
        boolean save = this.sysMenuService.save(sysMenu);
        if(!save) return Result.error("添加菜单失败");
        return Result.ok();
    }


    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        SysMenu sysMenu = this.sysMenuService.getById(id);
        return Result.ok().put("data",sysMenu);
    }

    @LogAnnotation("编辑菜单")
    @PutMapping("/edit")
    public Result edit(@RequestBody SysMenu sysMenu){
        boolean update = this.sysMenuService.updateById(sysMenu);
        if(!update) return Result.error("更新失败");
        return Result.ok();
    }

    @LogAnnotation("删除菜单")
    @DeleteMapping("/del/{id}")
    public Result del(@PathVariable("id") Integer id){
        boolean remove = this.sysMenuService.removeById(id);
        //当前目录下的目录也需要被删除
        QueryWrapper<SysMenu> sysMenuQueryWrapper = new QueryWrapper<>();
        sysMenuQueryWrapper.eq("parent_id",id);
        this.sysMenuService.remove(sysMenuQueryWrapper);
        //sys_role_menu表也需要删
        QueryWrapper<SysRoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("menu_id",id);
        this.sysRoleMenuService.remove(queryWrapper);
        if(remove) return Result.ok();
        return Result.error("删除菜单失败");
    }


}

