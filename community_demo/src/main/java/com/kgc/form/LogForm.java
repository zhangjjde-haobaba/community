package com.kgc.form;

import lombok.Data;

/**
 * Created by jiang on 8/23/23 9:53 PM
 */
@Data
public class LogForm {
    private Long page;
    private Long limit;
    private String username;
    private String startDate;
    private String endDate;
    private String operation;
}
