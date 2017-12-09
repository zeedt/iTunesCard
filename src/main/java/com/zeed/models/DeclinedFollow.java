package com.zeed.models;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by longbridge on 12/8/17.
 */
@Entity
public class DeclinedFollow {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    @ManyToOne
    public Cards cards;
    public Date dateSent;
    @Lob
    public String message;
    public Date dateReceived;
    @ManyToOne
    public User sentBy;
    @ManyToOne
    public User sentTo;

    public DeclinedFollow(Cards cards, User sentBy, User sentTo, String message) {
        this.cards = cards;
        this.dateSent = new Date();
        this.sentBy = sentBy;
        this.sentTo = sentTo;
        this.message = message;
    }
    public DeclinedFollow(){}
}
