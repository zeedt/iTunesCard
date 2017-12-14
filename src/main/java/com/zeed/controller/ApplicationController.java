package com.zeed.controller;

import com.zeed.Utils.services.CardService;
import com.zeed.Utils.UserUtil;
import com.zeed.models.Cards;
import com.zeed.models.DeclinedFollow;
import com.zeed.models.Status;
import com.zeed.models.User;
import com.zeed.repository.CardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by longbridge on 11/13/17.
 */
@Controller
public class ApplicationController {

    @Autowired
    private CardService cardService;
    @Autowired
    private CardsRepository cardsRepository;
    @Autowired
    private UserUtil userUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping(method = RequestMethod.GET, value="/")
    public String home(HttpSession httpSession,Model model, @RequestParam(value = "page", required = false) String page){
        User user = null;
        String token = "";
        Long pageNo = Long.valueOf(0);
        if(page!=null && !page.equals("none") && Long.valueOf(page)>-1){
            pageNo = Long.valueOf(page);
        }
        if (httpSession.getAttribute("token")!=null) {
            token = httpSession.getAttribute("token").toString();
            user = userUtil.returnUser(httpSession.getAttribute("token").toString());

        }
        String resp = userUtil.checkIfUserInSession(token);
        if(user!=null){
            List<Cards> cardsList = new ArrayList<>();
            cardsList = cardsRepository.getUserPendingCard(new PageRequest(Math.toIntExact(pageNo),2),Status.PENDING,user);
            for(Cards cards:cardsList){cards.cardgroup=null;}
            model.addAttribute("user",user.username);
            model.addAttribute("role",user.role);
            if(cardsList.size()>0){
                model.addAttribute("usercard",cardsList);
                model.addAttribute("lastpicked",pageNo);
            }else{
                if(pageNo>0){pageNo--;}else{pageNo= Long.valueOf(0);}
                cardsList = cardsRepository.getUserPendingCard(new PageRequest(Math.toIntExact(pageNo),2),Status.PENDING,user);
                if(cardsList.size()>0){
                    model.addAttribute("usercard",cardsList);
                    model.addAttribute("lastpicked",pageNo);
                }else{
                    cardsList = cardsRepository.getUserPendingCard(new PageRequest(Math.toIntExact(0),2),Status.PENDING,user);
                    model.addAttribute("usercard",cardsList);
                    model.addAttribute("lastpicked",0);
                }
            }
        }
        return resp;
    }

    @RequestMapping(method=RequestMethod.GET, value = "/dashboard")
    public String login(HttpSession httpSession, Model model, @RequestParam(value = "page", required = false) String page){String token = "";
        User user = null;
        Long pageNo = Long.valueOf(0);
        if(page!=null && !page.equals("none") && Long.valueOf(page)>-1){
            pageNo = Long.valueOf(page);
        }
        if (httpSession.getAttribute("token")!=null) {
            token = httpSession.getAttribute("token").toString();
            user = userUtil.returnUser(token);
        }
        String resp =  userUtil.checkIfUserInSession(token);

        if(user!=null){
            List<Cards> cardsList = new ArrayList<>();
            cardsList = cardsRepository.getUserPendingCard(new PageRequest(Math.toIntExact(pageNo),2),Status.PENDING,user);
            for(Cards cards:cardsList){cards.cardgroup=null;}
            model.addAttribute("user",user.username);
            model.addAttribute("role",user.role);
            if(cardsList.size()>0){
                model.addAttribute("usercard",cardsList);
                model.addAttribute("lastpicked",pageNo);
            }else{
                if(pageNo>0){pageNo--;}else{pageNo= Long.valueOf(0);}
                cardsList = cardsRepository.getUserPendingCard(new PageRequest(Math.toIntExact(pageNo),2),Status.PENDING,user);
                if(cardsList.size()>0){
                    model.addAttribute("usercard",cardsList);
                    model.addAttribute("lastpicked",pageNo);
                }else{
                    cardsList = cardsRepository.getUserPendingCard(new PageRequest(Math.toIntExact(0),2),Status.PENDING,user);
                    model.addAttribute("usercard",cardsList);
                    model.addAttribute("lastpicked",0);
                }
            }

//            model.addAttribute("usercard",cardsList.stream().filter(cards -> cards.status==Status.PENDING).collect(Collectors.toList()));
        }
        return resp;
    }
    @RequestMapping(method=RequestMethod.GET, value = "/declined")
    public String declined(HttpSession httpSession,Model model,@RequestParam(value = "page", required = false) String page){
        String token = "";
        User user = null;
        Long pageNo = Long.valueOf(0);
        if(page!=null && !page.equals("none") && Long.valueOf(page)>-1){
            pageNo = Long.valueOf(page);
        }
        if (httpSession.getAttribute("token")!=null) {
            token = httpSession.getAttribute("token").toString();
            user = userUtil.returnUser(token);
        }
        String resp =  userUtil.checkIfUserInSession(token);
        if(user!=null){
            List<Cards> cardsList = new ArrayList<>();
            cardsList = cardsRepository.getUserPendingCard(new PageRequest(Math.toIntExact(pageNo),2),Status.VERIFICATION_DECLINED,user);
            for(Cards cards:cardsList){cards.cardgroup=null;}
            model.addAttribute("user",user.username);
            model.addAttribute("role",user.role);
            if(cardsList.size()>0){
                model.addAttribute("usercard",cardsList);
                model.addAttribute("lastpicked",pageNo);
            }else{
                if(pageNo>0){pageNo--;}else{pageNo= Long.valueOf(0);}
                cardsList = cardsRepository.getUserPendingCard(new PageRequest(Math.toIntExact(pageNo),2),Status.VERIFICATION_DECLINED,user);
                if(cardsList.size()>0){
                    model.addAttribute("usercard",cardsList);
                    model.addAttribute("lastpicked",pageNo);
                }else{
                    cardsList = cardsRepository.getUserPendingCard(new PageRequest(Math.toIntExact(0),2),Status.VERIFICATION_DECLINED,user);
                    model.addAttribute("usercard",cardsList);
                    model.addAttribute("lastpicked",0);
                }
            }
        }
        if(resp.equals("dashboard")){
            return "declined";
        }else{
            return resp;
        }
    }
    @RequestMapping(method=RequestMethod.GET, value = "/verified")
    public String verified(HttpSession httpSession,Model model,@RequestParam(value = "page", required = false) String page){
        String token = "";
        User user = null;
        Long pageNo = Long.valueOf(0);
        if(page!=null && !page.equals("none") && Long.valueOf(page)>-1){
            pageNo = Long.valueOf(page);
        }
        if (httpSession.getAttribute("token")!=null) {
            token = httpSession.getAttribute("token").toString();
            user = userUtil.returnUser(token);
        }
        String resp =  userUtil.checkIfUserInSession(token);
        if(user!=null){
            List<Cards> cardsList = new ArrayList<>();
            cardsList = cardsRepository.getUserPendingCard(new PageRequest(Math.toIntExact(pageNo),2),Status.VERIFIED,user);
            for(Cards cards:cardsList){cards.cardgroup=null;}
            model.addAttribute("user",user.username);
            model.addAttribute("role",user.role);
            if(cardsList.size()>0){
                model.addAttribute("usercard",cardsList);
                model.addAttribute("lastpicked",pageNo);
            }else{
                pageNo--;
                cardsList = cardsRepository.getUserPendingCard(new PageRequest(Math.toIntExact(pageNo),2),Status.VERIFIED,user);
                if(cardsList.size()>0){
                    model.addAttribute("usercard",cardsList);
                    model.addAttribute("lastpicked",pageNo);
                }else{
                    cardsList = cardsRepository.getUserPendingCard(new PageRequest(Math.toIntExact(0),2),Status.VERIFIED,user);
                    model.addAttribute("usercard",cardsList);
                    model.addAttribute("lastpicked",0);
                }
            }
        }
        if(resp.equals("dashboard")){
            return "verified";
        }else{
            return resp;
        }
    }
    @RequestMapping(method=RequestMethod.GET, value = "/usersignup")
    public String signup(){
        return "signup";
    }

    @RequestMapping(method=RequestMethod.GET, value = "/uploadCard")
    public String uploadCard(HttpSession httpSession){
        String token = "";
        if (httpSession.getAttribute("token")!=null) {
            token = httpSession.getAttribute("token").toString();
        }
        String resp = userUtil.checkIfUserInSession(token);
        if(resp.equals("dashboard")){
            return "uploadCard";
        }else{
            return resp;
        }
    }
    @RequestMapping(method=RequestMethod.GET, value = "/editProfile")
    public String editProfile(HttpSession httpSession,Model model){
        String token = "";
        if (httpSession.getAttribute("token")!=null) {
            token = httpSession.getAttribute("token").toString();
        }
        String resp = userUtil.checkIfUserInSession(token);
        if(resp.equals("dashboard")){
            User user = userUtil.returnUser(token);
            model.addAttribute("user",user);
            return "editProfile";
        }else{
            return resp;
        }
    }
    @RequestMapping(method=RequestMethod.GET, value = "/userlogout")
    public String logout(HttpSession httpSession){
        httpSession.setAttribute("token","");
            return "home";
        }
    @RequestMapping(method=RequestMethod.POST, value = "/fetchNotificationPage")
    public ModelAndView fetchNotificationPage(HttpSession httpSession, @RequestBody HashMap<String,String> data){
        String token = "";
        System.out.println("Card is "+data.get("cardId"));
        ModelAndView modelAndView = new ModelAndView();
        if (httpSession.getAttribute("token")!=null) {
            token = httpSession.getAttribute("token").toString();
        }
        String resp = userUtil.checkIfUserInSession(token);
        if(resp.equals("dashboard")){
            User user = userUtil.returnUser(token);
            List<DeclinedFollow> declinedFollows = new ArrayList<>();
            Cards cards = cardsRepository.findCardsById(Long.valueOf(data.get("cardId")));
            declinedFollows = (cards!=null) ? cards.declinedFollows : declinedFollows;
            Collections.sort(declinedFollows,(d1, d2)->d1.id.compareTo(d2.id));
            modelAndView.addObject("cardId",data.get("cardId"));
            modelAndView.addObject("messages",declinedFollows);
            modelAndView.addObject("role",user.role);
            modelAndView.addObject("lastCardMId",(declinedFollows.size()>0) ? declinedFollows.get(declinedFollows.size()-1) : 0);
            modelAndView.setViewName("messageBox");
        }else{
        }
            return modelAndView;
        }

        @RequestMapping(method = RequestMethod.GET,value = "/indexx")
        public String res(){

            return "index";
        }
}
