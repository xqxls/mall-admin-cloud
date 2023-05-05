package com.xqxls.cloud.service;

import cn.hutool.core.collection.CollUtil;
import com.xqxls.cloud.constant.RedisConstant;
import com.xqxls.cloud.feign.UmsResourceFeign;
import com.xqxls.cloud.response.UmsResourceRpcResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 资源与角色匹配关系管理业务类
 * Created by xqxls on 2020/6/19.
 */
@Service
public class ResourceServiceImpl {

    private Map<String, List<String>> resourceRolesMap;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private UmsResourceFeign umsResourceFeign;

    @PostConstruct
    public void initData() {
        List<UmsResourceRpcResponse> umsResourceRpcResponseList = umsResourceFeign.findAll().getData();
        resourceRolesMap = new TreeMap<>();
        umsResourceRpcResponseList.forEach(resource -> {
            resourceRolesMap.put(resource.getUrl(),CollUtil.toList(resource.getId()+":"+resource.getName()));
        });
        redisTemplate.opsForHash().putAll(RedisConstant.RESOURCE_ROLES_MAP, resourceRolesMap);
    }

}
