package com.kgc.service;

import com.kgc.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kgc.form.RoleForm;
import com.kgc.vo.PageVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2023-08-03
 */
public interface SysRoleService extends IService<SysRole> {

    public PageVo roleList(RoleForm roleForm);

}
