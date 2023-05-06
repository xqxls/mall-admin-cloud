package com.xqxls.cloud.domain;

import com.xqxls.cloud.response.UmsAdminRpcResponse;
import com.xqxls.cloud.response.UmsResourceRpcResponse;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 登录用户信息
 * Created by xqxls on 2020/6/19.
 */
@Data
public class SecurityUser implements UserDetails {

    /**
     * ID
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户状态
     */
    private Boolean enabled;
    /**
     * 权限数据
     */
    private Collection<SimpleGrantedAuthority> authorities;

    /**
     * 资源列表
     */
    private List<UmsResourceRpcResponse> resourceList;

    public SecurityUser() {

    }

    public SecurityUser(UmsAdminRpcResponse umsAdminRpcResponse, List<UmsResourceRpcResponse> resourceList) {
        this.setId(umsAdminRpcResponse.getId());
        this.setUsername(umsAdminRpcResponse.getUsername());
        this.setPassword(umsAdminRpcResponse.getPassword());
        this.setEnabled(umsAdminRpcResponse.getStatus() == 1);
        if (umsAdminRpcResponse.getId() != null) {
            authorities = new ArrayList<>();
            // 资源id+name作为凭证
            resourceList.forEach(resource -> authorities.add(new SimpleGrantedAuthority(resource.getUrl())));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}
