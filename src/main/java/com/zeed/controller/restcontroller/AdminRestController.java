package com.zeed.controller.restcontroller;

import com.zeed.Utils.services.CardService;
import com.zeed.Utils.services.UserUtil;
import com.zeed.Utils.services.DeclineMessageService;
import com.zeed.models.DeclinedFollow;
import com.zeed.models.User;
import com.zeed.websocket.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Created by longbridge on 11/24/17.
 */
@RestController
public class AdminRestController {
    @Autowired
    UserUtil userUtil;
    @Autowired
    CardService cardService;
    @Autowired
    DeclineMessageService declineMessageService;

    @PostMapping("/declineCard")
    public Object declineCard(@RequestParam("itunescardDecline") MultipartFile uploadfile, @RequestParam("declineReason") String comment,
                              @RequestParam("cardId") String cardId, @RequestParam("username") String username,
                              HttpSession httpSession) {
        HashMap<String, String> response = new HashMap<>();
        try {
            System.out.println("Username " + username);
            System.out.println("cardId " + cardId);
            System.out.println("iteunsb h" + uploadfile);
            System.out.println("comment " + comment);
            User user = userUtil.returnUser(httpSession.getAttribute("admintoken").toString());
            if (user.equals(username)) {
                System.out.println("Equal");
            } else {
                System.out.println("Not equal");
            }
            String resp = cardService.declineCard(Long.valueOf(cardId), username, uploadfile.getBytes(), comment);
            if (resp.equals("Declination successfull")) {
                response.put("status", "success");
                response.put("message", "Itunes card successfully declined");
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.put("status", "failure");
        response.put("message", "Itunes card declination failed");
        return response;
    }

    @PostMapping("/verifyCard")
    public Object verifyCard(@RequestBody HashMap<String, String> data, HttpSession httpSession) {
        HashMap<String, String> response = new HashMap<>();
        try {
            System.out.println("Username " + data.get("username"));
            System.out.println("Card id " + data.get("cardId"));
            String cardId = data.get("cardId");
            String username = data.get("username");
            User user = userUtil.returnUser(httpSession.getAttribute("admintoken").toString());
            if (user.equals(username)) {
                System.out.println("Equal");
            } else {
                System.out.println("Not equal");
            }
            String resp = cardService.verifyCard(Long.valueOf(cardId), username);
            if (resp.equals("Verification successfull")) {
                response.put("status", "success");
                response.put("message", "Itunes card successfully verified");
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.put("status", "failure");
        response.put("message", "Itunes card verification failed");
        return response;
    }

    @PostMapping(value = "/adminPostMessage")
    public String postMessage(@RequestBody HashMap<String, String> data, HttpSession httpSession) {
        User user = userUtil.returnUser(httpSession.getAttribute("admintoken").toString());
        DeclinedFollow declinedFollow = null;
        if (user != null) {
            declinedFollow = declineMessageService.sendMessage(user, null, data.get("message"), Long.valueOf(data.get("cardId")));
        }
        return ((declinedFollow != null) ? "sent" : "failed");
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/zeed")
    @RequestMapping(method=RequestMethod.POST, value = "/topic/zeed")
    public Object chatMessage(ChatMessage chatMessage, @RequestBody HashMap<String, String> data) throws Exception {
        HttpSession httpSession = null;
        if(chatMessage.message==null && chatMessage.cardId==null && chatMessage.userRole==null) {
            try {
                RequestAttributes requestAttributes = RequestContextHolder
                        .currentRequestAttributes();
                ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
                HttpServletRequest request = attributes.getRequest();
                httpSession = request.getSession(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(chatMessage.message==null && httpSession!=null){
            User user = userUtil.returnUser(httpSession.getAttribute("admintoken").toString());
            DeclinedFollow declinedFollow = null;
            if (user != null) {
                declinedFollow = declineMessageService.sendMessage(user, null, data.get("message"), Long.valueOf(data.get("cardId")));
            }
            return ((declinedFollow != null) ? "sent" : "failed");
        }else{
            Thread.sleep(500); // simulated delay
            ChatMessage chatMessage1 = new ChatMessage(chatMessage.message,chatMessage.cardId,chatMessage.userRole);
            return chatMessage1;
        }

    }
}
