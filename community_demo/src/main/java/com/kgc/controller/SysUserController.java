package com.kgc.controller;


import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kgc.annotation.LogAnnotation;
import com.kgc.entity.SysUser;
import com.kgc.entity.SysUserRole;
import com.kgc.form.UpdatePasswordForm;
import com.kgc.form.UserAddOrUpdateForm;
import com.kgc.form.UserForm;
import com.kgc.mapper.SysRoleMapper;
import com.kgc.service.SysMenuService;
import com.kgc.service.SysUserRoleService;
import com.kgc.service.SysUserService;
import com.kgc.util.Result;
import com.kgc.vo.MenuRouterVo;
import com.kgc.vo.PageVo;
import com.kgc.vo.UserEditVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 系统用户 前端控制器
 * </p>
 *
 * @author admin
 * @since 2023-07-25
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired(required = false)
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 动态路由 根据使用者的权限 展示不同的菜单
     * @return
     */
    @GetMapping("/getRouters")
    public Result getRouters(HttpSession session){
        SysUser user = (SysUser)session.getAttribute("user");
        String role = sysRoleMapper.getRuleNameByUserId(user.getUserId());
        List<MenuRouterVo> routerByUserId = sysMenuService.getRouterByUserId(user.getUserId());

        return Result.ok().put("data",user).put("roles",role).put("routers",routerByUserId);
    }

    /**
     * 修改密码
     */
    @LogAnnotation("修改密码")
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody UpdatePasswordForm updatePasswordForm, HttpSession session){
        SysUser user = (SysUser)session.getAttribute("user");
        //验证原密码是否正确 数据库存储的是加密过后的密码 不能直接和明文密码比较
        String pwd = SecureUtil.sha256(updatePasswordForm.getPassword());
        if(!user.getPassword().equals(pwd)) return Result.ok().put("status","passwordError");
        //更新密码
        String newPwd = SecureUtil.sha256(updatePasswordForm.getNewPassword());
        user.setPassword(newPwd);
        boolean updateById = sysUserService.updateById(user);
        if(updateById) return Result.ok().put("status","success");
        return Result.error("更新密码失败");

    }

    @GetMapping("/list")
    public Result list(UserForm userForm){
        PageVo pageVo = this.sysUserService.userList(userForm);
        return Result.ok().put("data",pageVo);
    }

    @LogAnnotation("添加用户")
    @Transactional
    @PostMapping("/add")
    public Result add(@RequestBody UserAddOrUpdateForm userAddOrUpdateForm){
        //因为SysUser表中没有roleId 所以先将属性拷贝到SysUser对象中 等自动生成了UserId 在和前端传入的roleId一起插入到sys_user_role表中
        SysUser user = new SysUser();
        BeanUtils.copyProperties(userAddOrUpdateForm,user);
        boolean save = this.sysUserService.save(user);
        if(!save) return Result.error("用户添加失败");
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleId(userAddOrUpdateForm.getRoleId());
        sysUserRole.setUserId(user.getUserId());
        boolean save1 = this.sysUserRoleService.save(sysUserRole);
        if(!save1) return Result.error("用户角色表添加失败");
        return Result.ok();

    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        SysUser sysUser = this.sysUserService.getById(id);
        UserEditVo userEditVo = new UserEditVo();
        BeanUtils.copyProperties(sysUser,userEditVo);
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id);
        SysUserRole userRole = this.sysUserRoleService.getOne(queryWrapper);
        userEditVo.setRoleId(userRole.getRoleId());
        return Result.ok().put("data",userEditVo);
    }

    @LogAnnotation("编辑用户")
    @PutMapping("/edit")
    public Result edit(@RequestBody UserEditVo userEditVo){
        SysUser user = new SysUser();
        BeanUtils.copyProperties(userEditVo,user);
        boolean update = this.sysUserService.updateById(user);
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(user.getUserId());
        userRole.setRoleId(userEditVo.getRoleId());
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getUserId());
        //update方法需要传入一个对象 和条件才能使用
        //根据querywrapper作为条件查询到记录 然后将SysUserRole对象作为修改内容
        this.sysUserRoleService.update(userRole, queryWrapper);

        return Result.ok();
    }

    @LogAnnotation("删除用户")
    @DeleteMapping("/del")
    public Result del(@RequestBody Integer[] ids){
        boolean removeByIds = this.sysUserService.removeByIds(Arrays.asList(ids));
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(ids.length>0,"user_id",ids);
        boolean remove = this.sysUserRoleService.remove(queryWrapper);
        if(removeByIds && remove) return Result.ok();
        return Result.error("删除失败");
    }

}

