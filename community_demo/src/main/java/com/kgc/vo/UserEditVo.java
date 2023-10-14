package com.kgc.vo;

import lombok.Data;

/**
 * Created by jiang on 8/21/23 4:30 PM
 */
@Data
public class UserEditVo {
    private Integer userId;
    private String username;
    private String password;
    private String realName;
    private String contact;
    private String mobile;
    private Integer status;
    private Integer roleId;
}
