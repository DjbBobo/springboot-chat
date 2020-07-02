package com.bo.controller;

import com.bo.pojo.Admin;
import com.bo.pojo.User;
import com.bo.result.R;
import com.bo.service.*;
import com.bo.utils.JsonUtils;
import com.bo.vo.AllGroupVo;
import com.bo.vo.ConListVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private GroupsService groupsService;
    @Autowired
    private UserGroupService userGroupService;
    @Autowired
    private FriendGroupRelService friendGroupRelService;


}
