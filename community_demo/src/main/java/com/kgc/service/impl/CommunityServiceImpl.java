package com.kgc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kgc.entity.Community;
import com.kgc.form.CommunityListForm;
import com.kgc.mapper.CommunityMapper;
import com.kgc.mapper.PersoninfoMapper;
import com.kgc.service.CommunityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kgc.vo.CommunityVo;
import com.kgc.vo.PageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2023-08-09
 */
@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community> implements CommunityService {

    @Autowired(required = false)
    private CommunityMapper communityMapper;
    @Autowired(required = false)
    private PersoninfoMapper personinfoMapper;

    @Override
    public PageVo communityList(CommunityListForm communityListForm) {
        Page<Community> page = new Page<>(communityListForm.getPage(),communityListForm.getLimit());

        QueryWrapper<Community> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(communityListForm.getCommunityName()),"community_name",communityListForm.getCommunityName());
        Page<Community> resultPage = this.communityMapper.selectPage(page, queryWrapper);
        PageVo pageVo = new PageVo();
        List<CommunityVo> list = new ArrayList<>();
        for(Community record : resultPage.getRecords()){
            CommunityVo communityVo = new CommunityVo();
            BeanUtils.copyProperties(record,communityVo);
            communityVo.setPersonCnt(personinfoMapper.getCountByCommunityId(record.getCommunityId()));
            list.add(communityVo);
        }


        pageVo.setList(list);
        pageVo.setTotalCount(resultPage.getTotal());
        pageVo.setCurrPage(resultPage.getCurrent());
        pageVo.setPageSize(resultPage.getSize());
        pageVo.setTotalPage(resultPage.getPages());

        return pageVo;
    }
}
