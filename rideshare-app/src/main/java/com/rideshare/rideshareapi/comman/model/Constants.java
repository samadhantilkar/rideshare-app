package com.rideshare.rideshareapi.comman.model;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Constants {
    private static final int TEN_MINUTES=60*10*1000;
    private final Map<String,String> constants=new HashMap<>();

    public String getSchedulingTopicName(){
        return constants.getOrDefault("schedulingTopicName","schedulingServiceTopic");
    }

    public String getDriverMatchingTopicName() {
        return constants.getOrDefault("driverMatchingTopicName","driverMatchingTopic");
    }
}
