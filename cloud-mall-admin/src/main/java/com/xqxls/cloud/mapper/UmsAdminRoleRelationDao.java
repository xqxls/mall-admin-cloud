package com.xqxls.cloud.mapper;

import com.xqxls.cloud.base.TkBaseMapper;
import com.xqxls.cloud.entity.UmsAdminRoleRelationEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 后台用户和角色关系表 Mapper 接口
 * 
 * @author xqxls
 * @date 2023-04-25 9:20 上午
 */
@Component
public interface UmsAdminRoleRelationDao extends TkBaseMapper<UmsAdminRoleRelationEntity> {

    int addBatch(@Param("list") List<UmsAdminRoleRelationEntity> list);

    int delByAdminId(@Param("adminId") Long adminId);
}