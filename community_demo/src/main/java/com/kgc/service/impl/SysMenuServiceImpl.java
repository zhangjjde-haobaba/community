package com.kgc.service.impl;

import com.kgc.entity.SysMenu;
import com.kgc.mapper.SysMenuMapper;
import com.kgc.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kgc.vo.ChildMenuRouterVo;
import com.kgc.vo.MenuRoleVo;
import com.kgc.vo.MenuRouterVo;
import com.kgc.vo.MetaVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author admin
 * @since 2023-08-04
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired(required = false)
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<MenuRouterVo> getRouterByUserId(Integer userId) {
        List<SysMenu> menuList = this.sysMenuMapper.getMenusByUserId(userId);
        List<MenuRouterVo> list = new ArrayList<>();
        for(SysMenu menu : menuList){
            // 父节点为0
            if(menu.getParentId() == 0){
                MenuRouterVo menuRouterVo = new MenuRouterVo();
                BeanUtils.copyProperties(menu,menuRouterVo);
                MetaVo metaVo = new MetaVo();
                metaVo.setTitle(menu.getName());
                metaVo.setIcon(menu.getIcon());
                menuRouterVo.setMeta(metaVo);
                //生成children parent_id == menu_Id的是子菜单
                Integer menuId = menu.getId();
                List<ChildMenuRouterVo> children = new ArrayList<>();
                for(SysMenu child : menuList){
                    if(child.getParentId() == menuId ){
                        ChildMenuRouterVo childVo = new ChildMenuRouterVo();
                        BeanUtils.copyProperties(child,childVo);
                        MetaVo childMetaVo = new MetaVo();
                        childMetaVo.setTitle(child.getName());
                        childMetaVo.setIcon(child.getIcon());
                        childVo.setMeta(childMetaVo);
                        children.add(childVo);
                    }
                }
                menuRouterVo.setChildren(children);
                list.add(menuRouterVo);
            }


        }


        return list;
    }

    @Override
    public List<MenuRoleVo> getMenuRoleList() {

        List<SysMenu> menuList = this.sysMenuMapper.selectList(null);
        List<MenuRoleVo> list = new ArrayList<>();
        for(SysMenu menu : menuList){
            if(menu.getParentId() == 0){
                MenuRoleVo menuRoleVo = new MenuRoleVo();
                BeanUtils.copyProperties(menu, menuRoleVo);
                //获取子菜单
                List<MenuRoleVo> child = new ArrayList<>();
                for(SysMenu item : menuList){
                    if(item.getParentId() == menuRoleVo.getId()){
                        MenuRoleVo children = new MenuRoleVo();
                        BeanUtils.copyProperties(item, children);
                        child.add(children);
                    }
                }
                menuRoleVo.setChildren(child);
                list.add(menuRoleVo);
            }
        }

        return list;
    }
}
