package com.zeed.Utils;

import com.zeed.models.User;
import com.zeed.repository.UserRepositoy;
import com.zeed.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by longbridge on 11/14/17.
 */
@Service
public class UserUtil {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    UserRepositoy userRepositoy;
    public User registerUser(User user) throws Exception {
        User user1 = userRepositoy.findByUsername(user.username);
        if(user1==null){
            user.password = Hash.createPassword(user.password);
            userRepositoy.save(user);
            user.message = "Registration successfull";
        }else{
            user.message = "Username already exists";
        }
        return user;
    }
    public User validateLogin(User user,Device device){
        User user1 = userRepositoy.findByUsername(user.username);
        if(user1!=null){
            System.out.println("I am here "+device);
            if(Hash.checkPassword(user.password,user1.password)){
                final UserDetails userDetails = userDetailsService.loadUserByUsername(user.username);
                System.out.println("userdetails is "+userDetails.toString());
//                Device currentDevice = DeviceUtils.getCurrentDevice(servletRequest);
                final String token = jwtTokenUtil.generateToken(userDetails, device);
                System.out.println("Token is "+token);
                System.out.println("I am here 1");
                    user1.message = "User found";
                user1.password = "";
                return user1;
            }else{
                System.out.println("I am here 22");
                user1.password = "";
                user1.message = "Invalid username/password";
                return user1;
            }
        }else{
            user.message = "Invalid username/password";
            return user;
        }
    }
}
