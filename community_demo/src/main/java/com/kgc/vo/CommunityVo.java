package com.kgc.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * Created by jiang on 8/9/23 5:11 PM
 */
@Data
public class CommunityVo {

    private Integer communityId;

    /**
     * 小区名称
     */
    private String communityName;

    /**
     * 楼栋数量
     */
    private Integer termCount;

    /**
     * 序号
     */
    private Integer seq;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 经度
     */
    private Float lng;

    /**
     * 维度
     */
    private Float lat;

    private Integer personCnt;
}
