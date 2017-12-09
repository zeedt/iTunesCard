package com.zeed.Utils.services;

import com.zeed.models.DeclinedFollow;
import com.zeed.models.User;

/**
 * Created by longbridge on 12/8/17.
 */
public interface DeclineMessageService {
    public DeclinedFollow sendMessage(User sentBy, User sentTo, String message, Long id);
}
