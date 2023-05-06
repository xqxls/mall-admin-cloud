package com.xqxls.cloud.controller;

import com.github.pagehelper.PageInfo;
import com.xqxls.cloud.common.api.CommonPage;
import com.xqxls.cloud.api.CommonResult;
import com.xqxls.cloud.entity.UmsResourceEntity;
import com.xqxls.cloud.response.UmsResourceRpcResponse;
import com.xqxls.cloud.service.UmsResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台资源表 前端控制器
 *
 * @author xqxls
 * @date 2023-04-25 9:20 上午
 */
@Api(tags = "后台资源表前端控制器")
@RestController
@RequestMapping("/resource")
public class UmsResourceController {

    @Autowired
    private UmsResourceService umsResourceService;

    @ApiOperation("添加后台资源")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult<Void> create(@RequestBody UmsResourceEntity umsResourceEntity) {
        int count = umsResourceService.create(umsResourceEntity);
        return CommonResult.getCountResult(count);
    }

    @ApiOperation("修改后台资源")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public CommonResult<Void> update(@PathVariable Long id,
                               @RequestBody UmsResourceEntity umsResourceEntity) {
        int count = umsResourceService.update(id, umsResourceEntity);
        return CommonResult.getCountResult(count);
    }

    @ApiOperation("根据ID获取资源详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CommonResult<UmsResourceEntity> getItem(@PathVariable Long id) {
        UmsResourceEntity umsResource = umsResourceService.findById(id);
        return CommonResult.success(umsResource);
    }

    @ApiOperation("根据ID删除后台资源")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public CommonResult<Void> delete(@PathVariable Long id) {
        int count = umsResourceService.delete(id);
        return CommonResult.getCountResult(count);
    }

    @ApiOperation("分页模糊查询后台资源")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<UmsResourceEntity>> list(@RequestParam(required = false) Long categoryId,
                                                            @RequestParam(required = false) String nameKeyword,
                                                            @RequestParam(required = false) String urlKeyword,
                                                            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageInfo<UmsResourceEntity> resourceList = umsResourceService.list(categoryId,nameKeyword, urlKeyword, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(resourceList));
    }

    @ApiOperation("查询所有后台资源")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public CommonResult<List<UmsResourceEntity>> listAll() {
        List<UmsResourceEntity> resourceList = umsResourceService.findAll();
        return CommonResult.success(resourceList);
    }

    @ApiOperation("查询所有后台资源rpc")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public CommonResult<List<UmsResourceRpcResponse>> findAll() {
        List<UmsResourceRpcResponse> result = new ArrayList<>();
        List<UmsResourceEntity> resourceList = umsResourceService.findAll();
        resourceList.forEach(resource->{
            UmsResourceRpcResponse response = new UmsResourceRpcResponse();
            BeanUtils.copyProperties(resource,response);
            result.add(response);
        });
        return CommonResult.success(result);
    }
}
