package com.zeed.models;

import lombok.Data;

import javax.persistence.*;
import javax.smartcardio.Card;
import java.util.List;

/**
 * Created by longbridge on 11/13/17.
 */
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    @Column(unique = true)
    public String username;
    public String firstName;
    public String lastName;
    public String email;
    public String gender;
    public String password;
    public String bank;
    public String accountNumber;
    @Enumerated(EnumType.STRING)
    public Role role;
    @Transient
    public String message;
    @Transient
    public String token;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "user")
    public List<Cards> cards;
}
