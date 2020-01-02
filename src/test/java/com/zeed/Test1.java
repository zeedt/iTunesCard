package com.zeed;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Properties;

@RunWith(SpringRunner.class)
public class Test1 {

    @Test
    public void encrypt() {
        Gson gson = new Gson();

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("fbn@w2!89"));
        Properties data = gson.fromJson(String.format("{\"Status\":\"%s\",\"name\":\"%s\"}", "false", "ola"), Properties.class);
        boolean status = Boolean.parseBoolean(data.getProperty("Status"));

        System.out.println(bCryptPasswordEncoder.encode("fbn@w2!89"));

    }

    @Test
    public void test() {
        System.out.println(String.format("my name is %s", null));
        System.out.println("my name is " + null + " " + null);
        sqrt(-1.8f);
    }

    /**
     *  @pre f >= 0.0
     */
    public float sqrt(float f) {
        return f;
    }

}
