package com.jm.rack.fl.plcConnect.core;

import HslCommunication.Profinet.Siemens.SiemensPLCS;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.service.S7PLC;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PlcCatch {
    private final Map<String, S7PLC> plcConnectorMap = new HashMap<>();

    public void setPlcConnector(EPlcType type, String ipAddress) {
        plcConnectorMap.put(ipAddress, new S7PLC(type, ipAddress));
    }

    public S7PLC getConnector(String ipAddress) {
        return plcConnectorMap.get(ipAddress);
    }
}
