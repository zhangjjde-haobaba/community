package com.kgc.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by jiang on 8/13/23 11:58 AM
 */
@Component
public class FillHandler implements MetaObjectHandler {
    /**
     * 用来自动填充时间
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("inTime", new Date(), metaObject);
        this.setFieldValByName("signTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("outTime", new Date(), metaObject);
    }
}
