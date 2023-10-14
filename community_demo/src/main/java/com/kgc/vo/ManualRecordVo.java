package com.kgc.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * Created by jiang on 8/20/23 10:56 PM
 */
@Data
public class ManualRecordVo {
    private Integer manualRecordId;
    private String communityName;
    private String visitor;
    private String mobile;
    private String cardNo;
    private String termName;
    private String houseNo;
    private String interviewee;
    private String remark;
    private Date inTime;
    private Date outTime;
    private String userName;
    private Date signTime;

}
