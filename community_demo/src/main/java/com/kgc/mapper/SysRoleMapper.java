package com.kgc.mapper;

import com.kgc.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2023-08-03
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    @Select({
        "select r.role_name from sys_role r, sys_user_role ur where r.role_id = ur.role_id and ur.user_id = #{userId}"
    })
    public String getRuleNameByUserId(Integer userId);

}
