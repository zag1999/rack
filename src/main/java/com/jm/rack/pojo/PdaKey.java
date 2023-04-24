//package com.jm.rack.pojo;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.jm.rack.mapper.ESignMapper;
//import com.jm.rack.mapper.MatRackInfoMapper;
//import com.jm.rack.untils.CommonUtils;
//import com.jm.rack.untils.HslHelper;
//import lombok.Data;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import java.io.Serializable;
//import java.util.List;
//
//@Component
//@Data
//public class PdaKey extends Thread implements Serializable{
//
//    @Autowired
//    private MatRackInfoMapper matRackInfoMapper;
//    @Autowired
//    private ESignMapper eSignMapper;
//
//    @PostConstruct
//    public void init() {
//        System.out.println("系统启动中。。。加载codeMap");
//        //查询所有柜内物料
//        List<MatRackInfo> matRackInfos = matRackInfoMapper.selectAllMatNum();
//        System.out.println(matRackInfos.size());
//        CommonUtils.getInstance().getExecutorService().execute(new Runnable() {
//            @Override
//            public void run() {
//                for (MatRackInfo matRackInfo : matRackInfos) {
//                    //查物料plc地址并写入
//                    QueryWrapper<ESign> eSignQueryWrapper = new QueryWrapper<>();
//                    eSignQueryWrapper.eq("stocksign", 0).eq("matno", matRackInfo.getRackno());
//                    ESign eSign = eSignMapper.selectOne(eSignQueryWrapper);
//                    String pos = "";
//                    if (eSign.getMatno().contains("Q")) {
//                        pos = "Q";
//                    } else if (eSign.getMatno().contains("S")) {
//                        pos = "S";
//                    } else if ("10.57.31.99".equals(eSign.getPlcip())) {
//                        pos = "Z2";
//                    } else if ("10.57.31.98".equals(eSign.getPlcip())) {
//                        pos = "Z1";
//                    }
//                    HslHelper.getInstance().writeShort(eSign.getComNo() + eSign.getComChildNo(), (short) matRackInfo.getMatnum().intValue(), pos);
//                }
//
//            }
//        });
//    }
//    @PreDestroy
//    public void destroy(){
//        System.out.println("系统运行结束");
//    }
//}
