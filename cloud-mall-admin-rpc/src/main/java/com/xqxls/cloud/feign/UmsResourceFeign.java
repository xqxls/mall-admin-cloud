package com.xqxls.cloud.feign;

import com.xqxls.cloud.common.CommonResult;
import com.xqxls.cloud.response.UmsResourceRpcResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "cloud-mall-admin", path = "/resource")
public interface UmsResourceFeign {

    @GetMapping(value = "/findAll")
    CommonResult<List<UmsResourceRpcResponse>> findAll();
}
