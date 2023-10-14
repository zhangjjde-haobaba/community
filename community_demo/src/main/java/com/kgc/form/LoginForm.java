package com.kgc.form;

import lombok.Data;

/**
 * Created by jiang on 7/30/23 10:56 AM
 */
@Data
public class LoginForm {
    private String captcha;
    private String password;
    private String username;
    private String uuid;
}
