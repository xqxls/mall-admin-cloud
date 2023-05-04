package com.xqxls.cloud.holder;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import com.xqxls.cloud.domain.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取登录用户信息
 * Created by xqxls on 2020/6/17.
 */
@Component
public class LoginUserHolder {

    public UserDTO getCurrentUser(){
        //从Header中获取用户信息
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(servletRequestAttributes!=null){
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String userStr = request.getHeader("user");
            JSONObject userJsonObject = new JSONObject(userStr);
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(userJsonObject.getStr("user_name"));
            userDTO.setId(Convert.toLong(userJsonObject.get("id")));
            userDTO.setRoles(Convert.toList(String.class,userJsonObject.get("authorities")));
            return userDTO;
        }
        return null;
    }
}
