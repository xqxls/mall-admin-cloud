package com.xqxls.cloud.authorization;

import com.xqxls.cloud.constant.AuthConstant;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * 鉴权管理器，用于判断是否有资源的访问权限
 * Created by xqxls on 2020/6/19.
 */
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        // 从上下文拿到请求路径
        URI uri = authorizationContext.getExchange().getRequest().getURI();
        String path = uri.getPath();
        // 去掉服务前缀，并加上权限前缀
        path = AuthConstant.AUTHORITY_PREFIX + path.substring(path.indexOf("/",path.indexOf("/")+1));
        // 自定匹配器
        MyPathMatcher matcher = new MyPathMatcher(path);
        // 认证通过且角色匹配的用户可访问当前路径
        return mono
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(matcher::match)
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

}
