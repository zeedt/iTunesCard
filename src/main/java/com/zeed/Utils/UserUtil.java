package com.zeed.Utils;

import com.zeed.models.User;
import com.zeed.repository.UserRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by longbridge on 11/14/17.
 */
@Service
public class UserUtil {
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
    public User validateLogin(User user){
        System.out.println("Username is "+user.username);
        User user1 = userRepositoy.findByUsername(user.username);
        if(user1!=null){
            System.out.println("I am here");
            if(Hash.checkPassword(user.password,user1.password)){
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
