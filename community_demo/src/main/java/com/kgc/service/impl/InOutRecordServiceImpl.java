package com.kgc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kgc.entity.Community;
import com.kgc.entity.InOutRecord;
import com.kgc.entity.Personinfo;
import com.kgc.form.InOutForm;
import com.kgc.mapper.CommunityMapper;
import com.kgc.mapper.InOutRecordMapper;
import com.kgc.mapper.PersoninfoMapper;
import com.kgc.service.InOutRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kgc.vo.ChartVo;
import com.kgc.vo.InOutRecordVo;
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
 * @since 2023-08-08
 */
@Service
public class InOutRecordServiceImpl extends ServiceImpl<InOutRecordMapper, InOutRecord> implements InOutRecordService {

    @Autowired(required = false)
    private InOutRecordMapper inOutRecordMapper;
    @Autowired(required = false)
    private PersoninfoMapper personinfoMapper;
    @Autowired(required = false)
    private CommunityMapper communityMapper;

    @Override
    public Map chart() {
        List<ChartVo> chart = this.inOutRecordMapper.chart();
        List<String> names = new ArrayList<>();
        List<Integer> nums = new ArrayList<>();
        //饼图的内容
        List<ChartVo> pieList = new ArrayList<>();

        for(ChartVo chartVo : chart){
            names.add(chartVo.getName());
            nums.add(chartVo.getValue());

            pieList.add(chartVo);
        }
        Map<String, List> map = new HashMap<>();
        map.put("names",names);
        map.put("nums",nums);
        map.put("list",pieList);
        return map;
    }

    @Override
    public PageVo inOutRecordList(InOutForm inOutForm) {
        Page<InOutRecord> page = new Page<>(inOutForm.getPage(),inOutForm.getLimit());
        //根据姓名进行查找 姓名在personinfo表中 先在personinfo表中查找person_id,再在in_out_record表中进行查找
        QueryWrapper<Personinfo> personinfoQueryWrapper = new QueryWrapper<>();
        personinfoQueryWrapper.like(StringUtils.isNotBlank(inOutForm.getUserName()),"user_name", inOutForm.getUserName());
        //可能重名或者局部匹配
        List<Personinfo> personinfos = this.personinfoMapper.selectList(personinfoQueryWrapper);
        List<Integer> ids = new ArrayList<>();
        for(Personinfo p : personinfos){
            ids.add(p.getPersonId());
        }
        QueryWrapper<InOutRecord> inOutRecordQueryWrapper = new QueryWrapper<>();
        inOutRecordQueryWrapper.in(ids.size()>0,"person_id",ids);
        inOutRecordQueryWrapper.between(StringUtils.isNotBlank(inOutForm.getStartDate()) && StringUtils.isNotBlank(inOutForm.getEndDate()),"in_time", inOutForm.getStartDate(),inOutForm.getEndDate());

        Page<InOutRecord> resultPage = this.inOutRecordMapper.selectPage(page, inOutRecordQueryWrapper);
        PageVo pageVo = new PageVo();
        pageVo.setTotalCount(resultPage.getTotal());
        pageVo.setCurrPage(resultPage.getCurrent());
        pageVo.setPageSize(resultPage.getSize());
        pageVo.setTotalPage(resultPage.getPages());

        List<InOutRecordVo> list = new ArrayList<>();

        for(InOutRecord inOutRecord:resultPage.getRecords()){
            InOutRecordVo inOutRecordVo = new InOutRecordVo();
            BeanUtils.copyProperties(inOutRecord,inOutRecordVo);
            Community community = this.communityMapper.selectById(inOutRecord.getCommunityId());
            inOutRecordVo.setCommunityName(community.getCommunityName());
            Personinfo personinfo = this.personinfoMapper.selectById(inOutRecord.getPersonId());
            inOutRecordVo.setTermName(personinfo.getTermName());
            inOutRecordVo.setHouseNo(personinfo.getHouseNo());
            inOutRecordVo.setUserName(personinfo.getUserName());
            list.add(inOutRecordVo);
        }
        pageVo.setList(list);

        return pageVo;
    }
}
