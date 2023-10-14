package com.kgc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kgc.annotation.LogAnnotation;
import com.kgc.entity.SysRole;
import com.kgc.entity.SysRoleMenu;
import com.kgc.entity.SysUser;
import com.kgc.form.RoleAddOrUpdateForm;
import com.kgc.form.RoleForm;
import com.kgc.form.UserAddOrUpdateForm;
import com.kgc.service.SysRoleMenuService;
import com.kgc.service.SysRoleService;
import com.kgc.util.Result;
import com.kgc.vo.PageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2023-08-03
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @GetMapping("/getRoleList")
    public Result getRoleList(){
        List<SysRole> list = this.sysRoleService.list();
        return Result.ok().put("data",list);

    }

    @GetMapping("/list")
    public Result list(RoleForm roleForm){
        PageVo pageVo = this.sysRoleService.roleList(roleForm);
        return Result.ok().put("data",pageVo);
    }

    @LogAnnotation("添加角色")
    @PostMapping("/add")
    public Result add(@RequestBody RoleAddOrUpdateForm roleAddOrUpdateForm){
        //要存Sys_role表和Sys_role_menu表
        SysRole role = new SysRole();
        BeanUtils.copyProperties(roleAddOrUpdateForm,role);
        boolean save = this.sysRoleService.save(role);
        for(Integer i : roleAddOrUpdateForm.getMenuIdList()){
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(role.getRoleId());
            sysRoleMenu.setMenuId(i);
            boolean save1 = this.sysRoleMenuService.save(sysRoleMenu);
            if(!save1) return Result.error("添加角色失败");
        }
        if(save) return Result.ok();
        return Result.error("添加角色失败");
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        SysRole sysRole = this.sysRoleService.getById(id);
        RoleAddOrUpdateForm roleAddOrUpdateForm = new RoleAddOrUpdateForm();
        BeanUtils.copyProperties(sysRole,roleAddOrUpdateForm);
        List<Integer> list = new ArrayList<>();
        QueryWrapper<SysRoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleAddOrUpdateForm.getRoleId());
        List<SysRoleMenu> sysRoleMenus = this.sysRoleMenuService.list(queryWrapper);
        for(SysRoleMenu sysRoleMenu : sysRoleMenus){
            list.add(sysRoleMenu.getMenuId());
        }
        roleAddOrUpdateForm.setMenuIdList(list);
        return Result.ok().put("data",roleAddOrUpdateForm);
    }

    @LogAnnotation("编辑角色")
    @PutMapping("/edit")
    public Result edit(@RequestBody RoleAddOrUpdateForm roleAddOrUpdateForm){
        //先把sys_role_menu表中对应的数据删除再重新存入
        SysRole role = new SysRole();
        BeanUtils.copyProperties(roleAddOrUpdateForm,role);
        boolean update = this.sysRoleService.updateById(role);
        //先删除数据
        QueryWrapper<SysRoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleAddOrUpdateForm.getRoleId());
        boolean remove = this.sysRoleMenuService.remove(queryWrapper);
        //重新存入数据
        for(Integer i : roleAddOrUpdateForm.getMenuIdList()){
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(i);
            sysRoleMenu.setRoleId(roleAddOrUpdateForm.getRoleId());
            boolean save = this.sysRoleMenuService.save(sysRoleMenu);
            if(!save) return Result.error("编辑角色失败");

        }
        if(update) return Result.ok();
        return Result.error("编辑角色失败");

    }

    @LogAnnotation("删除角色")
    @DeleteMapping("/del")
    public Result del(@RequestBody Integer[] ids){
        boolean removeByIds = this.sysRoleService.removeByIds(Arrays.asList(ids));
        QueryWrapper<SysRoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id",ids);
        boolean remove = this.sysRoleMenuService.remove(queryWrapper);
        if(removeByIds && remove) return Result.ok();
        return Result.error("删除角色失败");
    }

}

