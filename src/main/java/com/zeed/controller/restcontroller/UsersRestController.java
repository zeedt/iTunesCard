package com.zeed.controller.restcontroller;

import com.zeed.Utils.CardService;
import com.zeed.Utils.UserUtil;
import com.zeed.models.Cards;
import com.zeed.models.Status;
import com.zeed.models.User;
import com.zeed.repository.CardsRepository;
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
import java.util.HashMap;

/**
 * Created by longbridge on 11/13/17.
 */
@RestController
@RequestMapping("/user")
public class UsersRestController {

    @Autowired
    UserUtil userUtil;
    @Autowired
    private CardService cardService;

    @Autowired
    CardsRepository cardsRepository;

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
    public Object uploadCard(@RequestParam("itunescard") MultipartFile uploadfile,@RequestParam("desc") String desc,
                             @RequestParam("amount") String amount,HttpSession httpSession){
        HashMap<String,String> response = new HashMap<>();
        try {
            String filePath = "cardsUpload/card_"+(new Date()).getTime()+".png";
            User user = userUtil.returnUser(httpSession.getAttribute("token").toString());
            Cards cards = new Cards(desc,amount,filePath,user,new Date(),null, Status.PENDING);
            cards = cardService.addCards(cards,uploadfile.getBytes());
            if (cards != null) {
                response.put("status","success");
                response.put("message","Upload successful... Kindly wait for verification");
            }else{
                response.put("status","failure");
                response.put("message","Error in upload... Kindly retry or contact us");
            }
            return response;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.put("status","failure");
        return response;
    }
}
