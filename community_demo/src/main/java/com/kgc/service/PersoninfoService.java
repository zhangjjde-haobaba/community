package com.kgc.service;

import com.kgc.entity.Personinfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kgc.form.PersonListForm;
import com.kgc.vo.PageVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2023-08-09
 */
public interface PersoninfoService extends IService<Personinfo> {

    public PageVo personList(PersonListForm personListForm);

}
