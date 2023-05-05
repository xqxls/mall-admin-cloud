package com.xqxls.cloud.convert.mapstruct;

import com.xqxls.cloud.dto.UmsAdminRegisterDto;
import com.xqxls.cloud.entity.UmsAdminEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UmsAdminMapper {

    UmsAdminMapper INSTANCE = Mappers.getMapper(UmsAdminMapper.class);

    UmsAdminEntity umsAdminRegisterToEntity(UmsAdminRegisterDto umsAdminRegisterDto);
}
