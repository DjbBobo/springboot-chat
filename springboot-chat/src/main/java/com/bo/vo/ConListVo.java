package com.bo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConListVo {
    private Integer code;
    private String msg;
    private Long count;
    private Object data;
}
