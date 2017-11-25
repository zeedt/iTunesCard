package com.zeed.controller;

import com.zeed.Utils.CardService;
import com.zeed.Utils.UserUtil;
import com.zeed.models.Cards;
import com.zeed.models.Status;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        User user = null;
        String token = "";
        if (httpSession.getAttribute("token")!=null) {
            token = httpSession.getAttribute("token").toString();
            user = userUtil.returnUser(httpSession.getAttribute("token").toString());

        }
        String resp = userUtil.checkIfUserInSession(token);
        if(user!=null){
            List<Cards> cardsList = user.cards;
            Collections.reverse(cardsList);
            model.addAttribute("user",user.username);
            model.addAttribute("usercard",cardsList.stream().filter(cards -> cards.status==Status.PENDING).collect(Collectors.toList()));
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
            List<Cards> cardsList = user.cards;
            Collections.reverse(cardsList);
            model.addAttribute("user",user.username);
            model.addAttribute("role",user.role);
            model.addAttribute("usercard",cardsList.stream().filter(cards -> cards.status==Status.PENDING).collect(Collectors.toList()));
        }
        return resp;
    }
    @RequestMapping(method=RequestMethod.GET, value = "/declined")
    public String declined(HttpSession httpSession,Model model){
        String token = "";
        User user = null;
        if (httpSession.getAttribute("token")!=null) {
            token = httpSession.getAttribute("token").toString();
            user = userUtil.returnUser(token);
        }
        String resp =  userUtil.checkIfUserInSession(token);
        if(user!=null){
            List<Cards> cardsList = user.cards;
            Collections.reverse(cardsList);
            model.addAttribute("user",user.username);
            model.addAttribute("role",user.role);
            model.addAttribute("usercard",cardsList.stream().filter(c -> c.status==Status.VERIFICATION_DECLINED).collect(Collectors.toList()));
        }
        if(resp.equals("dashboard")){
            return "declined";
        }else{
            return resp;
        }
    }
    @RequestMapping(method=RequestMethod.GET, value = "/verified")
    public String verified(HttpSession httpSession,Model model){
        String token = "";
        User user = null;
        if (httpSession.getAttribute("token")!=null) {
            token = httpSession.getAttribute("token").toString();
            user = userUtil.returnUser(token);
        }
        String resp =  userUtil.checkIfUserInSession(token);
        if(user!=null){
            List<Cards> cardsList = user.cards;
            Collections.reverse(cardsList);
            model.addAttribute("user",user.username);
            model.addAttribute("role",user.role);
            model.addAttribute("usercard",cardsList.stream().filter(c -> c.status==Status.VERIFIED).collect(Collectors.toList()));
        }
        if(resp.equals("dashboard")){
            return "verified";
        }else{
            return resp;
        }
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
    @RequestMapping(method=RequestMethod.GET, value = "/editProfile")
    public String editProfile(HttpSession httpSession,Model model){
        String token = "";
        if (httpSession.getAttribute("token")!=null) {
            token = httpSession.getAttribute("token").toString();
        }
        String resp = userUtil.checkIfUserInSession(token);
        if(resp.equals("dashboard")){
            User user = userUtil.returnUser(token);
            model.addAttribute("user",user);
            return "editProfile";
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
