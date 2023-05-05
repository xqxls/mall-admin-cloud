package com.xqxls.cloud.service;

import com.xqxls.cloud.entity.UmsRoleResourceRelationEntity;

import java.util.List;

/**
 * 后台角色资源关系表 服务类接口
 *
 * @author xqxls
 * @date 2023-04-25 9:20 上午
 */
public interface UmsRoleResourceRelationService extends IService<UmsRoleResourceRelationEntity>{

    void deleteByRoleId(Long roleId);

    void deleteByRoleIds(List<Long> roleIds);

}
