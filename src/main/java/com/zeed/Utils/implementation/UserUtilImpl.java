package com.zeed.Utils.implementation;

import com.zeed.Utils.Hash;
import com.zeed.Utils.services.UserUtil;
import com.zeed.models.Role;
import com.zeed.models.User;
import com.zeed.repository.UserRepositoy;
import com.zeed.security.JwtTokenUtil;
import com.zeed.security.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * Created by longbridge on 12/15/17.
 */
@Service
public class UserUtilImpl implements UserUtil{
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    UserRepositoy userRepositoy;
    @Override
    public User registerUser(User user) throws Exception {
        User user1 = userRepositoy.findByUsername(user.username);
        if(user1==null){
            user.password = Hash.createPassword(user.password);
            user.role = Role.USER;
            userRepositoy.save(user);
            user.message = "Registration successfull";
        }else{
            user.message = "Username already exists";
        }
        return user;
    }
    @Override
    public User registerAdminUser(User user) throws Exception {
        User user1 = userRepositoy.findByUsername(user.username);
        if(user1==null){
            user.password = Hash.createPassword(user.password);
            user.role = Role.ADMIN;
            userRepositoy.save(user);
            user.message = "Registration successfull";
        }else{
            user.message = "Username already exists";
        }
        return user;
    }
    @Override
    public User validateLogin(User user, Device device, HttpSession httpSession){
        User user1 = userRepositoy.findByUsernameAndRole(user.username,Role.USER);
        if(user1!=null){
            user1.cards.forEach(card->{card.user=null;card.cardgroup=null;card.declinedFollows=null;});
            if(Hash.checkPassword(user.password,user1.password)){
                final UserDetails userDetails = userDetailsService.loadUserByUsername(user.username);
                final String token = jwtTokenUtil.generateToken(userDetails, device);
                user1.message = "User found";
                user1.password = "";
                httpSession.setAttribute("token",token);
                return user1;
            }else{
                user1.password = "";
                user1.message = "Invalid username/password";
                return user1;
            }
        }else{
            user.message = "Invalid username/password";
            return user;
        }
    }
    @Override
    public User validateAdminUser(User user, Device device, HttpSession httpSession){
        User user1 = userRepositoy.findByUsernameAndRole(user.username,Role.ADMIN);
        if(user1!=null){
            user1.cards.forEach(card->card.user=null);
            if(Hash.checkPassword(user.password,user1.password)){
                final UserDetails userDetails = userDetailsService.loadUserByUsername(user.username);
                final String token = jwtTokenUtil.generateToken(userDetails, device);
                user1.message = "User found";
                user1.password = "";
                httpSession.setAttribute("admintoken",token);
                return user1;
            }else{
                user1.password = "";
                user1.message = "Invalid username/password";
                return user1;
            }
        }else{
            user.message = "Invalid username/password";
            return user;
        }
    }
    @Override
    public String checkIfUserInSession(String token){
        String username = jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ",""));
        if (token==null || token.equals("") || username==null){
            return "home";
        }else if(username!=null){
            JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
            if(user!=null){
                User user1 = userRepositoy.findByUsername(user.getUsername());
                System.out.println("USer "+user1);
                if(user1!=null){
                    return "dashboard";
                }else{
                    return "home";
                }
            }else{
                return "home";
            }
        }
        return "home";
    }
    @Override
    public String checkIfAdminUserInSession(String token){
        String username = jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ",""));
        if (token==null || token.equals("") || username==null){
            return "adminHome";
        }else if(username!=null){
            JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
            if(user!=null){
                return "admindashboard";
            }else{
                return "adminHome";
            }
        }
        return "adminHome";
    }
    @Override
    public User returnUser(String token){
        String username = jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ",""));
        if (token==null || token.equals("") || username==null){
            return null;
        }else if(username!=null){
            JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
            if(user!=null){
                return userRepositoy.findByUsername(user.getUsername());
            }else{
                return null;
            }
        }
        return null;
    }
    @Override
    public User returnAdminUser(String token){
        String username = jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ",""));
        if (token==null || token.equals("") || username==null){
            return null;
        }else if(username!=null){
            JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
            if(user!=null){
                return userRepositoy.findByUsername(user.getUsername());
            }else{
                return null;
            }
        }
        return null;
    }
}
