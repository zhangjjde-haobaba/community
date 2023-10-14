package com.kgc.service;

import com.kgc.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kgc.form.UserForm;
import com.kgc.vo.PageVo;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author admin
 * @since 2023-07-25
 */
public interface SysUserService extends IService<SysUser> {

    public PageVo userList(UserForm userForm);

}
