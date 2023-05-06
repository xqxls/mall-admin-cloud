package com.xqxls.cloud.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xqxls.cloud.cache.UmsAdminCacheService;
import com.xqxls.cloud.common.cache.RedisService;
import com.xqxls.cloud.constant.RedisConstant;
import com.xqxls.cloud.entity.UmsResourceEntity;
import com.xqxls.cloud.mapper.UmsResourceDao;
import com.xqxls.cloud.service.UmsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 后台资源表 服务实现类
 *
 * @author xqxls
 * @date 2023-04-25 9:20 上午
 */
@Service
public class UmsResourceServiceImpl extends ServiceImpl<UmsResourceDao, UmsResourceEntity> implements UmsResourceService {

    @Autowired
    private UmsAdminCacheService adminCacheService;

    @Autowired
    private UmsResourceDao umsResourceDao;

    @Autowired
    private RedisService redisService;

    @PostConstruct
    public void initData() {
        /**
         * 这里一个可行的方案是，如果资源表存的是明确的不带模式匹配的路径，可以在初始化的时候定义一个hash类型的redis数据，然后
         * 将资源路径作为key，资源的某种凭证组合作为value(比如id+name)，存入redis。在auth服务中可以通过当前登录用户id拿到
         * 资源，再将资源组合转化为authorities。最后在gateway服务中，拦截用户请求，获取url，找到redis中url对应的凭证，看
         * authorities里面是否包含访问这个url的凭证【本项目因为数据库资源url是模式串，未实现这种方案】
         */
        List<UmsResourceEntity> umsResourceEntityList = this.findAll();
        Set<String> resourceRolesSet = new HashSet<>();
        umsResourceEntityList.forEach(resource -> {
            resourceRolesSet.add(resource.getUrl());
        });
        redisService.set(RedisConstant.RESOURCE_ROLES_SET, resourceRolesSet);
    }

    @Override
    public int create(UmsResourceEntity umsResourceEntity) {
        umsResourceEntity.setPrimaryId();
        umsResourceEntity.setCreateTime(new Date());
        return this.add(umsResourceEntity);
    }

    @Override
    public int update(Long id, UmsResourceEntity umsResourceEntity) {
        umsResourceEntity.setId(id);
        int count = this.update(umsResourceEntity);
        adminCacheService.delResourceListByResource(id);
        return count;
    }

    @Override
    public int delete(Long id) {
        int count = this.deleteById(id);
        adminCacheService.delResourceListByResource(id);
        return count;
    }

    @Override
    public PageInfo<UmsResourceEntity> list(Long categoryId, String nameKeyword, String urlKeyword, Integer page, Integer size) {
        //分页
        PageHelper.startPage(page,size);
        Example example = new Example(UmsResourceEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if(Objects.nonNull(categoryId)){
            criteria.andEqualTo("categoryId", categoryId);
        }
        if(StrUtil.isNotEmpty(nameKeyword)){
            criteria.andLike("name", "%"+nameKeyword+"%");
        }
        if(StrUtil.isNotEmpty(urlKeyword)){
            criteria.andLike("url", "%"+urlKeyword+"%");
        }
        return new PageInfo<>(umsResourceDao.selectByExample(example));
    }


}
