package com.zeed.controller;

import com.zeed.Utils.CardService;
import com.zeed.Utils.UserUtil;
import com.zeed.models.User;
import com.zeed.security.JwtTokenUtil;
import com.zeed.security.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private CardService cardService;
    @Autowired
    private UserUtil userUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping(method = RequestMethod.GET, value="/")
    public String home(HttpSession httpSession,Model model){
        System.out.println("Session is "+httpSession.getAttribute("token"));
        cardService.logSomething();
        User user = null;
        String token = "";
        if (httpSession.getAttribute("token")!=null) {
            token = httpSession.getAttribute("token").toString();
            user = userUtil.returnUser(httpSession.getAttribute("token").toString());
        }
        String resp = userUtil.checkIfUserInSession(token);
        if(user!=null){
            model.addAttribute("user",user);
        }
        return resp;
    }

    @RequestMapping(method=RequestMethod.GET, value = "/dashboard")
    public String login(HttpSession httpSession,Model model){String token = "";
        User user = null;
        if (httpSession.getAttribute("token")!=null) {
            token = httpSession.getAttribute("token").toString();
            user = userUtil.returnUser(token);
        }
        String resp =  userUtil.checkIfUserInSession(token);
        if(user!=null){
            model.addAttribute("user",user);
        }
        return resp;
    }
    @RequestMapping(method=RequestMethod.GET, value = "/usersignup")
    public String signup(){
        return "signup";
    }

    @RequestMapping(method=RequestMethod.GET, value = "/uploadCard")
    public String uploadCard(HttpSession httpSession){
        String token = "";
        if (httpSession.getAttribute("token")!=null) {
            token = httpSession.getAttribute("token").toString();
        }
        String resp = userUtil.checkIfUserInSession(token);
        if(resp.equals("dashboard")){
            return "uploadCard";
        }else{
            return resp;
        }
    }
    @RequestMapping(method=RequestMethod.GET, value = "/userlogout")
    public String logout(HttpSession httpSession){
        httpSession.setAttribute("token","");
            return "home";
        }
}
