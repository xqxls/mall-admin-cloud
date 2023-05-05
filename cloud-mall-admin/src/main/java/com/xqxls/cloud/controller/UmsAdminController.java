package com.xqxls.cloud.controller;

import com.github.pagehelper.PageInfo;
import com.xqxls.cloud.aop.annotation.WebLog;
import com.xqxls.cloud.common.api.CommonPage;
import com.xqxls.cloud.common.api.CommonResult;
import com.xqxls.cloud.dto.UmsAdminLoginDto;
import com.xqxls.cloud.dto.UmsAdminRegisterDto;
import com.xqxls.cloud.dto.UpdateAdminPasswordDto;
import com.xqxls.cloud.entity.UmsAdminEntity;
import com.xqxls.cloud.entity.UmsResourceEntity;
import com.xqxls.cloud.entity.UmsRoleEntity;
import com.xqxls.cloud.response.UmsAdminRpcResponse;
import com.xqxls.cloud.response.UmsResourceRpcResponse;
import com.xqxls.cloud.service.UmsAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台用户表 前端控制器
 *
 * @author xqxls
 * @date 2023-04-25 9:20 上午
 */
@Api(tags = "后台用户表前端控制器")
@RestController
@RequestMapping("/admin")
public class UmsAdminController {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private UmsAdminService adminService;

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public CommonResult<UmsAdminEntity> register(@Validated @RequestBody UmsAdminRegisterDto umsAdminRegisterDto) {
        UmsAdminEntity umsAdminEntity = adminService.register(umsAdminRegisterDto);
        if (umsAdminEntity == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(umsAdminEntity);
    }

    @ApiOperation(value = "登出功能")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public CommonResult<Void> logout() {
        return CommonResult.success(null);
    }

    @ApiOperation("根据用户名或姓名分页获取用户列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<UmsAdminEntity>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                         @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageInfo<UmsAdminEntity> adminList = adminService.list(keyword, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(adminList));
    }

    @ApiOperation("获取指定用户信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CommonResult<UmsAdminEntity> getItem(@PathVariable Long id) {
        UmsAdminEntity admin = adminService.findById(id);
        return CommonResult.success(admin);
    }

    @ApiOperation("修改指定用户信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public CommonResult<Void> update(@PathVariable Long id, @RequestBody UmsAdminEntity admin) {
        int count = adminService.update(id, admin);
        return CommonResult.getCountResult(count);
    }

    @ApiOperation("修改指定用户密码")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.PUT)
    public CommonResult<Integer> updatePassword(@Validated @RequestBody UpdateAdminPasswordDto updateAdminPasswordDto) {
        int status = adminService.updatePassword(updateAdminPasswordDto);
        if (status > 0) {
            return CommonResult.success(status);
        } else if (status == -1) {
            return CommonResult.failed("提交参数不合法");
        } else if (status == -2) {
            return CommonResult.failed("找不到该用户");
        } else if (status == -3) {
            return CommonResult.failed("旧密码错误");
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("删除指定用户信息")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public CommonResult<Void> delete(@PathVariable Long id) {
        int count = adminService.delete(id);
        return CommonResult.getCountResult(count);
    }

    @ApiOperation("修改帐号状态")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.PUT)
    public CommonResult<Void> updateStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {
        UmsAdminEntity umsAdmin = new UmsAdminEntity();
        umsAdmin.setStatus(status);
        int count = adminService.update(id,umsAdmin);
        return CommonResult.getCountResult(count);
    }

    @ApiOperation("给用户分配角色")
    @RequestMapping(value = "/allocateRole", method = RequestMethod.POST)
    public CommonResult<Integer> allocateRole(@RequestParam("adminId") Long adminId,
                                   @RequestParam("roleIds") List<Long> roleIds) {
        int count = adminService.allocateRole(adminId, roleIds);
        if (count >= 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取指定用户的角色")
    @RequestMapping(value = "/role/{adminId}", method = RequestMethod.GET)
    public CommonResult<List<UmsRoleEntity>> getRoleList(@PathVariable Long adminId) {
        List<UmsRoleEntity> roleList = adminService.getRoleList(adminId);
        return CommonResult.success(roleList);
    }

    @ApiOperation("根据用户名获取用户")
    @RequestMapping(value = "/getAdminByUsername", method = RequestMethod.GET)
    public CommonResult<UmsAdminRpcResponse> getAdminByUsername(@RequestParam(value = "username") String username) {
        UmsAdminRpcResponse result = new UmsAdminRpcResponse();
        UmsAdminEntity umsAdminEntity = adminService.getAdminByUsername(username);
        BeanUtils.copyProperties(umsAdminEntity,result);
        return CommonResult.success(result);
    }

    @ApiOperation("根据用户id获取资源")
    @RequestMapping(value = "/getResourceList", method = RequestMethod.GET)
    public CommonResult<List<UmsResourceRpcResponse>> getResourceList(@RequestParam(value = "adminId") Long adminId) {
        List<UmsResourceRpcResponse> result = new ArrayList<>();
        List<UmsResourceEntity> umsResourceEntityList = adminService.getResourceList(adminId);
        umsResourceEntityList.forEach(resource->{
            UmsResourceRpcResponse response = new UmsResourceRpcResponse();
            BeanUtils.copyProperties(resource,response);
            result.add(response);
        });
        return CommonResult.success(result);
    }

}
