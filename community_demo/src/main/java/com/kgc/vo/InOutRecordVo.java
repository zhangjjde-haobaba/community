package com.kgc.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * Created by jiang on 8/20/23 6:19 PM
 */
@Data
public class InOutRecordVo {
    private Integer inOutRecordId;
    private Date inTime;
    private Date outTime;
    private String inPic;
    private String outPic;
    private String communityName;
    private String termName;
    private String houseNo;
    private String userName;

}
