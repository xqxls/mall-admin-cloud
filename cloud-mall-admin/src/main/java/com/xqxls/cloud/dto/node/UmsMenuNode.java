package com.xqxls.cloud.dto.node;

import com.xqxls.cloud.entity.UmsMenuEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 胡卓
 * @create 2023-04-26 17:36
 * @Description
 */
@Data
public class UmsMenuNode extends UmsMenuEntity {

    @ApiModelProperty(value = "子级菜单")
    private List<UmsMenuNode> children;
}
