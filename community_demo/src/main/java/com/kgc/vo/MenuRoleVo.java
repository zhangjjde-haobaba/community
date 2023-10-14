package com.kgc.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by jiang on 8/22/23 12:28 PM
 */
@Data
public class MenuRoleVo {

    private Integer id;

    private Integer parentId;

    private List<MenuRoleVo> children;

    private String name;

    private String path;

    private Boolean menuType;

    private Integer status;

    private String icon;

    private Integer sort;

    private String hidden;

}
