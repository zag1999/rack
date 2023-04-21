package com.jm.rack.fl.plcConnect.service;

import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import com.jm.rack.fl.plcConnect.core.PlcCatch;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PlcHandleService {
    @Resource
    private PlcCatch plcCatch;

    public boolean writeShort(String ip, String db, Short b) throws Exception {
        SiemensS7Net connector = plcCatch.getConnector(ip);
        if (connector == null) throw new Exception("未找到ip为" + ip + "的plc");
        OperateResult result = connector.Write(db, b);
        return result.IsSuccess;
    }

    public Integer readInt(String ip, String db) throws Exception {
        SiemensS7Net connector = plcCatch.getConnector(ip);
        if (connector == null) throw new Exception("未找到ip为" + ip + "的plc");
        OperateResultExOne<Short> result = connector.ReadInt16(db);
        if (result.IsSuccess) return result.Content.intValue();
        return null;
    }

    public boolean writeByte(String ip, String db, String address, byte b) throws Exception {
        SiemensS7Net connector = plcCatch.getConnector(ip);
        if (connector == null) throw new Exception("未找到ip为" + ip + "的plc");
        OperateResult result = connector.Write(db + address, b);
        return result.IsSuccess;
    }

    public byte readByte(String ip, String db, String address) throws Exception {
        SiemensS7Net connector = plcCatch.getConnector(ip);
        if (connector == null) throw new Exception("未找到ip为" + ip + "的plc");
        OperateResultExOne<Byte> result = connector.ReadByte(db + address);
        if (result.IsSuccess) return result.Content;
        return 0;
    }
}
