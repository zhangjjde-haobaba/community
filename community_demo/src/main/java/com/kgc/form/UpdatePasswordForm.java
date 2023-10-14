package com.kgc.form;

import lombok.Data;

/**
 * Created by jiang on 8/8/23 11:58 AM
 */
@Data
public class UpdatePasswordForm {
    private String password;
    private String newPassword;
    private String confirmPassword;
}
