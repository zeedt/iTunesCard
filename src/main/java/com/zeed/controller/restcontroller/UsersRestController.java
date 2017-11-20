package com.zeed.controller.restcontroller;

import com.zeed.Utils.UserUtil;
import com.zeed.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by longbridge on 11/13/17.
 */
@RestController
@RequestMapping("/user")
public class UsersRestController {
    @Autowired
    UserUtil userUtil;

    @PostMapping("/validateUser")
    public Object validateUser(@RequestBody User user, Device device){
        return userUtil.validateLogin(user, device);
    }
    @PostMapping("/registerUser")
    public Object signUpUser(@RequestBody User userObject) throws Exception {
        User user = userUtil.registerUser(userObject);
        return user;
    }

    @PostMapping("/getUserDetails")
    public Object getUserDetails(){

        return null;
    }
}
