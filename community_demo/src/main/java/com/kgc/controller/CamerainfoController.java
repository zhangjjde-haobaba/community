package com.kgc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kgc.annotation.LogAnnotation;
import com.kgc.entity.Camerainfo;
import com.kgc.entity.Community;
import com.kgc.entity.SysUser;
import com.kgc.service.CamerainfoService;
import com.kgc.service.CommunityService;
import com.kgc.util.Result;
import com.kgc.vo.CameraVo;
import com.tencentcloudapi.cdn.v20180606.models.Https;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2023-08-13
 */
@RestController
@RequestMapping("/sys/camera")
public class CamerainfoController {

    @Autowired
    private CamerainfoService camerainfoService;
    @Autowired
    private CommunityService communityService;

    @GetMapping("/list/{id}")
    public Result list(@PathVariable("id") Integer id){
        QueryWrapper<Camerainfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("community_id",id);
        List<Camerainfo> camerainfoList = this.camerainfoService.list(queryWrapper);
        List<CameraVo> list = new ArrayList<>();
        for(Camerainfo camerainfo : camerainfoList){
            CameraVo cameraVo = new CameraVo();
            BeanUtils.copyProperties(camerainfo,cameraVo);
            Community community = this.communityService.getById(camerainfo.getCommunityId());
            cameraVo.setCommunityName(community.getCommunityName());
            list.add(cameraVo);
        }
        return Result.ok().put("data",list);
    }


    @LogAnnotation("添加摄像头")
    @PostMapping("/add")
    public Result add(@RequestBody Camerainfo camerainfo, HttpSession session){
        SysUser user = (SysUser) session.getAttribute("user");
        camerainfo.setCreater(user.getUsername());
        boolean save = false;
        try {
            save = this.camerainfoService.save(camerainfo);
        }catch (Exception e){
            return Result.ok().put("status","fail");
        }
        if(!save) return Result.error("摄像头添加失败");
        return Result.ok().put("status","success");

    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        Camerainfo camerainfo = this.camerainfoService.getById(id);
        if(camerainfo == null) return Result.error("摄像头不存在");
        return Result.ok().put("data",camerainfo);
    }


    @LogAnnotation("编辑摄像头")
    @PutMapping("/edit")
    public Result edit(@RequestBody Camerainfo camerainfo){
        boolean edit = false;
        try {
            edit = this.camerainfoService.updateById(camerainfo);
        }catch (Exception e){
            return Result.ok().put("status","fail");
        }
        if(!edit) return Result.error("编辑摄像头失败");
        return Result.ok().put("status","success");

    }

    @LogAnnotation("删除摄像头")
    @DeleteMapping("/del")
    public Result del(@RequestBody Integer[] delId){
        boolean removeByIds = this.camerainfoService.removeByIds(Arrays.asList(delId));
        if(!removeByIds) return Result.error("摄像头删除失败");
        return Result.ok();
    }

}

