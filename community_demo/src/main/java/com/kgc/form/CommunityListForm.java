package com.kgc.form;

import lombok.Data;

/**
 * Created by jiang on 8/9/23 4:19 PM
 */
@Data
public class CommunityListForm {
    private Long page;
    private Long limit;
    private Integer communityId;
    private String communityName;
}
