package com.xqxls.cloud.service;

import com.xqxls.cloud.entity.UmsRoleMenuRelationEntity;

import java.util.List;

/**
 * 后台角色菜单关系表 服务类接口
 *
 * @author xqxls
 * @date 2023-04-25 9:20 上午
 */
public interface UmsRoleMenuRelationService  extends IService<UmsRoleMenuRelationEntity>{

    void deleteByRoleId(Long roleId);

    void deleteByRoleIds(List<Long> roleIds);

}
