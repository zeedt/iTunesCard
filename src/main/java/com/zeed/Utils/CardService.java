package com.zeed.Utils;

import com.zeed.models.Cards;

/**
 * Created by longbridge on 11/22/17.
 */
public interface CardService {
   public Cards addCards(Cards cards, byte[] bytes);
   public void logSomething();
}
