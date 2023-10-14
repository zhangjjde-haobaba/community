package com.kgc.vo;

import lombok.Data;

/**
 * Created by jiang on 8/21/23 12:29 PM
 */
@Data
public class UserVo {

    private Integer userId;
    private String username;
    private String realName;
    private String contact;
    private String mobile;
    private Integer status;
    private String roleName;
}
