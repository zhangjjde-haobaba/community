package com.kgc.service;

import com.kgc.entity.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kgc.form.LogForm;
import com.kgc.vo.PageVo;

/**
 * <p>
 * 操作日志表 服务类
 * </p>
 *
 * @author admin
 * @since 2023-08-23
 */
public interface SysLogService extends IService<SysLog> {

    public PageVo logList(LogForm logForm);

}
