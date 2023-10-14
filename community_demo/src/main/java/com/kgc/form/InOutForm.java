package com.kgc.form;

import lombok.Data;

import java.util.Date;

/**
 * Created by jiang on 8/20/23 5:39 PM
 */
@Data
public class InOutForm {
    private Long page;
    private Long limit;
    private String userName;
    private String startDate;
    private String endDate;
}
