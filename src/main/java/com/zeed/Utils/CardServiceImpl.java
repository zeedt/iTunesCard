package com.zeed.Utils;

import com.zeed.models.Cards;
import com.zeed.models.Status;
import com.zeed.repository.CardsRepository;
import com.zeed.repository.UserRepositoy;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.codehaus.groovy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by longbridge on 11/22/17.
 */
@Service
public class CardServiceImpl implements CardService{
    @Autowired
    CardsRepository cardsRepository;
    @Autowired
    UserRepositoy userRepositoy;
    @Override
    public Cards addCards(Cards cards,byte[] bytes) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("data:image/png;base64,");
            stringBuilder.append(StringUtils.newStringUtf8(Base64.encodeBase64(bytes,false)));
            cards.filePath = stringBuilder.toString();
            cardsRepository.save(cards);
            cards.user.cards.add(cards);
            userRepositoy.save(cards.user);
            return cards;
        } catch (Exception e) {
            e.printStackTrace();
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
    public void logSomething() {
        System.out.println("I am the log something service");

    }

    @Override
    public List<Cards> getUpdateCards(Long last) {
        List<Cards> cardsList = cardsRepository.findCardsByIdGreaterThan(last);
        return cardsList;
    }
}
