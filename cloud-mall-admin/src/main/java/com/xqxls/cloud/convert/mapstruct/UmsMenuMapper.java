package com.xqxls.cloud.convert.mapstruct;

import com.xqxls.cloud.dto.node.UmsMenuNode;
import com.xqxls.cloud.entity.UmsMenuEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Description:
 * @Author: huzhuo
 * @Date: Created in 2023/4/26 20:36
 */
@Mapper
public interface UmsMenuMapper {

    UmsMenuMapper INSTANCE = Mappers.getMapper(UmsMenuMapper.class);

    UmsMenuNode umsMenuEntityToNode(UmsMenuEntity umsMenuEntity);
}
