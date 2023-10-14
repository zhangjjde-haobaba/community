package com.kgc.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * Created by jiang on 8/13/23 9:48 PM
 */
@Data
public class CameraVo {

    private Long camerainfoId;

    /**
     * 小区ID
     */
    private Long communityId;

    /**
     * 摄像头名称
     */
    private String cameraName;

    /**
     * 唯一MAC地址
     */
    private String macId;

    /**
     * 出入方向（1进，2出）
     */
    private Integer direction;

    /**
     * 状态（1启用，2禁用）
     */
    private Integer state;

    /**
     * 序号
     */
    private Long seq;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */

    private Date createTime;

    /**
     * 备注
     */
    private String remark;

    private String communityName;
}
