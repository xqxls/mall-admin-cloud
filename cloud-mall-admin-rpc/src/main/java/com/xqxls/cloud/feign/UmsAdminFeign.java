package com.xqxls.cloud.feign;

import com.xqxls.cloud.api.CommonResult;
import com.xqxls.cloud.response.UmsAdminRpcResponse;
import com.xqxls.cloud.response.UmsResourceRpcResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "cloud-mall-admin", path = "/admin")
public interface UmsAdminFeign {

    @GetMapping(value = "/getAdminByUsername")
    CommonResult<UmsAdminRpcResponse> getAdminByUsername(@RequestParam(value = "username") String username);

    @GetMapping(value = "/getResourceList")
    CommonResult<List<UmsResourceRpcResponse>> getResourceList(@RequestParam(value = "adminId") Long adminId);
}
