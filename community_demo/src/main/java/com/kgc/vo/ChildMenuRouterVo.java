package com.kgc.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by jiang on 8/7/23 12:29 PM
 */
@Data
public class ChildMenuRouterVo {
    private String name;

    private String path;

    private String component;

    private String hidden;

    private MetaVo meta;

}
