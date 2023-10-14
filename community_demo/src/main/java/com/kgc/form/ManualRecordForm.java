package com.kgc.form;

import lombok.Data;

/**
 * Created by jiang on 8/20/23 10:33 PM
 */
@Data
public class ManualRecordForm {
    private Long page;
    private Long limit;
    private Integer communityId;
    private String mobile;
    private String visitor;

}
