package com.zeed.Utils;

import com.zeed.models.Cards;
import com.zeed.repository.CardsRepository;
import com.zeed.repository.UserRepositoy;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.codehaus.groovy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

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
//            FileOutputStream fileOutputStream = new FileOutputStream(cards.filePath);
//            fileOutputStream.write(bytes);
//            fileOutputStream.flush();
//            fileOutputStream.close();
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
    public void logSomething() {
        System.out.println("I am the log something service");

    }
}
