package com.zeed.controller.restcontroller;

import com.zeed.Utils.CardService;
import com.zeed.Utils.UserUtil;
import com.zeed.models.Cards;
import com.zeed.models.Status;
import com.zeed.models.User;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
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
    @PostMapping("/declineCard")
    public Object declineCard(@RequestParam("itunescardDecline") MultipartFile uploadfile, @RequestParam("declineReason") String comment,
                              @RequestParam("cardId") String cardId, @RequestParam("username") String username,
                              HttpSession httpSession){
        HashMap<String,String> response = new HashMap<>();
        try {
            System.out.println("Username "+username);
            System.out.println("cardId "+cardId);
            System.out.println("iteunsb h"+uploadfile);
            System.out.println("comment "+comment);
            User user = userUtil.returnUser(httpSession.getAttribute("admintoken").toString());
            if(user.equals(username)){System.out.println("Equal");}else{
                System.out.println("Not equal");
            }
            String resp = cardService.declineCard(Long.valueOf(cardId),username,uploadfile.getBytes(),comment);
            if(resp.equals("Declination successfull")){
                response.put("status","success");
                response.put("message","Itunes card successfully declined");
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.put("status","failure");
        response.put("message","Itunes card declination failed");
        return response;
    }
    @PostMapping("/verifyCard")
    public Object verifyCard(@RequestBody HashMap<String,String> data,HttpSession httpSession){
        HashMap<String,String> response = new HashMap<>();
        try {
            System.out.println("Username "+data.get("username"));
            System.out.println("Card id "+data.get("cardId"));
            String cardId = data.get("cardId");
            String username = data.get("username");
            User user = userUtil.returnUser(httpSession.getAttribute("admintoken").toString());
            if(user.equals(username)){System.out.println("Equal");}else{
                System.out.println("Not equal");
            }
            String resp = cardService.verifyCard(Long.valueOf(cardId),username);
            if(resp.equals("Verification successfull")){
                response.put("status","success");
                response.put("message","Itunes card successfully verified");
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.put("status","failure");
        response.put("message","Itunes card verification failed");
        return response;
    }
}
