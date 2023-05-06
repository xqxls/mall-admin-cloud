package com.xqxls.cloud.authorization;

import org.springframework.util.AntPathMatcher;


/**
 * @author 胡卓
 * @create 2023-05-06 12:31
 * @Description
 */
public class MyPathMatcher extends AntPathMatcher {

    private String path;

    MyPathMatcher(String path){
        this.path = path;
    }

    public boolean match(String pattern){
        return super.match(pattern,path);
    }
}
