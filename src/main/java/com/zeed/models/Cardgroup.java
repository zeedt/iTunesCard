package com.zeed.models;

import javax.persistence.*;
import java.util.List;

/**
 * Created by longbridge on 12/5/17.
 */
@Entity
public class Cardgroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    @OneToMany (mappedBy = "cardgroup",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    public List<Cards> cardsList;
}
