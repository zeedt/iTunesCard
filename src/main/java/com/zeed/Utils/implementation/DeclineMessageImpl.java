package com.zeed.Utils.implementation;

import com.zeed.Utils.services.DeclineMessageService;
import com.zeed.models.Cards;
import com.zeed.models.DeclinedFollow;
import com.zeed.models.User;
import com.zeed.repository.CardsRepository;
import com.zeed.repository.DeclineFollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by longbridge on 12/8/17.
 */
@Service
public class DeclineMessageImpl implements DeclineMessageService {
    @Autowired
    CardsRepository cardsRepository;
    @Autowired
    DeclineFollowRepository declineFollowRepository;
    @Override
    public DeclinedFollow sendMessage(User sentBy, User sentTo, String message, Long id) {
        Cards cards = cardsRepository.findCardsById(id);
        if(cards!=null){
            DeclinedFollow declinedFollow = new DeclinedFollow(cards,sentBy,sentTo,message);
            declineFollowRepository.save(declinedFollow);
            return declinedFollow;
        }else{
            return null;
        }
    }
}
