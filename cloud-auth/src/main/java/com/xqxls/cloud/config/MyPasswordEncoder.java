package com.xqxls.cloud.config;

import cn.hutool.crypto.digest.DigestUtil;
import com.xqxls.cloud.constant.MessageConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author 胡卓
 * @create 2023-05-05 20:15
 * @Description
 */
@Slf4j
@Component
public class MyPasswordEncoder implements PasswordEncoder {


    /**
     * 编码规则
     *
     * @param rawPassword 原密码
     * @return
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return DigestUtil.md5Hex((String) rawPassword);
    }

    /**
     * 比对规则
     *
     * @param rawPassword     未编码的原密码
     * @param encodedPassword 编码后的密码
     * @return
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String rawPass = DigestUtil.md5Hex((String) rawPassword);
        boolean result = rawPass.equals(encodedPassword);
        log.info("密码匹配成功 :{}", result);
        if (!result) {
            throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
        }
        return true;
    }

    public static void main(String[] args) {
        String password = DigestUtil.md5Hex("123456");
        System.out.println(password);
    }

}

