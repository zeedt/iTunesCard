package com.zeed.controller;

import com.zeed.Utils.CardService;
import com.zeed.Utils.UserUtil;
import com.zeed.models.Cards;
import com.zeed.models.Status;
import com.zeed.models.User;
import com.zeed.repository.CardsRepository;
import com.zeed.repository.UserRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by longbridge on 11/23/17.
 */
@Controller
public class AdminController {
    @Autowired
    private CardService cardService;
    @Autowired
    private UserUtil userUtil;
    @Autowired
    CardsRepository cardsRepository;
    @Autowired
    UserRepositoy userRepositoy;
    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping(method = RequestMethod.GET, value="/adminHome")
    public String home(HttpSession httpSession, Model model){
        System.out.println("Session is "+httpSession.getAttribute("admintoken"));
        User user = null;
        String token = "";
        if (httpSession.getAttribute("admintoken")!=null) {
            token = httpSession.getAttribute("admintoken").toString();
            user = userUtil.returnAdminUser(httpSession.getAttribute("admintoken").toString());
        }
        String resp = userUtil.checkIfAdminUserInSession(token);
        if(user!=null){
            List<Cards> cardsList = cardService.pendingCards();
            Collections.reverse(cardsList);
            model.addAttribute("user",user.username);
            model.addAttribute("usercard",cardsList);
        }
        return resp;
    }
    @RequestMapping(method = RequestMethod.GET, value="/adminsignup")
    public String adminSignup(HttpSession httpSession, Model model){
        return "adminReg";
    }

    @RequestMapping(method = RequestMethod.GET, value="/admindashboard")
    public String admindashboard(HttpSession httpSession, Model model){
        String token = "";
        User user = null;
        if (httpSession.getAttribute("admintoken")!=null) {
            token = httpSession.getAttribute("admintoken").toString();
            user = userUtil.returnAdminUser(token);
        }
        String resp =  userUtil.checkIfAdminUserInSession(token);
        if(user!=null){
            List<Cards> cardsList = cardService.pendingCards();
            Collections.reverse(cardsList);
            model.addAttribute("user",user.username);
            model.addAttribute("role",user.role);
            model.addAttribute("usercard",cardsList);
        }
        return resp;
    }
    @RequestMapping(method = RequestMethod.POST, value="/modalVerDecDet")
    public ModelAndView modalVerDecDet(HttpSession httpSession, Model model, @RequestBody Map<String,String> data){
        String token = "";
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adminVerifyDeclineModal");
        Cards cards = cardsRepository.findCardsById(Long.valueOf(data.get("cardid")));
        User user = null;
        if (httpSession.getAttribute("admintoken")!=null) {
            token = httpSession.getAttribute("admintoken").toString();
            user = userUtil.returnAdminUser(token);
        }
        String resp =  userUtil.checkIfAdminUserInSession(token);
        if(user!=null && cards!=null){
            if(cards.user.username.equals(data.get("user"))){
                    modelAndView.addObject("user",user.username);
                    modelAndView.addObject("role",user.role);
                    modelAndView.addObject("cards",cards);
                    modelAndView.addObject("cardstatus",cards.status.toString());
                    return modelAndView;
            }
        }
        return null;
    }
    @RequestMapping(method = RequestMethod.POST, value="/profileModal")
    public ModelAndView profileModal(HttpSession httpSession, Model model, @RequestBody Map<String,String> data){
        String token = "";
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adminUserProfile");
        Cards cards = cardsRepository.findCardsById(Long.valueOf(data.get("cardid")));
        User user = null;
        if (httpSession.getAttribute("admintoken")!=null) {
            token = httpSession.getAttribute("admintoken").toString();
            user = userUtil.returnAdminUser(token);
        }
        String resp =  userUtil.checkIfAdminUserInSession(token);
        if(user!=null && cards!=null){
            if(cards.user.username.equals(data.get("user"))){
                    modelAndView.addObject("user",user.username);
                    modelAndView.addObject("role",user.role);
                    modelAndView.addObject("carduser",cards.user);
                    modelAndView.addObject("cardstatus",cards.status.toString());
                    return modelAndView;
            }
        }
        return null;
    }
    @RequestMapping(method=RequestMethod.GET, value = "/adminlogout")
    public String logout(HttpSession httpSession){
        httpSession.setAttribute("admintoken","");
        return "home";
    }
    @RequestMapping(method=RequestMethod.GET, value = "/userEnquiry")
    public String userEnquiry(HttpSession httpSession){
        String token = "";
        User user = null;
        if (httpSession.getAttribute("admintoken")!=null) {
            token = httpSession.getAttribute("admintoken").toString();
            user = userUtil.returnAdminUser(token);
        }
        String resp =  userUtil.checkIfAdminUserInSession(token);
        if(resp.equals("admindashboard")){
            return "userEnquiryPage";
        }
        return resp;
    }

    @RequestMapping(method = RequestMethod.POST, value="/getuserEnquiry")
    public ModelAndView getuserEnquiry(HttpSession httpSession, Model model, @RequestBody Map<String,String> data){
        String token = "";
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adminUserProfile");
        User user1 = userRepositoy.findByUsername(data.get("username"));
        User user = null;
        if (httpSession.getAttribute("admintoken")!=null) {
            token = httpSession.getAttribute("admintoken").toString();
            user = userUtil.returnAdminUser(token);
        }
        String resp =  userUtil.checkIfAdminUserInSession(token);
        if(user1!=null && user!=null){
            modelAndView.addObject("carduser",user1);
            return modelAndView;
        }
        return null;
    }

}
