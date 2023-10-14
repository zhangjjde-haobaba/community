package com.kgc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kgc.entity.Community;
import com.kgc.entity.ManualRecord;
import com.kgc.form.ManualRecordForm;
import com.kgc.mapper.CommunityMapper;
import com.kgc.mapper.ManualRecordMapper;
import com.kgc.service.CommunityService;
import com.kgc.service.ManualRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kgc.vo.ManualRecordVo;
import com.kgc.vo.PageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2023-08-13
 */
@Service
public class ManualRecordServiceImpl extends ServiceImpl<ManualRecordMapper, ManualRecord> implements ManualRecordService {
    @Autowired(required = false)
    private CommunityMapper communityMapper;
    @Autowired(required = false)
    private ManualRecordMapper manualRecordMapper;

    @Override
    public Map ManualRecordList(ManualRecordForm manualRecordForm) {
        Map<String,Object> map = new HashMap<>();
        map.put("communityList",this.communityMapper.selectList(null));
        Page<ManualRecord> page = new Page<>(manualRecordForm.getPage(),manualRecordForm.getLimit());
        QueryWrapper<ManualRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(manualRecordForm.getCommunityId() != null, "community_id",manualRecordForm.getCommunityId())
                .eq(StringUtils.isNotBlank(manualRecordForm.getMobile()),"mobile",manualRecordForm.getMobile())
                .like(StringUtils.isNotBlank(manualRecordForm.getVisitor()),"visitor",manualRecordForm.getVisitor());

        Page<ManualRecord> resultPage = this.manualRecordMapper.selectPage(page, queryWrapper);

        PageVo pageVo = new PageVo();
        pageVo.setTotalCount(resultPage.getTotal());
        pageVo.setCurrPage(resultPage.getCurrent());
        pageVo.setPageSize(resultPage.getSize());
        pageVo.setTotalPage(resultPage.getPages());

        List<ManualRecordVo> list = new ArrayList<>();
        for(ManualRecord manualRecord : resultPage.getRecords()){
            ManualRecordVo manualRecordVo = new ManualRecordVo();
            BeanUtils.copyProperties(manualRecord,manualRecordVo);
            Community community = this.communityMapper.selectById(manualRecord.getCommunityId());
            manualRecordVo.setCommunityName(community.getCommunityName());
            list.add(manualRecordVo);
        }
        pageVo.setList(list);
        map.put("pageList",pageVo);

        return map;
    }
}
