package com.kgc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kgc.entity.SysRole;
import com.kgc.entity.SysUser;
import com.kgc.entity.SysUserRole;
import com.kgc.form.UserForm;
import com.kgc.mapper.SysRoleMapper;
import com.kgc.mapper.SysUserMapper;
import com.kgc.mapper.SysUserRoleMapper;
import com.kgc.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kgc.vo.PageVo;
import com.kgc.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author admin
 * @since 2023-07-25
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired(required = false)
    private SysUserMapper sysUserMapper;
    @Autowired(required = false)
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired(required = false)
    private SysRoleMapper sysRoleMapper;

    @Override
    public PageVo userList(UserForm userForm) {
        Page<SysUser> page = new Page<>(userForm.getPage(),userForm.getLimit());
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(userForm.getUsername()),"username",userForm.getUsername())
                .like(StringUtils.isNotBlank(userForm.getRealName()),"real_name",userForm.getRealName());
        Page<SysUser> resultPage = this.sysUserMapper.selectPage(page, queryWrapper);

        PageVo pageVo = new PageVo();
        pageVo.setTotalCount(resultPage.getTotal());
        pageVo.setCurrPage(resultPage.getCurrent());
        pageVo.setPageSize(resultPage.getSize());
        pageVo.setTotalPage(resultPage.getPages());

        List<UserVo> list = new ArrayList<>();

        for(SysUser user : resultPage.getRecords()){
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user,userVo);
            QueryWrapper<SysUserRole> sysUserRoleQueryWrapper = new QueryWrapper<>();
            sysUserRoleQueryWrapper.eq("user_id", user.getUserId());
            SysUserRole sysUserRole = this.sysUserRoleMapper.selectOne(sysUserRoleQueryWrapper);
            QueryWrapper<SysRole> sysRoleQueryWrapper = new QueryWrapper<>();
            sysRoleQueryWrapper.eq("role_id",sysUserRole.getRoleId());
            SysRole sysRole = this.sysRoleMapper.selectOne(sysRoleQueryWrapper);
            userVo.setRoleName(sysRole.getRoleName());
            list.add(userVo);
        }
        pageVo.setList(list);
        return pageVo;
    }
}
