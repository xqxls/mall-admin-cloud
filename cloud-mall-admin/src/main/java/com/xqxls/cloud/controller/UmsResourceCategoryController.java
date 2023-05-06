package com.xqxls.cloud.controller;

import com.xqxls.cloud.api.CommonResult;
import com.xqxls.cloud.entity.UmsResourceCategoryEntity;
import com.xqxls.cloud.service.UmsResourceCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资源分类表 前端控制器
 *
 * @author xqxls
 * @date 2023-04-25 9:20 上午
 */
@Api(tags = "资源分类表前端控制器")
@RestController
@RequestMapping("/resourceCategory")
public class UmsResourceCategoryController {

    @Autowired
    private UmsResourceCategoryService umsResourceCategoryService;

    @ApiOperation("查询所有后台资源分类")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public CommonResult<List<UmsResourceCategoryEntity>> listAll() {
        List<UmsResourceCategoryEntity> resourceList = umsResourceCategoryService.findAll();
        return CommonResult.success(resourceList);
    }

    @ApiOperation("添加后台资源分类")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult<Void> create(@RequestBody UmsResourceCategoryEntity umsResourceCategory) {
        int count = umsResourceCategoryService.create(umsResourceCategory);
        return CommonResult.getCountResult(count);
    }

    @ApiOperation("修改后台资源分类")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public CommonResult<Void> update(@PathVariable Long id,
                               @RequestBody UmsResourceCategoryEntity umsResourceCategory) {
        umsResourceCategory.setId(id);
        int count = umsResourceCategoryService.update(umsResourceCategory);
        return CommonResult.getCountResult(count);
    }

    @ApiOperation("根据ID删除后台资源")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public CommonResult<Void> delete(@PathVariable Long id) {
        int count = umsResourceCategoryService.deleteById(id);
        return CommonResult.getCountResult(count);
    }
}
