package com.kgc.form;

import lombok.Data;

/**
 * Created by jiang on 8/14/23 4:09 PM
 */
@Data
public class PersonListForm {
    private Long page;
    private Long limit;
    private String userName;
    private Integer communityId;
    private String mobile;
}
