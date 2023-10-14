package com.kgc.mapper;

import com.kgc.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 菜单管理 Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2023-08-04
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    @Select({
          "select m.id, m.parent_id, m.`name`,\n" +
                  "m.path, m.component, m.menu_type, m.`status` , m.icon, m.sort, m.hidden from sys_menu m, sys_role_menu rm, sys_user_role ur\n" +
                  "where ur.role_id = rm.role_id and rm.menu_id = m.id\n" +
                  "and ur.user_id = #{userId} order by m.sort"
    })
    public List<SysMenu> getMenusByUserId(Integer userId);

}
