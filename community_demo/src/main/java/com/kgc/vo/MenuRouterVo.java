package com.kgc.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by jiang on 8/7/23 11:30 AM
 */
@Data
public class MenuRouterVo {

    private String name;

    private String path;

    private String component;

    private String hidden;

    private String redirect = "noRedirect";

    private Boolean alwaysShow = true;

    private MetaVo meta;

    private List<ChildMenuRouterVo> children;


}
