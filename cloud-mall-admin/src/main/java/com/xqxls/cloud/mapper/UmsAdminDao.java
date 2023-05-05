package com.xqxls.cloud.mapper;

import com.xqxls.cloud.base.TkBaseMapper;
import com.xqxls.cloud.entity.UmsAdminEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 后台用户表 Mapper 接口
 * 
 * @author xqxls
 * @date 2023-04-25 9:20 上午
 */
@Component
public interface UmsAdminDao extends TkBaseMapper<UmsAdminEntity> {

    List<Long> getAdminIdList(@Param("resourceId") Long resourceId);
}