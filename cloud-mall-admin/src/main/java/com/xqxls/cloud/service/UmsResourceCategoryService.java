package com.xqxls.cloud.service;

import com.xqxls.cloud.entity.UmsResourceCategoryEntity;

/**
 * 资源分类表 服务类接口
 *
 * @author xqxls
 * @date 2023-04-25 9:20 上午
 */
public interface UmsResourceCategoryService extends IService<UmsResourceCategoryEntity>{

    int create(UmsResourceCategoryEntity umsResourceCategory);
}
