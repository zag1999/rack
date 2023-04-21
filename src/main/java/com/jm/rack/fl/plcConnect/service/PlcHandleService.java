package com.jm.rack.fl.plcConnect.service;

import HslCommunication.Core.Types.OperateResultExOne;
import com.github.xingshuangs.iot.protocol.s7.service.S7PLC;
import com.jm.rack.fl.plcConnect.core.PlcCatch;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PlcHandleService {
    @Resource
    private PlcCatch plcCatch;

    public void writeInt(String ip, String db, Short b) throws Exception {
        db = db.replaceAll("[^0-9.]", "");
        S7PLC connector = plcCatch.getConnector(ip);
        if (connector == null) throw new Exception("未找到ip为" + ip + "的plc");
        connector.writeInt16(db, b);
    }

    public Integer readInt(String ip, String db) throws Exception {
        db = db.replaceAll("[^0-9.]", "");
        S7PLC connector = plcCatch.getConnector(ip);
        if (connector == null) throw new Exception("未找到ip为" + ip + "的plc");
        short res = connector.readInt16(db);
        return (int) res;
    }
}
