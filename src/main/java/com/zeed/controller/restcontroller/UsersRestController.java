package com.zeed.controller.restcontroller;

import com.zeed.Utils.UserUtil;
import com.zeed.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by longbridge on 11/13/17.
 */
@RestController
@RequestMapping("/user")
public class UsersRestController {
    @Autowired
    UserUtil userUtil;

    @PostMapping("/validateUser")
    public Object validateUser(@RequestBody User user, Device device, HttpSession httpSession){
        return userUtil.validateLogin(user, device,httpSession);
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
    @PostMapping("/uploadCard")
    public Object uploadCard(@RequestParam("itunescard") MultipartFile uploadfile,@RequestParam("desc") String desc){
        System.out.println("Obj is "+uploadfile.toString());
        System.out.println("Obj is "+desc);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("cardsUpload/card_"+(new Date()).getTime()+".jpg");
            int c=0;
            fileOutputStream.write(uploadfile.getBytes());
            fileOutputStream.flush();fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
