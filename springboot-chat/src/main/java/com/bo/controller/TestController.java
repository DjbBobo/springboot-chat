package com.bo.controller;

import com.bo.config.QQConfigInfo;
import com.bo.pojo.QQUser;
import com.bo.pojo.User;
import com.bo.redis.UserKey;
import com.bo.result.R;
import com.bo.service.QQUserService;
import com.bo.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@RestController
public class TestController {

    @Autowired
    private QQUserService qqUserService;

    /**
     * 跳转到qq授权网页
     * @param session
     * @return
     */
    @GetMapping("/qq_login")
    public void qq(HttpSession session, HttpServletResponse response) throws IOException {
        /**
         * 防止请求受到攻击
         */
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        session.setAttribute("state", uuid);

        String url = QQConfigInfo.GETQQPAGE+"?response_type=code" +
                "&client_id=" + QQConfigInfo.APPID +
                "&redirect_uri=" + URLEncodeUtil.getURLEncoderString(QQConfigInfo.BACKURL) +
                "&state=" + uuid;
        response.sendRedirect(url);
    }

    /**
     * qq回调地址
     * @param request
     * @return
     */
    @GetMapping("/QQLogin")
    public void callback(HttpServletRequest request,HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String uuid = (String) session.getAttribute("state");
        if (uuid != null) {
            if (!uuid.equals(state)) {
                System.out.println("TOKEN错误, 防止CSRF攻击, 业务异常处理......");
                return;
            }
        }
        User user = null;
        try {
            user =  qqUserService.saveQQAccount(code);
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("http://localhost:8081/BoChat/main.html?id="+user.getAccount());
    }
}
