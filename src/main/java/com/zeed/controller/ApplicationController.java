package com.zeed.controller;

import com.zeed.Utils.UserUtil;
import com.zeed.security.JwtTokenUtil;
import com.zeed.security.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * Created by longbridge on 11/13/17.
 */
@Controller
public class ApplicationController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserUtil userUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping(method = RequestMethod.GET, value="/")
    public String home(HttpSession httpSession){
        System.out.println("Session is "+httpSession.getAttribute("token"));
        String token = "";
        if (httpSession.getAttribute("token")!=null) {
            token = httpSession.getAttribute("token").toString();
        }
        return userUtil.checkIfUserInSession(token);
    }

    @RequestMapping(method=RequestMethod.GET, value = "/dashboard")
    public String login(HttpSession httpSession){String token = "";
        if (httpSession.getAttribute("token")!=null) {
            token = httpSession.getAttribute("token").toString();
        }
        return userUtil.checkIfUserInSession(token);
    }
    @RequestMapping(method=RequestMethod.GET, value = "/usersignup")
    public String signup(){
        return "signup";
    }
    @RequestMapping(method=RequestMethod.GET, value = "/uploadCard")
    public String uploadCard(){
        return "uploadCard";
    }
}
