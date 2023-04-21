package com.jm.rack.fl.plcConnect.core;

import HslCommunication.Profinet.Siemens.SiemensPLCS;
import com.jm.rack.service.ESignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
@Order(0)
public class PlcConnectCore {
    @Resource
    private ESignService eSignService;
    @Resource
    private PlcCatch plcCatch;

    /**
     * 从数据库查询plc的ip地址，并初始化连接对象
     */
    @PostConstruct
    private void initPlcConnector() {
        // 从数据库中获取所有的 PLC ip地址
        List<String> ipAddressList = eSignService.getAllPlcIpAddress();
        if (ipAddressList.size() > 0) {
            for (String ipAddress : ipAddressList) {
                plcCatch.setPlcConnector(SiemensPLCS.S1200, ipAddress);
            }
        } else {
            log.error("未从配置表e_sign中找到任何plc的ip地址");
        }
    }
}
