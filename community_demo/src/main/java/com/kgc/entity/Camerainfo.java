package com.kgc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author admin
 * @since 2023-08-13
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class Camerainfo implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "cameraInfo_id", type = IdType.AUTO)
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
        @TableField(fill = FieldFill.INSERT)
      private Date createTime;

      /**
     * 备注
     */
      private String remark;


}
