package com.bo.service;

import com.bo.mapper.AdminMapper;
import com.bo.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    public Admin selectAdmin(Admin admin) {
        return adminMapper.selectAdmin(admin);
    }
}
