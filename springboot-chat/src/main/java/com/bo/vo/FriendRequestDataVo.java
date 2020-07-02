package com.bo.vo;

import com.bo.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestDataVo {
   private Long id;
   private String content;
   private Long uid;
   private Long from;
   private Long from_group;
   private Integer type;
   private String remark;
   private String href;
   private Integer read;
   private String time;
   private Object user;
   private Integer flag;
}
