package com.kgc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kgc.entity.Community;
import com.kgc.entity.Personinfo;
import com.kgc.form.PersonListForm;
import com.kgc.mapper.CommunityMapper;
import com.kgc.mapper.PersoninfoMapper;
import com.kgc.service.PersoninfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kgc.vo.PageVo;
import com.kgc.vo.PersonVo;
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
public class PersoninfoServiceImpl extends ServiceImpl<PersoninfoMapper, Personinfo> implements PersoninfoService {

    @Autowired(required = false)
    private PersoninfoMapper personinfoMapper;
    @Autowired(required = false)
    private CommunityMapper communityMapper;

    @Override
    public PageVo personList(PersonListForm personListForm) {
        Page<Personinfo> page = new Page<>(personListForm.getPage(),personListForm.getLimit());
        QueryWrapper<Personinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(personListForm.getUserName()),"user_name",personListForm.getUserName())
                .eq(personListForm.getCommunityId() != null, "community_id",personListForm.getCommunityId())
                .eq(StringUtils.isNotBlank(personListForm.getMobile()),"mobile",personListForm.getMobile());
        Page<Personinfo> resultPage = this.personinfoMapper.selectPage(page,queryWrapper);
        PageVo pageVo = new PageVo();
        pageVo.setTotalPage(resultPage.getPages());
        pageVo.setPageSize(resultPage.getSize());
        pageVo.setTotalCount(resultPage.getTotal());
        pageVo.setCurrPage(resultPage.getCurrent());

        List<PersonVo> list = new ArrayList<>();

        for(Personinfo personinfo : resultPage.getRecords()){

            PersonVo personVo = new PersonVo();
            BeanUtils.copyProperties(personinfo,personVo);
            Community community = this.communityMapper.selectById(personinfo.getCommunityId());
            personVo.setCommunityName(community.getCommunityName());
            list.add(personVo);

        }
        pageVo.setList(list);

        return pageVo;
    }
}
