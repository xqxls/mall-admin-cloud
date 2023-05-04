package com.xqxls.cloud.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by xqxls on 2020/6/19.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private List<String> roles;
}
