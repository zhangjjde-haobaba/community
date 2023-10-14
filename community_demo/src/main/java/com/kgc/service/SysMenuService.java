package com.kgc.service;

import com.kgc.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kgc.vo.MenuRoleVo;
import com.kgc.vo.MenuRouterVo;

import java.util.List;

/**
 * <p>
 * 菜单管理 服务类
 * </p>
 *
 * @author admin
 * @since 2023-08-04
 */
public interface SysMenuService extends IService<SysMenu> {

    public List<MenuRouterVo> getRouterByUserId(Integer userId);

    public List<MenuRoleVo> getMenuRoleList();

}
