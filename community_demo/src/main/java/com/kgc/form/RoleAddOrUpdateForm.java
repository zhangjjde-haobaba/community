package com.kgc.form;

import lombok.Data;

import java.util.List;

/**
 * Created by jiang on 8/22/23 1:27 PM
 */
@Data
public class RoleAddOrUpdateForm {
    private Integer roleId; //用于修改
    private String roleName;
    private Integer type;
    private String remark;
    private List<Integer> menuIdList;

}
