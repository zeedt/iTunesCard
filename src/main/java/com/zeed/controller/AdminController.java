package com.zeed.controller;

import com.zeed.Utils.services.CardService;
import com.zeed.Utils.UserUtil;
import com.zeed.Utils.services.DeclineMessageService;
import com.zeed.models.Cards;
import com.zeed.models.DeclinedFollow;
import com.zeed.models.Status;
import com.zeed.models.User;
import com.zeed.repository.CardsRepository;
import com.zeed.repository.UserRepositoy;
import com.zeed.websocket.ChatMessage;
import com.zeed.websocket.Greeting;
import com.zeed.websocket.HelloMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    DeclineMessageService declineMessageService;

    @RequestMapping(method = RequestMethod.GET, value="/adminHome")
    public String home(HttpSession httpSession, Model model){
        Long pageNo = Long.valueOf(0);
        User user = null;
        String token = "";
        if (httpSession.getAttribute("admintoken")!=null) {
            token = httpSession.getAttribute("admintoken").toString();
            user = userUtil.returnAdminUser(httpSession.getAttribute("admintoken").toString());
        }
        String resp = userUtil.checkIfAdminUserInSession(token);
        if(user!=null){
            List<Cards> cardsList = cardService.CardsByStatusAndCount(Status.PENDING,pageNo);
            if(cardsList.size()>0){
                model.addAttribute("last",cardsRepository.getLastPendingCard());
                model.addAttribute("lastpicked",pageNo);
            }else{
                model.addAttribute("last",cardsRepository.getLastPendingCard());
                model.addAttribute("lastpicked",pageNo);
            }
//            Collections.reverse(cardsList);
            model.addAttribute("user",user.username);
            model.addAttribute("usercard",cardsList);
        }
        return resp;
    }
    public String adminSignup(HttpSession httpSession, Model model){
        return "adminReg";
    }

    @RequestMapping(method = RequestMethod.GET, value="/admindashboard")
    public String admindashboard(HttpSession httpSession, Model model,@RequestParam(value = "page",required = false) String page){
        String token = "";
        Long pageNo = Long.valueOf(0);
        if(page!=null && !page.equals("none") && Long.valueOf(page)>-1){
            pageNo = Long.valueOf(page);
        }
        User user = null;
        if (httpSession.getAttribute("admintoken")!=null) {
            token = httpSession.getAttribute("admintoken").toString();
            user = userUtil.returnAdminUser(token);
        }
        String resp =  userUtil.checkIfAdminUserInSession(token);
        if(user!=null){
            List<Cards> cardsList = new ArrayList<>();
            cardsList = cardService.CardsByStatusAndCount(Status.PENDING,pageNo);
            if(cardsList.size()>0){
                model.addAttribute("last",cardsRepository.getLastPendingCard());
                model.addAttribute("lastpicked",pageNo);
            }else{
                if(pageNo>0){pageNo--;}else{pageNo= Long.valueOf(0);}
                cardsList = cardService.CardsByStatusAndCount(Status.PENDING,pageNo);
                if(cardsList.size()>0){
                    model.addAttribute( "last",cardsRepository.getLastPendingCard());
                    model.addAttribute("lastpicked",pageNo);
                }else{
                    model.addAttribute("last",cardsRepository.getLastPendingCard());
                    model.addAttribute("lastpicked",pageNo);
                }

            }
//            Collections.reverse(cardsList);
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
        return "adminHome";
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
    @RequestMapping(method = RequestMethod.POST,value = "/updateDashB")
    public ModelAndView updateDashB(HttpSession httpSession,@RequestBody Map<String,String> data){
        User user = null;
        String token = "";
        if (httpSession.getAttribute("admintoken")!=null) {
            token = httpSession.getAttribute("admintoken").toString();
            user = userUtil.returnAdminUser(token);
        }
        try{
            if(user!=null){
                List<Cards> cardsList = cardService.getUpdateCards(Long.valueOf(data.get("last")));
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.setViewName("appendupdate");
                if(cardsList.size()>0) {
                    modelAndView.addObject("usercard", cardsList);
                    modelAndView.addObject("last", cardsList.get(cardsList.size()-1).id);
                    return modelAndView;
                }else{
                    return null;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value="/adminverified")
    public String adminverified(HttpSession httpSession, Model model,@RequestParam(value = "page",required = false) String page){
        String token = "";
        Long pageNo = Long.valueOf(0);
        if(page!=null && !page.equals("none") && Long.valueOf(page)>-1){
            pageNo = Long.valueOf(page);
        }
        User user = null;
        if (httpSession.getAttribute("admintoken")!=null) {
            token = httpSession.getAttribute("admintoken").toString();
            user = userUtil.returnAdminUser(token);
        }
        String resp =  userUtil.checkIfAdminUserInSession(token);
        if(user!=null){
            List<Cards> cardsList = new ArrayList<>();
            cardsList = cardService.CardsByStatusAndCount(Status.VERIFIED,pageNo);
            if(cardsList.size()>0){
                model.addAttribute("last",cardsRepository.getLastPendingCard());
                model.addAttribute("lastpicked",pageNo);
            }else{
                if(pageNo>0){pageNo--;}else{pageNo= Long.valueOf(0);}
                cardsList = cardService.CardsByStatusAndCount(Status.VERIFIED,pageNo);
                if(cardsList.size()>0){
                    model.addAttribute( "last",cardsRepository.getLastPendingCard());
                    model.addAttribute("lastpicked",pageNo);
                }else{
                    model.addAttribute("last",cardsRepository.getLastPendingCard());
                    model.addAttribute("lastpicked",pageNo);
                }

            }
//            Collections.reverse(cardsList);
            model.addAttribute("user",user.username);
            model.addAttribute("role",user.role);
            model.addAttribute("usercard",cardsList);
        }
        return (resp.equals("admindashboard") ? "adminVerified" : resp );
    }
    @RequestMapping(method = RequestMethod.GET, value="/adminDeclined")
    public String adminDeclined(HttpSession httpSession, Model model,@RequestParam(value = "page",required = false) String page){
        String token = "";
        Long pageNo = Long.valueOf(0);
        if(page!=null && !page.equals("none") && Long.valueOf(page)>-1){
            pageNo = Long.valueOf(page);
        }
        User user = null;
        if (httpSession.getAttribute("admintoken")!=null) {
            token = httpSession.getAttribute("admintoken").toString();
            user = userUtil.returnAdminUser(token);
        }
        String resp =  userUtil.checkIfAdminUserInSession(token);
        if(user!=null){
            List<Cards> cardsList = new ArrayList<>();
            cardsList = cardService.CardsByStatusAndCount(Status.VERIFICATION_DECLINED,pageNo);
            if(cardsList.size()>0){
                model.addAttribute("last",cardsRepository.getLastPendingCard());
                model.addAttribute("lastpicked",pageNo);
            }else{
                if(pageNo>0){pageNo--;}else{pageNo= Long.valueOf(0);}
                cardsList = cardService.CardsByStatusAndCount(Status.VERIFICATION_DECLINED,pageNo);
                if(cardsList.size()>0){
                    model.addAttribute( "last",cardsRepository.getLastPendingCard());
                    model.addAttribute("lastpicked",pageNo);
                }else{
                    model.addAttribute("last",cardsRepository.getLastPendingCard());
                    model.addAttribute("lastpicked",pageNo);
                }

            }
//            Collections.reverse(cardsList);
            model.addAttribute("user",user.username);
            model.addAttribute("role",user.role);
            model.addAttribute("usercard",cardsList);
        }
        return (resp.equals("admindashboard") ? "adminDeclined" : resp );
    }
    @RequestMapping(method=RequestMethod.POST, value = "/fetchAdminNotificationPage")
    public ModelAndView fetchNotificationPage(HttpSession httpSession, @RequestBody HashMap<String,String> data){
        String token = "";
        System.out.println("Card is "+data.get("cardId"));
        ModelAndView modelAndView = new ModelAndView();
        if (httpSession.getAttribute("admintoken")!=null) {
            token = httpSession.getAttribute("admintoken").toString();
        }
        String resp = userUtil.checkIfAdminUserInSession(token);
        User user = userUtil.returnAdminUser(token);
        if(resp.equals("admindashboard")){
            List<DeclinedFollow> declinedFollows = new ArrayList<>();
            Cards cards = cardsRepository.findCardsById(Long.valueOf(data.get("cardId")));
            declinedFollows = (cards!=null) ? cards.declinedFollows : declinedFollows;
            modelAndView.addObject("cardId",data.get("cardId"));
            Collections.sort(declinedFollows,(d1,d2)->d1.id.compareTo(d2.id));
            modelAndView.addObject("messages",declinedFollows);
            modelAndView.addObject("role",user.role);
            modelAndView.addObject("lastCardMId",(declinedFollows.size()>0) ? declinedFollows.get(declinedFollows.size()-1).id : 0);
            modelAndView.setViewName("adminMessageBox");
        }else{
        }
        return modelAndView;
    }

//    @MessageMapping("/sendMessage")
//    @SendTo("/topic/zeed")
//    @RequestMapping(method=RequestMethod.GET, value = "/topic/zeed")
//    public Object greeting(ChatMessage chatMessage,HttpSession httpSession) throws Exception {
//        String token = "";
//        if (httpSession.getAttribute("admintoken")!=null) {
//            token = httpSession.getAttribute("admintoken").toString();
//        }
//        System.out.println("Token is "+token);
//        Thread.sleep(500); // simulated delay
//        ChatMessage chatMessage1 = new ChatMessage(chatMessage.message,chatMessage.cardId,"ADMIN");
//        System.out.println("Chat message is "+chatMessage1);
//        return chatMessage1;
//    }



}
