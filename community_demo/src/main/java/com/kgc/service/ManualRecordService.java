package com.kgc.service;

import com.kgc.entity.ManualRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kgc.form.ManualRecordForm;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2023-08-13
 */
public interface ManualRecordService extends IService<ManualRecord> {

    //因为不仅需要pageList也需要communityList所以封装为一个map
    public Map ManualRecordList(ManualRecordForm manualRecordForm);

}
