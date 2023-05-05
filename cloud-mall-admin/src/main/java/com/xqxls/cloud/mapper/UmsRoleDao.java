package com.xqxls.cloud.mapper;

import com.xqxls.cloud.base.TkBaseMapper;
import com.xqxls.cloud.entity.UmsRoleEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 后台用户角色表 Mapper 接口
 * 
 * @author xqxls
 * @date 2023-04-25 9:20 上午
 */
@Component
public interface UmsRoleDao extends TkBaseMapper<UmsRoleEntity> {

    /**
     * 获取用户所有角色
     */
    List<UmsRoleEntity> getRoleList(@Param("adminId") Long adminId);

    /**
     * 批量删除角色
     * @param ids
     */
    void delByIds(@Param("ids") List<Long> ids);
}