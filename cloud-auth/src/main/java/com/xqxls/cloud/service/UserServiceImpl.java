package com.xqxls.cloud.service;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.xqxls.cloud.domain.UserDTO;
import com.xqxls.cloud.feign.UmsAdminFeign;
import com.xqxls.cloud.response.UmsAdminRpcResponse;
import com.xqxls.cloud.response.UmsResourceRpcResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户管理业务类
 * Created by macro on 2020/6/19.
 */
@Service
public class UserServiceImpl{

    @Autowired
    private UmsAdminFeign umsAdminFeign;

    public UserDTO loadUserByUsername(String username) {
        UmsAdminRpcResponse umsAdminRpcResponse = umsAdminFeign.getAdminByUsername(username).getData();
        if (Objects.isNull(umsAdminRpcResponse)) {
            return null;
        }
        List<UmsResourceRpcResponse> resourceList = umsAdminFeign.getResourceList(umsAdminRpcResponse.getId()).getData();

        return UserDTO.builder()
                .id(umsAdminRpcResponse.getId())
                .username(umsAdminRpcResponse.getUsername())
                .password(umsAdminRpcResponse.getPassword())
                .permissionList(resourceList.stream().map(UmsResourceRpcResponse::getUrl).collect(Collectors.toList()))
                .build();
    }

    public SaTokenInfo login(String username, String password) {
        SaTokenInfo saTokenInfo = null;
        UserDTO userDTO = loadUserByUsername(username);
        if (userDTO == null) {
            return null;
        }
        if (!SaSecureUtil.md5(password).equals(userDTO.getPassword())) {
            return null;
        }
        // 密码校验成功后登录，一行代码实现登录
        StpUtil.login(userDTO.getId());
        // 将用户信息存储到Session中
        StpUtil.getSession().set("userInfo",userDTO);
        // 获取当前登录用户Token信息
        saTokenInfo = StpUtil.getTokenInfo();
        return saTokenInfo;
    }
}
