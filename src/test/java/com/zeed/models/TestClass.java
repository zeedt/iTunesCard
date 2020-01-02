package com.zeed.models;

import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

public class TestClass {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TestClass.class.getName());

    public static void main(String[] args) {

        RoutingInformation routingInformation = new RoutingInformation();
        routingInformation.setPan("9889994344334334");
        routingInformation.setCardAcceptorId("CAR#ghjhhjh");

        TMRequest tmRequest = new TMRequest();
        tmRequest.setRequest("New Request");
        tmRequest.setPan("00000000000");
        tmRequest.setRoutingInformation(routingInformation);

        logger.trace("Received {} ", LogUtil.dump(tmRequest));

        System.out.println("yes");

    }


}
