package com.jm.rack.untils;


import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Siemens.SiemensPLCS;
import HslCommunication.Profinet.Siemens.SiemensS7Net;

public class HslHelper81 {
    private static volatile HslHelper81 hslHelp = null;

    private SiemensS7Net siemensS7Net;

    private HslHelper81(){
        siemensS7Net = new SiemensS7Net(SiemensPLCS.S1500,"10.57.31.230");
    }

    public void updateS7Net(String ip){
        siemensS7Net  = new SiemensS7Net(SiemensPLCS.S1500,ip);
    }

    public SiemensS7Net getSiemensS7Net() {
        return siemensS7Net;
    }

    public static HslHelper81 getInstance(){
        if( hslHelp == null ){
            synchronized (HslHelper81.class){
                if( hslHelp == null ){
                    hslHelp = new HslHelper81();
                }
            }
        }
        return hslHelp;
    }

    public boolean readBool(String db,String address){
        boolean res = false;
        OperateResultExOne<Boolean> read = siemensS7Net.ReadBool(db+address);
        if (read.IsSuccess){
            res = read.Content;
        }
        return res;
    }

    /*读AGV用16*/
    public int readInt(String db,String address){
        int res = -1;
        OperateResultExOne<Short> read = siemensS7Net.ReadInt16(db+address);
        if (read.IsSuccess){
            res = read.Content;
        }
        return res;
    }

    public byte readByte(String db,String address){
        byte res = 0;
        OperateResultExOne<Byte> read =  siemensS7Net.ReadByte(db+address);
        if (read.IsSuccess){
            res = read.Content;
        }
        return res;
    }

    public boolean writeBool(String db,String address,boolean b){
        boolean res = false;
        OperateResult operateResult = siemensS7Net.Write(db+address,b);
        if (operateResult.IsSuccess){
            res = true;
        }
        return res;
    }

    public boolean writeByte(String db,String address,byte b){
        boolean res = false;
        OperateResult operateResult = siemensS7Net.Write(db+address,b);
        if (operateResult.IsSuccess){
            res = true;
        }
        return res;
    }
    public boolean writeInt(String db,String address,Short b){
        boolean res = false;
        OperateResult operateResult = siemensS7Net.Write(db+address,b);
//        System.out.println("errorcode:"+operateResult.ErrorCode);
//        System.out.println("msg:"+operateResult.ToMessageShowString());
        if (operateResult.IsSuccess){
            res = true;
        }
        return res;
    }


}
