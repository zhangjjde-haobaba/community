package com.kgc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kgc.entity.SysLog;
import com.kgc.form.LogForm;
import com.kgc.mapper.SysLogMapper;
import com.kgc.service.SysLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kgc.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author admin
 * @since 2023-08-23
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Autowired(required = false)
    private SysLogMapper sysLogMapper;

    @Override
    public PageVo logList(LogForm logForm) {
        Page<SysLog> page = new Page<>(logForm.getPage(),logForm.getLimit());
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(logForm.getUsername()),"username",logForm.getUsername())
                .like(StringUtils.isNotBlank(logForm.getOperation()),"operation",logForm.getOperation())
                .between(StringUtils.isNotBlank(logForm.getStartDate()) && StringUtils.isNotBlank(logForm.getEndDate()),"create_time",logForm.getStartDate(),logForm.getEndDate());
        Page<SysLog> resultPage = this.sysLogMapper.selectPage(page, queryWrapper);
        PageVo pageVo = new PageVo();
        pageVo.setTotalCount(resultPage.getTotal());
        pageVo.setCurrPage(resultPage.getCurrent());
        pageVo.setPageSize(resultPage.getSize());
        pageVo.setTotalPage(resultPage.getPages());
        pageVo.setList(resultPage.getRecords());

        return pageVo;
    }
}
