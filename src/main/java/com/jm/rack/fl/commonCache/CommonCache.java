package com.jm.rack.fl.commonCache;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommonCache {
    private final Map<Integer, Long> currPlan = new HashMap<>();

    public void setPlanId(Integer type, Long planId) {
        currPlan.put(type, planId);
    }

    public Long getPlanId(Integer type) {
        return currPlan.get(type);
    }
}
