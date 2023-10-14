package com.kgc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 菜单管理
 * </p>
 *
 * @author admin
 * @since 2023-08-04
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class SysMenu implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 父菜单ID，一级菜单为0
     */
      private Integer parentId;

      /**
     * 菜单名称
     */
      private String name;

      /**
     * 菜单URL
     */
      private String path;

      /**
     * 组件路径
     */
      private String component;

    private Boolean menuType;

    private Integer status;

      /**
     * 菜单图标
     */
      private String icon;

      /**
     * 排序
     */
      private Integer sort;

    private String hidden;


}
