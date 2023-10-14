package com.kgc.service;

import com.kgc.entity.Community;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kgc.form.CommunityListForm;
import com.kgc.vo.PageVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2023-08-09
 */
public interface CommunityService extends IService<Community> {

    public PageVo communityList(CommunityListForm communityListForm);

}
