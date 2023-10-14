package com.kgc.form;

import lombok.Data;

/**
 * Created by jiang on 8/21/23 12:26 PM
 */
@Data
public class UserForm {
    private Long page;
    private Long limit;
    private String username;
    private String realName;
}
