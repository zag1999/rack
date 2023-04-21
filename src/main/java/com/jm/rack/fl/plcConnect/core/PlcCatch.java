package com.jm.rack.fl.plcConnect.core;

import HslCommunication.Profinet.Siemens.SiemensPLCS;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PlcCatch {
    private final Map<String, SiemensS7Net> plcConnectorMap = new HashMap<>();

    public void setPlcConnector(SiemensPLCS type, String ipAddress) {
        plcConnectorMap.put(ipAddress, new SiemensS7Net(type, ipAddress));
    }

    public SiemensS7Net getConnector(String ipAddress) {
        return plcConnectorMap.get(ipAddress);
    }
}
