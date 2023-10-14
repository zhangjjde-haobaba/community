package com.kgc.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * Created by jiang on 8/14/23 4:43 PM
 */
@Data
public class PersonVo {

    private Integer personId;

    private Integer communityId;

    private String termName;

    private String houseNo;

    private String userName;

    private String sex;

    private String mobile;

    private String faceUrl;

    private String personType;

    private Integer state;

    private String creater;

    private Date createTime;

    private String remark;

    private String faceBase;

    private String communityName;
}
