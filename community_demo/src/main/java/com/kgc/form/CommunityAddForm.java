package com.kgc.form;

import lombok.Data;

/**
 * Created by jiang on 8/11/23 4:10 PM
 */
@Data
public class CommunityAddForm {
    private Integer communityId;
    private String communityName;
    /**
     * 经度
     */
    private Float lng;

    /**
     * 维度
     */
    private Float lat;
    /**
     * 序号
     */
    private Integer seq;
    /**
     * 楼栋数量
     */
    private Integer termCount;
}
