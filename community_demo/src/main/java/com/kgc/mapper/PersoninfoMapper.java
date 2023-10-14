package com.kgc.mapper;

import com.kgc.entity.Personinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2023-08-09
 */
public interface PersoninfoMapper extends BaseMapper<Personinfo> {

    @Select({
         "\n" +
                 "select count(*) from personinfo\n" +
                 "where community_id = #{communityId}"
    })
    public Integer getCountByCommunityId(Integer communityId);

}
