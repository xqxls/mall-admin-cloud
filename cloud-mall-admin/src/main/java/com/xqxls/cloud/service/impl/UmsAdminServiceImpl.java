package com.xqxls.cloud.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xqxls.cloud.cache.UmsAdminCacheService;
import com.xqxls.cloud.common.exception.Asserts;
import com.xqxls.cloud.convert.mapstruct.UmsAdminMapper;
import com.xqxls.cloud.dto.UmsAdminRegisterDto;
import com.xqxls.cloud.dto.UpdateAdminPasswordDto;
import com.xqxls.cloud.entity.*;
import com.xqxls.cloud.mapper.*;
import com.xqxls.cloud.service.UmsAdminService;
import com.xqxls.cloud.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * 后台用户表 服务实现类
 *
 * @author xqxls
 * @date 2023-04-25 9:20 上午
 */
@Service
@Slf4j
public class UmsAdminServiceImpl extends ServiceImpl<UmsAdminDao,UmsAdminEntity> implements UmsAdminService {

    @Autowired
    private UmsAdminDao umsAdminDao;

    @Autowired
    private UmsRoleDao umsRoleDao;

    @Autowired
    private UmsResourceDao umsResourceDao;

    @Autowired
    private UmsAdminRoleRelationDao umsAdminRoleRelationDao;


    @Override
    public UmsAdminEntity getAdminByUsername(String username) {
        UmsAdminEntity admin = this.getCacheService().getAdmin(username);
        if(admin!=null) return  admin;
        Example example = new Example(UmsAdminEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        List<UmsAdminEntity> umsAdminEntityList = umsAdminDao.selectByExample(example);
        if (!CollectionUtils.isEmpty(umsAdminEntityList)&&umsAdminEntityList.size()>0) {
            admin = umsAdminEntityList.get(0);
            getCacheService().setAdmin(admin);
            return admin;
        }
        return null;
    }

    @Override
    public List<UmsRoleEntity> getRoleList(Long adminId) {
        return umsRoleDao.getRoleList(adminId);
    }

    @Override
    public UmsAdminCacheService getCacheService() {
        return SpringUtil.getBean(UmsAdminCacheService.class);
    }

    @Override
    public UmsAdminEntity register(UmsAdminRegisterDto umsAdminRegisterDto) {
        UmsAdminEntity umsAdminEntity = UmsAdminMapper.INSTANCE.umsAdminRegisterToEntity(umsAdminRegisterDto);
        umsAdminEntity.setPrimaryId();
        umsAdminEntity.setCreateTime(new Date());
        umsAdminEntity.setStatus(1);
        //查询是否有相同用户名的用户
        UmsAdminEntity queryEntity = this.getAdminByUsername(umsAdminEntity.getUsername());
        if (Objects.nonNull(queryEntity)) {
            Asserts.fail("用户名已存在");
        }
        //将密码进行加密操作

        String encodePassword = DigestUtil.md5Hex(umsAdminEntity.getPassword());
        umsAdminEntity.setPassword(encodePassword);
        this.add(umsAdminEntity);
        return umsAdminEntity;
    }

    @Override
    public PageInfo<UmsAdminEntity> list(String keyword, Integer page, Integer size) {
        //分页
        PageHelper.startPage(page,size);
        Example example = new Example(UmsAdminEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if(StrUtil.isNotEmpty(keyword)){
            criteria.andLike("username", "%"+keyword+"%");
            criteria.orLike("nickName", "%"+keyword+"%");
        }
        return new PageInfo<>(umsAdminDao.selectByExample(example));
    }

    @Override
    public int update(Long id, UmsAdminEntity admin) {
        admin.setId(id);
        UmsAdminEntity queryAdminEntity = this.findById(id);
        if(queryAdminEntity.getPassword().equals(admin.getPassword())){
            //与原加密密码相同的不需要修改
            admin.setPassword(null);
        }else{
            //与原加密密码不同的需要加密修改
            if(StrUtil.isEmpty(admin.getPassword())){
                admin.setPassword(null);
            }else{
                admin.setPassword(DigestUtil.md5Hex(admin.getPassword()));
            }
        }
        this.getCacheService().delAdmin(id);
        return this.update(admin);
    }

    @Override
    public int updatePassword(UpdateAdminPasswordDto dto) {
        if(StrUtil.isEmpty(dto.getUsername())
                || StrUtil.isEmpty(dto.getOldPassword())
                || StrUtil.isEmpty(dto.getNewPassword())){
            return -1;
        }
        UmsAdminEntity queryAdminEntity = this.getAdminByUsername(dto.getUsername());
        if(Objects.isNull(queryAdminEntity)){
            return -2;
        }
        if(!dto.getOldPassword().equals(queryAdminEntity.getPassword())){
            return -3;
        }
        queryAdminEntity.setPassword(DigestUtil.md5Hex(dto.getNewPassword()));
        this.update(queryAdminEntity);
        getCacheService().delAdmin(queryAdminEntity.getId());
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id) {
        this.getCacheService().delAdmin(id);
        // 删除用户角色关系
        umsAdminRoleRelationDao.delByAdminId(id);
        // 删除用户
        int count = this.deleteById(id);
        this.getCacheService().delResourceList(id);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int allocateRole(Long adminId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();
        // 删除原来的角色
        Example example = new Example(UmsAdminRoleRelationEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("adminId", adminId);
        umsAdminRoleRelationDao.deleteByExample(example);

        // 绑定新角色
        if(!CollectionUtils.isEmpty(roleIds)){
            List<UmsAdminRoleRelationEntity> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                UmsAdminRoleRelationEntity roleRelation = new UmsAdminRoleRelationEntity();
                roleRelation.setPrimaryId();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
            umsAdminRoleRelationDao.insertBatch(list);
        }
        this.getCacheService().delResourceList(adminId);
        return count;
    }

    @Override
    public List<UmsResourceEntity> getResourceList(Long adminId) {
        return umsResourceDao.getResourceList(adminId);
    }
}
