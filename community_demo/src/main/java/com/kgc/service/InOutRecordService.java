package com.kgc.service;

import com.kgc.entity.InOutRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kgc.form.InOutForm;
import com.kgc.vo.PageVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2023-08-08
 */
public interface InOutRecordService extends IService<InOutRecord> {

    public Map chart();

    public PageVo inOutRecordList(InOutForm inOutForm);

}
