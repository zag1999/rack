package com.jm.rack.fl.plcConnect.service;

import com.jm.rack.fl.interactionSign.enums.InteractionOperator;
import com.jm.rack.fl.interactionSign.po.InteractionSign;
import com.jm.rack.fl.interactionSign.service.InteractionSignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class PlcOperateService {
    @Resource
    private InteractionSignService interactionSignService;

    public int allLightOn(String type) {
        int i = 0;
        interactionSignService.initInteractionSign();
        List<InteractionSign> list = interactionSignService.getListByType(type);
        for (InteractionSign item : list) {
            try {
                interactionSignService.sendInteractionSign(item.getLocation(), InteractionOperator.ALL_LIGHT_ON);
                i++;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return i;
    }

    public int allLightOff(String type) {
        int i = 0;
        interactionSignService.initInteractionSign();
        List<InteractionSign> list = interactionSignService.getListByType(type);
        for (InteractionSign item : list) {
            try {
                interactionSignService.sendInteractionSign(item.getLocation(), InteractionOperator.ALL_LIGHT_OFF);
                i++;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return i;
    }

    public int reset(String type) {
        int i = 0;
        interactionSignService.initInteractionSign();
        List<InteractionSign> list = interactionSignService.getListByType(type);
        for (InteractionSign item : list) {
            try {
                interactionSignService.sendInteractionSign(item.getLocation(), InteractionOperator.RESET);
                i++;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return i;
    }
}
