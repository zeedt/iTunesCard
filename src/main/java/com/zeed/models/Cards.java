package com.zeed.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by longbridge on 11/22/17.
 */
@Entity
public class Cards {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String description;
    public String amount;
    @Lob
    public String filePath;
    @ManyToOne
    public User user;
    @ManyToOne
    public Cardgroup cardgroup;
    public Date uploadedOn;
    public Date verifiedOn;
    @Lob
    public String comment;
    @Enumerated(EnumType.STRING)
    public Status status;
    @Lob
    public String declinedImage;
    @OneToMany(mappedBy = "cards",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<DeclinedFollow> declinedFollows;
    public Cards(String description, String amount, String filePath, User user,Date uploadedOn,Date verifiedOn,Status status) {
        this.description = description;
        this.amount = amount;
        this.filePath = filePath;
        this.user = user;
        this.uploadedOn = uploadedOn;
        this.status = status;
    }
    public Cards(){}
}
