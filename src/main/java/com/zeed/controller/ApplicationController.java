package com.zeed.controller;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by longbridge on 11/13/17.
 */
@Controller
public class ApplicationController {
    @RequestMapping(method = RequestMethod.GET, value="/")
    public String home(){
        return "home";
    }

    @RequestMapping(method=RequestMethod.GET, value = "/dashboard")
    public String login(){
        System.out.println("Passed in parameter is ");
        return "dashboard";
    }
    @RequestMapping(method=RequestMethod.GET, value = "/usersignup")
    public String signup(){
        System.out.println("Passed in parameter is ");
        return "signup";
    }
}
