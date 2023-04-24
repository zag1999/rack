package com.jm.rack.untils;

import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Siemens.SiemensPLCS;
import HslCommunication.Profinet.Siemens.SiemensS7Net;

public class HslHelper {

    public static final  String lastH ="DB93.";   // 0  放行 1  byte
    public static final  Short LV_DENG = 992;   // 亮绿灯
    public static final  String JIAO_HU_ZI_101 = "DB101.DBW168.0";   // 101交互字地址
    public static final  String JIAO_HU_ZI_201 = "DB201.DBW168.0";   // 201交互字地址
    public static final  String JIAO_HU_ZI_301 = "DB301.DBW112.0";   // 301交互字地址
    public static final  String JIAO_HU_ZI_401 = "DB401.DBW112.0";   // 401交互字地址
    public static final  String JIAO_HU_ZI_501 = "DB501.DBW168.0";   // 1101交互字地址
    public static final  String JIAO_HU_ZI_601 = "DB601.DBW168.0";   // 1101交互字地址
    public static final  String JIAO_HU_ZI_701 = "DB701.DBW168.0";   // 1101交互字地址
    public static final  String JIAO_HU_ZI_801 = "DB801.DBW112.0";   // 1101交互字地址
    public static final  String JIAO_HU_ZI_901 = "DB901.DBW112.0";   // 1101交互字地址
    public static final  String JIAO_HU_ZI_1001 = "DB1001.DBW112.0";   // 1101交互字地址
    public static final  String JIAO_HU_ZI_1101 = "DB1101.DBW168.0";   // 1101交互字地址
    public static final  String JIAO_HU_ZI_1201 = "DB1201.DBW112.0";   // 1201交互字地址
    public static final  String QUAN_MIE_OVER_101 = "DB101.DBW170.0";   // 101取货完成已完成总信号
    public static final  String QUAN_MIE_OVER_201 = "DB201.DBW170.0";   // 201取货完成已完成总信号
    public static final  String QUAN_MIE_OVER_170 = "DBW170.0";   // 总完成信号170地址
    public static final  String FUWEI_101 = "DB101.DBW180.0";   // 复位
    public static final  String FUWEI_201 = "DB201.DBW180.0";   // 复位
    public static final  String FUWEI_301 = "DB301.DBW132.0";   // 复位
    public static final  String FUWEI_401 = "DB401.DBW132.0";   // 复位
    public static final  Short FUWEI_VALUE = 1;   // 复位
    public static final  Short JIAO_HU_ZI_VALUE = 1;   // 交互字数值
    public static final  Short QUAN_MIE_OVER_VALUE = 2;   // 交互字数值

    private static volatile HslHelper hslHelp = null;

    private SiemensS7Net WsiemensS7Net;
    private SiemensS7Net QsiemensS7Net;
    private SiemensS7Net Z1siemensS7Net;
    private SiemensS7Net Z2siemensS7Net;
    private SiemensS7Net SsiemensS7Net;
    private SiemensS7Net X1siemensS7Net;
    private SiemensS7Net X2siemensS7Net;
    private SiemensS7Net X3siemensS7Net;
    private SiemensS7Net Q1siemensS7Net;

    //前排取货显示数cm1,2 格式一样 前排库存cm3,4格式一样
    //中排1就是取货显示数,cm5,6,7
    //中排2就是库存,cm8,9,10
    //三排就是,cm10取货,11库存
    private HslHelper(){
        Q1siemensS7Net = new SiemensS7Net(SiemensPLCS.S1200, "10.57.31.133");
        //前排料架控制器
        QsiemensS7Net = new SiemensS7Net(SiemensPLCS.S1200,"10.57.31.97");

        //中排料架控制器1
        Z1siemensS7Net = new SiemensS7Net(SiemensPLCS.S1200,"10.57.31.98");
        //中排料架控制器2
        Z2siemensS7Net = new SiemensS7Net(SiemensPLCS.S1200,"10.57.31.99");
        //三排料架控制器
        SsiemensS7Net = new SiemensS7Net(SiemensPLCS.S1200,"10.57.31.131");
        //线体plc
        X1siemensS7Net = new SiemensS7Net(SiemensPLCS.S1200,"10.57.31.217");
        X2siemensS7Net = new SiemensS7Net(SiemensPLCS.S1200,"10.57.31.224");
        X3siemensS7Net = new SiemensS7Net(SiemensPLCS.S1200,"10.57.31.230");
    }

    public SiemensS7Net getQsiemensS7Net() {
        return QsiemensS7Net;
    }
    public SiemensS7Net getQ1siemensS7Net() {
        return Q1siemensS7Net;
    }
    public SiemensS7Net getZ1siemensS7Net() {
        return Z1siemensS7Net;
    }
    public SiemensS7Net getZ2siemensS7Net() {
        return Z2siemensS7Net;
    }
    public SiemensS7Net getSsiemensS7Net() {
        return SsiemensS7Net;
    }

    public SiemensS7Net getX1siemensS7Net() {
        return X1siemensS7Net;
    }
    public SiemensS7Net getX2siemensS7Net() {
        return X2siemensS7Net;
    }
    public SiemensS7Net getX3siemensS7Net() {
        return X3siemensS7Net;
    }
    public static HslHelper getInstance(){
        if( hslHelp == null ){
            synchronized (HslHelper.class){
                if( hslHelp == null ){
                    hslHelp = new HslHelper();
                }
            }
        }
        return hslHelp;
    }

    public static void main(String[] args) {
        //站号2   db代表每一个db块地址   address暂时为0  ,后面代表指令数字,每一个都有固定地址(数字或者亮灯)
//        boolean b1 = HslHelper.getInstance().writeShort("DB101.DBW6.0", "0", (short) 6);
//        boolean b2 = HslHelper.getInstance().writeShort("DB101.DBW8.0","0", (short) 992);
        //前排复位
//        boolean q1 = HslHelper.getInstance().writeShort(HslHelper.FUWEI_101,  HslHelper.FUWEI_VALUE,"Q");
//        boolean q2 = HslHelper.getInstance().writeShort(HslHelper.FUWEI_201,  HslHelper.FUWEI_VALUE,"Q");
//        //库存数复位
//        boolean z1 = HslHelper.getInstance().writeShort(HslHelper.FUWEI_301,  HslHelper.FUWEI_VALUE,"Q");
//        boolean z2 = HslHelper.getInstance().writeShort(HslHelper.FUWEI_401,  HslHelper.FUWEI_VALUE,"Q");
        //三排复位
//        boolean s1 = HslHelper.getInstance().writeShort("DB1101.DBW180.0",  HslHelper.FUWEI_VALUE,"S");
//        boolean s2 = HslHelper.getInstance().writeShort("DB1101.DBW180.0",  HslHelper.FUWEI_VALUE,"S");
//        boolean q1 = HslHelper.getInstance().writeShort("DB401.DBW112.0", (short) 1, "Q");
//        System.out.println(q1+"===");
        CommonUtils.readSpeech("你好,前排Q1-002需要补货,请尽快完成");

    }

    /**
     * 根据类型判断写向哪个地址的交互字
     * @param type
     * @return
     */
    public boolean writeJiaoHuZi(String type){
        boolean res = false;
        if ("Q".equals(type)){
            //前排
           HslHelper.getInstance().writeShort(HslHelper.JIAO_HU_ZI_101,HslHelper.JIAO_HU_ZI_VALUE,type);
           HslHelper.getInstance().writeShort(HslHelper.JIAO_HU_ZI_201,HslHelper.JIAO_HU_ZI_VALUE,type);
           res = true;
        }else if ("Z".equals(type)){
            //中排取货口面的交互字
            HslHelper.getInstance().writeShort(HslHelper.JIAO_HU_ZI_501,HslHelper.JIAO_HU_ZI_VALUE,type);
            HslHelper.getInstance().writeShort(HslHelper.JIAO_HU_ZI_601,HslHelper.JIAO_HU_ZI_VALUE,type);
            HslHelper.getInstance().writeShort(HslHelper.JIAO_HU_ZI_701,HslHelper.JIAO_HU_ZI_VALUE,type);
//            HslHelper.getInstance().writeShort(HslHelper.JIAO_HU_ZI_801,HslHelper.JIAO_HU_ZI_VALUE,type);
//            HslHelper.getInstance().writeShort(HslHelper.JIAO_HU_ZI_901,HslHelper.JIAO_HU_ZI_VALUE,type);
//            HslHelper.getInstance().writeShort(HslHelper.JIAO_HU_ZI_1001,HslHelper.JIAO_HU_ZI_VALUE,type);
            res = true;
        }else if ("S".equals(type)){
            //三排交互字,db1101
            HslHelper.getInstance().writeShort(HslHelper.JIAO_HU_ZI_1101,HslHelper.JIAO_HU_ZI_VALUE,type);
            //db1201
//            HslHelper.getInstance().writeShort(HslHelper.JIAO_HU_ZI_1201,HslHelper.JIAO_HU_ZI_VALUE,type);
            res = true;
        }
        return res;
    }

    /**
     * 向db块写数据
     */
    public boolean writeShort(String db,Short b,String type){
        boolean res = false;
        if ("Q".equals(type)){
            //前排
            OperateResult operateResult = getQsiemensS7Net().Write(db,b);
            if (operateResult.IsSuccess){
                res = true;
            }
        }else if ("Q1".equals(type)){
            //中排
            OperateResult operateResult = getQ1siemensS7Net().Write(db,b);
            if (operateResult.IsSuccess){
                res = true;
            }
        }else if ("Z".equals(type)){
            //中排
            OperateResult operateResult = getZ1siemensS7Net().Write(db,b);
            if (operateResult.IsSuccess){
                res = true;
            }
        }else if ("S".equals(type)){
            //三排
            OperateResult operateResult = getSsiemensS7Net().Write(db,b);
            if (operateResult.IsSuccess){
                res = true;
            }
        }else if ("Z1".equals(type)){
            //三排
            OperateResult operateResult = getZ1siemensS7Net().Write(db,b);
            if (operateResult.IsSuccess){
                res = true;
            }
        }else if ("Z2".equals(type)){
            //三排
            OperateResult operateResult = getZ2siemensS7Net().Write(db,b);
            if (operateResult.IsSuccess){
                res = true;
            }
        }
        return res;
    }

    /**
     * 读取状态信息
     */
    public Integer readInt(String db,String type){
        if ("Q".equals(type)){
            //前排
            OperateResultExOne<Short> resultState = getQsiemensS7Net().ReadInt16(db);
            if (resultState.IsSuccess){
                return resultState.Content.intValue();
            }
        }else if ("Q1".equals(type)){
            //
            OperateResultExOne<Short> resultState = getQ1siemensS7Net().ReadInt16(db);
            if (resultState.IsSuccess){
                return resultState.Content.intValue();
            }
        }else if ("Z1".equals(type)){
            //中排1
            OperateResultExOne<Short> resultState = getZ1siemensS7Net().ReadInt16(db);
            if (resultState.IsSuccess){
                return resultState.Content.intValue();
            }
        }else if ("Z2".equals(type)){
            //中排2
            OperateResultExOne<Short> resultState = getZ2siemensS7Net().ReadInt16(db);
            if (resultState.IsSuccess){
                return resultState.Content.intValue();
            }
        }else if ("S".equals(type)){
            //三排
            OperateResultExOne<Short> resultState = getSsiemensS7Net().ReadInt16(db);
            if (resultState.IsSuccess){
                return resultState.Content.intValue();
            }
        }
        return null;
    }


    public byte readByte(String db,String address,int type){
        byte res = 0;
        if (type == 0){
            OperateResultExOne<Byte> read =  getX1siemensS7Net().ReadByte(db+address);
            if (read.IsSuccess){
                res = read.Content;
            }
        }else if (type  == 1){
            OperateResultExOne<Byte> read =  getX2siemensS7Net().ReadByte(db+address);
            if (read.IsSuccess){
                res = read.Content;
            }
        }else if (type == 2){
            OperateResultExOne<Byte> read =  getX3siemensS7Net().ReadByte(db+address);
            if (read.IsSuccess){
                res = read.Content;
            }
        }
        return res;
    }



    public boolean writeByte(String db,String address,byte b,int type){
        boolean res = false;
        if (type == 0){
            OperateResult operateResult = getX1siemensS7Net().Write(db+address,b);
            if (operateResult.IsSuccess){
                res = true;
            }
        }else if (type ==1){
            OperateResult operateResult = getX2siemensS7Net().Write(db+address,b);
            if (operateResult.IsSuccess){
                res = true;
            }
        }else if (type == 2){
            OperateResult operateResult = getX3siemensS7Net().Write(db+address,b);
            if (operateResult.IsSuccess){
                res = true;
            }
        }
        return res;
    }
}
