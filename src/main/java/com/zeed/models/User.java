package com.zeed.models;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by longbridge on 11/13/17.
 */
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String username;
    public String firstName;
    public String lastName;
    public String gnder;
    public String password;
    public String bank;
    public String acctountNumber;
    @Transient
    public String message;
    @Transient
    public String token;
}
