package com.zeed.Utils.implementation;

import com.zeed.Utils.services.CardService;
import com.zeed.models.Cardgroup;
import com.zeed.models.Cards;
import com.zeed.models.Status;
import com.zeed.repository.CardsRepository;
import com.zeed.repository.UserRepositoy;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by longbridge on 11/22/17.
 */
@Service
public class CardServiceImpl implements CardService {
    @Autowired
    CardsRepository cardsRepository;
    @Autowired
    UserRepositoy userRepositoy;
    @Override
    public Cards addCards(Cards cards, MultipartFile bytes, Cardgroup cardgroup) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("data:image/png;base64,");
            stringBuilder.append(StringUtils.newStringUtf8(Base64.encodeBase64(bytes.getBytes(),false)));
            cards.filePath = stringBuilder.toString();
            cards.cardgroup = cardgroup;
            cardsRepository.save(cards);
            return cards;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Cards addMultipleCards(Cards cards, MultipartFile[] multipart) {
        for (MultipartFile multipartFile:multipart){

        }
        return null;
    }

    @Override
    public String declineCard(Long id, String user, byte[] bytes, String comment) {
        Cards cards = cardsRepository.findCardsById(id);
        if(cards!=null){
            if(cards.user.username.equals(user)){
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("data:image/png;base64,");
                stringBuilder.append(StringUtils.newStringUtf8(Base64.encodeBase64(bytes,false)));
                cards.status = Status.VERIFICATION_DECLINED;
                cards.comment = comment;
                cards.verifiedOn = new Date();
                cards.declinedImage = stringBuilder.toString();
                cardsRepository.save(cards);
                return "Declination successfull";
            }
        }
        return "Declination failed";
    }

    @Override
    public String verifyCard(Long id, String user) {
        Cards cards = cardsRepository.findCardsById(id);
        if(cards!=null){
            if(cards.user.username.equals(user)){
                cards.status = Status.VERIFIED;
                cards.verifiedOn = new Date();
                cardsRepository.save(cards);
                return "Verification successfull";
            }
        }
        return "Verification failed";
    }

    @Override
    public List<Cards> pendingCards() {
        return cardsRepository.findCardsByStatus(Status.PENDING);
    }

    @Override
    public List<Cards> CardsByStatusAndCount(Status status, Long id) {
        List<Cards> cardsList = new ArrayList<>();
        cardsList = cardsRepository.findCardsByStatusAndCount(new PageRequest(Math.toIntExact(id),2), status);
        return cardsList;
    }

    @Override
    public void logSomething() {
        System.out.println("I am the log something service");

    }

    @Override
    public List<Cards> getUpdateCards(Long last) {
        List<Cards> cardsList = cardsRepository.findCardsByIdGreaterThanAndStatus(last,Status.PENDING);
        return cardsList;
    }
}
