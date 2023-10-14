package com.kgc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kgc.annotation.LogAnnotation;
import com.kgc.entity.*;
import com.kgc.form.CommunityAddForm;
import com.kgc.form.CommunityListForm;
import com.kgc.service.*;
import com.kgc.util.Result;
import com.kgc.vo.PageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2023-08-09
 */
@RestController
@RequestMapping("/sys/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;
    
    @Autowired
    private CamerainfoService camerainfoService;

    @Autowired
    private InOutRecordService inOutRecordService;

    @Autowired
    private ManualRecordService manualRecordService;

    @Autowired
    private PersoninfoService personinfoService;

    @GetMapping("/list")
    public Result list( CommunityListForm communityListForm){
        //get 请求不能加requestbody请求 只有put或post可以
        PageVo pageVo = this.communityService.communityList(communityListForm);
        return Result.ok().put("data",pageVo);
    }

    @LogAnnotation("添加小区")
    @PostMapping("/add")
    public Result add(@RequestBody CommunityAddForm communityAddForm, HttpSession session){
        Community community = new Community();
        BeanUtils.copyProperties(communityAddForm,community);
        SysUser user = (SysUser) session.getAttribute("user");
        community.setCreater(user.getUsername());
        boolean save = this.communityService.save(community);
        if(!save) return Result.error("楼栋添加失败");
        return Result.ok();
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer communityId){
        Community community = this.communityService.getById(communityId);
        if(community==null) return Result.error("小区不存在");
        return Result.ok().put("data",community);
    }

    @LogAnnotation("编辑小区")
    @PutMapping("/edit")
    public Result edit(@RequestBody CommunityAddForm communityAddForm){
        Community community = new Community();
        BeanUtils.copyProperties(communityAddForm,community);
        boolean update = this.communityService.updateById(community);
        if(!update) return Result.error("编辑小区失败");
        return Result.ok();
    }

    @LogAnnotation("删除小区")
    @DeleteMapping("/del")
    @Transactional
    public Result del(@RequestBody Integer[] ids){
        //传递数组也需要requestbody
        //community_id在多个表中都有
        QueryWrapper<Camerainfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("community_id", ids);
        boolean remove1 = this.camerainfoService.remove(queryWrapper);
        if(!remove1) return Result.error("小区摄像头删除失败");
        QueryWrapper<InOutRecord> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.in("community_id", ids);
        boolean remove2 = this.inOutRecordService.remove(queryWrapper1);
        if(!remove2) return Result.error("小区出入记录删除失败");
        QueryWrapper<ManualRecord> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.in("community_id", ids);
        boolean remove3 = this.manualRecordService.remove(queryWrapper2);
        if(!remove3) return Result.error("小区访客记录删除失败");
        QueryWrapper<Personinfo> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.in("community_id", ids);
        boolean remove4 = this.personinfoService.remove(queryWrapper3);
        if(!remove4) return Result.error("小区居民删除失败");

        boolean remove5 = this.communityService.removeByIds(Arrays.asList(ids));
        if(!remove5) return Result.error("小区删除失败");
        return Result.ok();
    }

    @GetMapping("/getCommunityMap")
    public Result getCommunityMap(){
        List<Community> list = this.communityService.list();
        if(list == null) return Result.error("没有找到小区数据");
        return Result.ok().put("data",list);

    }

}

