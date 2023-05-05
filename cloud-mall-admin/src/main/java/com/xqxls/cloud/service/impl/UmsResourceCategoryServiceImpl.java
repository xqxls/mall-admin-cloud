package com.xqxls.cloud.service.impl;

import com.xqxls.cloud.entity.UmsResourceCategoryEntity;
import com.xqxls.cloud.mapper.UmsResourceCategoryDao;
import com.xqxls.cloud.service.UmsResourceCategoryService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 资源分类表 服务实现类
 *
 * @author xqxls
 * @date 2023-04-25 9:20 上午
 */
@Service
public class UmsResourceCategoryServiceImpl extends ServiceImpl<UmsResourceCategoryDao, UmsResourceCategoryEntity> implements UmsResourceCategoryService {

    @Override
    public int create(UmsResourceCategoryEntity umsResourceCategory) {
        umsResourceCategory.setPrimaryId();
        umsResourceCategory.setCreateTime(new Date());
        return this.add(umsResourceCategory);
    }
}
