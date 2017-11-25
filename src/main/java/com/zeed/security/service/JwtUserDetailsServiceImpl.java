package com.zeed.security.service;

import com.zeed.security.JwtUserFactory;
import com.zeed.security.repository.Authority;
import com.zeed.security.repository.User;
import com.zeed.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by stephan on 20.03.16.
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
         com.zeed.models.User user1 =  userRepository.findByUsername(username);
         ArrayList<Authority> arrayList = new ArrayList<>();
         if(user1!=null){
             user = new User(user1.username,user1.password,true,null,arrayList);
         }
        if (user == null) {
             return null;
//            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtUserFactory.create(user);
        }
    }


}
