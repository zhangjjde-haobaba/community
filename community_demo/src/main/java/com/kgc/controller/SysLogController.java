package com.kgc.controller;


import com.kgc.form.LogForm;
import com.kgc.service.SysLogService;
import com.kgc.util.Result;
import com.kgc.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.Arrays;

/**
 * <p>
 * 操作日志表 前端控制器
 * </p>
 *
 * @author admin
 * @since 2023-08-23
 */
@RestController
@RequestMapping("/sys/log")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    @GetMapping("/list")
    public Result list(LogForm logForm){
        PageVo pageVo = this.sysLogService.logList(logForm);
        return Result.ok().put("data",pageVo);
    }

    @DeleteMapping("/del")
    public Result del(@RequestBody Integer[] ids){
        boolean removeByIds = this.sysLogService.removeByIds(Arrays.asList(ids));
        if(!removeByIds) return Result.error("删除日志失败");
        return Result.ok();
    }

}

