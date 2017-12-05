package com.zeed.controller.restcontroller;

import com.zeed.Utils.CardService;
import com.zeed.Utils.Hash;
import com.zeed.Utils.UserUtil;
import com.zeed.models.Cardgroup;
import com.zeed.models.Cards;
import com.zeed.models.Status;
import com.zeed.models.User;
import com.zeed.repository.CardgroupRepository;
import com.zeed.repository.CardsRepository;
import com.zeed.repository.UserRepositoy;
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
import java.util.ArrayList;
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
    @Autowired
    CardgroupRepository cardgroupRepository;
    @Autowired
    UserRepositoy userRepositoy;

    @PostMapping("/validateUser")
    public Object validateUser(@RequestBody User user, Device device, HttpSession httpSession){
        return userUtil.validateLogin(user, device,httpSession);
    }
    @PostMapping("/validateAdminUser")
    public Object validateAdminUser(@RequestBody User user, Device device, HttpSession httpSession){
        return userUtil.validateAdminUser(user, device,httpSession);
    }
    @PostMapping("/registerUser")
    public Object signUpUser(@RequestBody User userObject) throws Exception {
        User user = userUtil.registerUser(userObject);
        return user;
    }

    //Not needed at the moment.
    /*
    @PostMapping("/registerAdminUser")
    public Object signUpAdminUser(@RequestBody User userObject) throws Exception {
        User user = userUtil.registerAdminUser(userObject);
        return user;
    }
    */
    @PostMapping("/getUserDetails")
    public Object getUserDetails(){

        return null;
    }
    @PostMapping("/uploadCard")
    public Object uploadCard(@RequestParam("itunescard") MultipartFile[] uploadfile,@RequestParam("desc") String desc,
                             @RequestParam("amount") String amount,HttpSession httpSession){
        HashMap<String,String> response = new HashMap<>();
        String filePath = "cardsUpload/card_"+(new Date()).getTime()+".png";
        System.out.println("Number of records is "+uploadfile.length);
        User user = userUtil.returnUser(httpSession.getAttribute("token").toString());
        int c = 0;
        Date date = new Date();
        if(user!=null){
            Cardgroup cardgroup = new Cardgroup();
            ArrayList<Cards> cardsArrayList = new ArrayList<>();
            cardgroup.cardsList = cardsArrayList;
            cardgroupRepository.save(cardgroup);
            for(MultipartFile multipartFile:uploadfile){
                Cards cards = new Cards(desc, amount, filePath, user, date, null, Status.PENDING);
                cards = cardService.addCards(cards, multipartFile,cardgroup);
                if(cards.id!=null){
                    c++;
                    cardgroup.cardsList.add(cards);
                };
            }
            if (c==uploadfile.length) {
                response.put("status", "success");
                response.put("message", "Upload successful... Kindly wait for verification");
            } else {
                response.put("status", "failure");
                response.put("message", "Error in upload... Some files may not be uploaded. Kindly retry or contact us");
            }
            return response;
        }
        response.put("status","failure");
        return response;
    }
    @PostMapping("/updateProfileDetails")
    public Object updateProfileDetails(@RequestBody HashMap<String,String> data,HttpSession httpSession){
        HashMap<String,String> response = new HashMap<>();
        try {
            User user = userUtil.returnUser(httpSession.getAttribute("token").toString());
            if (user!=null) {
                user.gender = data.get("gender");
                user.accountNumber = data.get("accountNumber");
                user.lastName = data.get("lastName");
                user.firstName = data.get("firstName");
                user.bank = data.get("bank");
                user.email = data.get("email");
                userRepositoy.save(user);
                response.put("status","success");
                response.put("message","Profile Update successful.");
            }else{
                response.put("status","failure");
                response.put("message","Error in profile update... Kindly retry or contact us");
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.put("status","failure");
        response.put("message","Error in upload... Kindly retry or contact us");
        return response;
    }
    @PostMapping("/updatePasswordDetails")
    public Object updatePasswordDetails(@RequestBody HashMap<String,String> data,HttpSession httpSession){
        HashMap<String,String> response = new HashMap<>();
        try {
            User user = userUtil.returnUser(httpSession.getAttribute("token").toString());
            if (user!=null) {
                if(Hash.checkPassword(data.get("oldpassword"),user.password)){
                    user.password = Hash.createPassword(data.get("newpassword"));
                    userRepositoy.save(user);
                    response.put("status","success");
                    response.put("message","Password Update successful.");
                }else{
                    response.put("status","success");
                    response.put("message","Current password not correct.");
                }
            }else{
                response.put("status","failure");
                response.put("message","Error in password update... Kindly retry or contact us");
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.put("status","failure");
        response.put("message","Error in upload... Kindly retry or contact us");
        return response;
    }
}
