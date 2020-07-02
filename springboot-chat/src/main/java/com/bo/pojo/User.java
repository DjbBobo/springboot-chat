package com.bo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private String id;
    private String account;
    private String password;
    private String username;
    private String sign;
    private String avatar;
    private String status;
    private Long fakestatus;
}
