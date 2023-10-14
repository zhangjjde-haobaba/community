package com.kgc.controller;


import com.kgc.annotation.LogAnnotation;
import com.kgc.entity.Community;
import com.kgc.entity.ManualRecord;
import com.kgc.entity.SysUser;
import com.kgc.form.ManualRecordForm;
import com.kgc.service.CommunityService;
import com.kgc.service.ManualRecordService;
import com.kgc.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2023-08-13
 */
@RestController
@RequestMapping("/sys/manual")
public class ManualRecordController {

    @Autowired
    private ManualRecordService manualRecordService;
    @Autowired
    private CommunityService communityService;

    @GetMapping("/list")
    public Result list(ManualRecordForm manualRecordForm){
        Map map = this.manualRecordService.ManualRecordList(manualRecordForm);
        return Result.ok().put("data",map);

    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        if(id == 0){
            List<Community> list = this.communityService.list();
            Map<String,Object> map = new HashMap<>();
            map.put("communityList",list);
            return Result.ok().put("data",map);
        }else{
            List<Community> list = this.communityService.list();
            Map<String,Object> map = new HashMap<>();
            map.put("communityList",list);
            ManualRecord record = this.manualRecordService.getById(id);
            map.put("manualInfo",record);
            return Result.ok().put("data",map);
        }
    }

    @LogAnnotation("添加访客记录")
    @PostMapping("/add")
    public Result add(@RequestBody ManualRecord manualRecord, HttpSession session){
        SysUser user = (SysUser) session.getAttribute("user");
        manualRecord.setUserName(user.getUsername());
        boolean save = this.manualRecordService.save(manualRecord);
        if(!save) return Result.error("访客记录添加失败");
        return Result.ok();
    }

    @LogAnnotation("编辑访客记录")
    @PutMapping("/edit")
    public Result edit(@RequestBody ManualRecord manualRecord){
        boolean update = this.manualRecordService.updateById(manualRecord);
        if(!update) return Result.error("更新失败");
        return Result.ok();
    }

    @LogAnnotation("删除访客记录")
    @DeleteMapping("/del")
    public Result del(@RequestBody Integer[] ids){
        boolean removeByIds = this.manualRecordService.removeByIds(Arrays.asList(ids));
        if(!removeByIds) return Result.error("删除失败");
        return Result.ok();
    }

}

