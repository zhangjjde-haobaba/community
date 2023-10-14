package com.kgc.mapper;

import com.kgc.entity.InOutRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kgc.vo.ChartVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2023-08-08
 */
public interface InOutRecordMapper extends BaseMapper<InOutRecord> {

    @Select({
         "select c.community_name `name`,count(*) `value` from community c, personinfo p where c.community_id = p.community_id\n" +
                 "group by c.community_id"
    })
    public List<ChartVo> chart();

    @Select({
            "select * from in_out_record where " +
                    "community_id=#{communityId} and " +
                    "person_id=#{personId} and out_time is null"
    })
    public InOutRecord getInoutrecord(InOutRecord inOutRecord);
    //没有出场时间 表示就是进场

}
