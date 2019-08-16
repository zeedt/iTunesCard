package com.zeed.Utils.services;

import com.zeed.models.Role;
import com.zeed.models.User;
import com.zeed.repository.CardsRepository;
import com.zeed.repository.UserRepositoy;
import com.zeed.security.JwtTokenUtil;
import com.zeed.security.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;

/**
 * Created by longbridge on 11/14/17.
 */
public interface UserUtil {
    public User registerUser(User user) throws Exception;
    public User registerAdminUser(User user) throws Exception;
    public User validateLogin(User user, Device device, HttpSession httpSession);
    public User validateAdminUser(User user, Device device, HttpSession httpSession);
    public String checkIfUserInSession(String token);
    public String checkIfAdminUserInSession(String token);
    public User returnUser(String token);
    public User returnAdminUser(String token);
}
