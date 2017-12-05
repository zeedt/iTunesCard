package com.zeed.Utils;

import com.zeed.models.Cardgroup;
import com.zeed.models.Cards;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.util.List;

/**
 * Created by longbridge on 11/22/17.
 */
public interface CardService {
   public Cards addCards(Cards cards, MultipartFile multipart, Cardgroup cardgroup);
   public Cards addMultipleCards(Cards cards, MultipartFile[] multipart);
   public String declineCard(Long id, String user, byte[] bytes,String comment);
   public String verifyCard(Long id, String user);
   public List<Cards> pendingCards();
   public void logSomething();
   public List<Cards> getUpdateCards(Long last);
}
