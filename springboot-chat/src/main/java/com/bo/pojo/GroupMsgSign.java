package com.bo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupMsgSign {
    private Long id;
    private String groupMsgId;
    private Long to;
    private Integer sign;
}
