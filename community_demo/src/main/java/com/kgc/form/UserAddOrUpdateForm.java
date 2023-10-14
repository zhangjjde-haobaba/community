package com.kgc.form;

import lombok.Data;

/**
 * Created by jiang on 8/21/23 4:09 PM
 */
@Data
public class UserAddOrUpdateForm {
    private Integer userId;
    private String username;
    private String password;
    private String realName;
    private String mobile;
    private Integer status;
    private Integer roleId;
    private String contact;
}
