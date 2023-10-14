package com.kgc.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by jiang on 8/9/23 4:29 PM
 */
@Data
public class PageVo {

    private Long totalCount;
    private Long pageSize;
    private Long totalPage;
    private Long currPage;
    private List list;

}
