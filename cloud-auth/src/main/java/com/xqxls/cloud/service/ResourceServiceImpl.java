package com.xqxls.cloud.service;

import cn.hutool.core.collection.CollUtil;
import com.xqxls.cloud.constant.RedisConstant;
import com.xqxls.cloud.feign.UmsResourceFeign;
import com.xqxls.cloud.response.UmsResourceRpcResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 资源与角色匹配关系管理业务类
 * Created by xqxls on 2020/6/19.
 */
@Service
public class ResourceServiceImpl {

    private Set<String> resourceRolesSet;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private UmsResourceFeign umsResourceFeign;

    @PostConstruct
    public void initData() {
        List<UmsResourceRpcResponse> umsResourceRpcResponseList = umsResourceFeign.findAll().getData();
        resourceRolesSet = new HashSet<>();
        umsResourceRpcResponseList.forEach(resource -> {
            resourceRolesSet.add(resource.getUrl());
        });
        redisTemplate.opsForValue().set(RedisConstant.RESOURCE_ROLES_SET, resourceRolesSet);
    }

}
