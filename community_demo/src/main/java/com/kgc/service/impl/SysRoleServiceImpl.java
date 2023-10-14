package com.kgc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kgc.entity.SysRole;
import com.kgc.form.RoleForm;
import com.kgc.mapper.SysRoleMapper;
import com.kgc.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kgc.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2023-08-03
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired(required = false)
    private SysRoleMapper sysRoleMapper;

    @Override
    public PageVo roleList(RoleForm roleForm) {
        Page<SysRole> page = new Page<>(roleForm.getPage(),roleForm.getLimit());
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(roleForm.getRoleName()),"role_name",roleForm.getRoleName());
        Page<SysRole> resultPage = this.sysRoleMapper.selectPage(page, queryWrapper);
        PageVo pageVo = new PageVo();
        pageVo.setTotalCount(resultPage.getTotal());
        pageVo.setCurrPage(resultPage.getCurrent());
        pageVo.setPageSize(resultPage.getSize());
        pageVo.setTotalPage(resultPage.getPages());
        pageVo.setList(resultPage.getRecords());

        return pageVo;
    }
}
