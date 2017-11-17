package com.zeed.controller.restcontroller;

import com.zeed.Utils.UserUtil;
import com.zeed.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by longbridge on 11/13/17.
 */
@RestController
@RequestMapping("/user")
public class UsersRestController {
    @Autowired
    UserUtil userUtil;
    @PostMapping("/validateUser")
    public Object validateUser(@RequestBody User user){
        return userUtil.validateLogin(user);
    }
    @PostMapping("/registerUser")
    public Object signUpUser(@RequestBody User userObject) throws Exception {
        User user = userUtil.registerUser(userObject);
        return user;
    }
}
