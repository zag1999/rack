package com.jm.rack.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jm.rack.mapper.*;
import com.jm.rack.pojo.*;
import com.jm.rack.service.MaterialInfoService;
import com.jm.rack.untils.CommonUtils;
import com.jm.rack.untils.HslHelper;
import com.jm.rack.untils.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 料架通道
 */
@Service
public class MaterialInfoServiceImpl extends ServiceImpl<MaterialInfoMapper, MaterialInfo>
        implements MaterialInfoService {

    @Autowired
    MaterialInfoMapper materialInfoMapper;

    @Autowired
    RackInfoMapper rackInfoMapper;

    @Autowired
    MatReplenishMapper matReplenishMapper;

    @Autowired
    ESignMapper eSignMapper;

    @Autowired
    MatRackInfoMapper matRackInfoMapper;


    public Map selectAllBycode(Long rackId) {
        Map<String, Object> map = new HashMap<>();
        if (rackId == null) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        try {
            List<MaterialInfo> list = materialInfoMapper.searchAllByRackInfoId(rackId);
            for (MaterialInfo materialInfo : list
            ) {
                QueryWrapper<MatRackInfo> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("rackno", materialInfo.getMatno());
                materialInfo.setChild(matRackInfoMapper.selectList(queryWrapper));
            }
            map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
            map.put("list", list);
        } catch (Exception e) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", e.getMessage());
        }
        return map;
    }


    //扫码识别展示
    @Transactional()
    public Map scanRecognition(String code) {
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.getInstance().IsStringEmpty(code)) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        QueryWrapper<MatRackInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);
        MatRackInfo matRackInfo = matRackInfoMapper.selectOne(queryWrapper);
        if (matRackInfo == null) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", "料架找不到此物料");
            return map;
        }
        //通知料架亮灯
        QueryWrapper<ESign> eSignQueryWrapper = new QueryWrapper<>();
        eSignQueryWrapper.eq("stocksign",0).eq("matno",matRackInfo.getRackno());
        ESign eSign = eSignMapper.selectOne(eSignQueryWrapper);
        String pos = "";
        if ("10.57.31.97".equals(eSign.getPlcip())){
//            pos = "cplus1";
            pos = "Q";
        }else if ("10.57.31.133".equals(eSign.getPlcip())){
//            pos = "cplus2";
            pos = "Q1";
        }else if (eSign.getMatno().contains("S")){
//            pos = "cplus2";
            pos = "S";
        }else if ("10.57.31.99".equals(eSign.getPlcip())){
//            pos = "cplus3";
            pos = "Z2";
        }else if ("10.57.31.98".equals(eSign.getPlcip())){
            pos = "Z1";
        }
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("type", 4);
        jsonObject1.put("com", "com"+eSign.getComNo());
        jsonObject1.put("comchild",eSign.getComChildNo());
        jsonObject1.put("count", 1);//这个没任何含义
        jsonObject1.put("color","绿");
        WebSocketServer.sendSingleInfo(pos, jsonObject1);
        //替换成给plc写数据亮绿灯
        HslHelper.getInstance().writeShort(eSign.getComNo()+eSign.getOpenLight(), HslHelper.LV_DENG,pos);
        HslHelper.getInstance().writeShort(eSign.getComNo()+"DBW114.0",HslHelper.JIAO_HU_ZI_VALUE,pos);
        //当读到1的时候返回2
        Integer sign = HslHelper.getInstance().readInt(eSign.getComNo()+"DBW120.0", pos);
        if (sign != null && sign.equals(1)) HslHelper.getInstance().writeShort(eSign.getComNo()+"DBW120.0",HslHelper.QUAN_MIE_OVER_VALUE,pos);
        //pda 33  115  116
        QueryWrapper<MaterialInfo> materialInfoQueryWrapper = new QueryWrapper<>();
        materialInfoQueryWrapper.eq("matno", matRackInfo.getRackno());
        MaterialInfo materialInfo = materialInfoMapper.selectOne(materialInfoQueryWrapper);
        RackInfo rackInfo = rackInfoMapper.selectById(materialInfo.getRackInfoId());
        map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        map.put("matRackInfo", matRackInfo);
        map.put("rackinfo", rackInfo);
        return map;
    }

    //pda扫码入库
    @Transactional()
    public Map scanWarehouse(String code, Integer count, String staffname) {
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.getInstance().IsStringEmpty(code) || CommonUtils.getInstance().IsIntegerEmpty(count)) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        QueryWrapper<MatRackInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);
        MatRackInfo matRackInfo = matRackInfoMapper.selectOne(queryWrapper);
        if (matRackInfo == null) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", "料架找不到此物料");
            return map;
        }
        QueryWrapper<MaterialInfo> materialInfoQueryWrapper = new QueryWrapper<>();
        materialInfoQueryWrapper.eq("matno", matRackInfo.getRackno());
        MaterialInfo materialInfo = materialInfoMapper.selectOne(materialInfoQueryWrapper);
        RackInfo rackInfo = rackInfoMapper.selectById(materialInfo.getRackInfoId());
        String rname = "";
        if (rackInfo == null) {
            rname = "料架已删除";
        } else {
            rname = rackInfo.getInfoName();
        }
        int factCount = count + matRackInfo.getMatnum();
        QueryWrapper<ESign> eSignQueryWrapper = new QueryWrapper<>();
        eSignQueryWrapper.eq("stocksign", 0).eq("matno", matRackInfo.getRackno());
        ESign eSign = eSignMapper.selectOne(eSignQueryWrapper);
        String pos = "";
        if ("10.57.31.97".equals(eSign.getPlcip())){
//            pos = "cplus1";
            pos = "Q";
        }else if ("10.57.31.133".equals(eSign.getPlcip())){
//            pos = "cplus2";
            pos = "Q1";
        }else if (eSign.getMatno().contains("S")){
//            pos = "cplus2";
            pos = "S";
        }else if ("10.57.31.99".equals(eSign.getPlcip())){
//            pos = "cplus3";
            pos = "Z2";
        }else if ("10.57.31.98".equals(eSign.getPlcip())){
            pos = "Z1";
        }
        if (matRackInfo.getMatmax() >= factCount) {
            //可以修改
            try {
                MatReplenish matReplenish = new MatReplenish(rname, materialInfo.getMatno(), count, matRackInfo.getMatnum()
                        , factCount, matRackInfo.getCode(), matRackInfo.getName(), staffname, new Date());
                matReplenishMapper.insert(matReplenish);
                matRackInfoMapper.updateMatnumByCode(factCount, code);
                //关灯
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("type", 4);
                jsonObject1.put("com", "com"+eSign.getComNo());
                jsonObject1.put("comchild",eSign.getComChildNo());
                jsonObject1.put("count",factCount);
                jsonObject1.put("color","无");
                WebSocketServer.sendSingleInfo(pos, jsonObject1);
                //替换成告诉plc关灯
                HslHelper.getInstance().writeShort(eSign.getComNo()+eSign.getOpenLight(), (short) 1,pos);
                HslHelper.getInstance().writeShort(eSign.getComNo()+eSign.getComChildNo(),(short) factCount,pos);
                HslHelper.getInstance().writeShort(eSign.getComNo()+"DBW116.0",HslHelper.JIAO_HU_ZI_VALUE,pos);
                //读到为1的时候返回2
                Integer sign = HslHelper.getInstance().readInt(eSign.getComNo()+"DBW120.0", pos);
                if (sign != null && sign.equals(1)) HslHelper.getInstance().writeShort(eSign.getComNo()+"DBW120.0",HslHelper.QUAN_MIE_OVER_VALUE,pos);
                map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
            } catch (Exception e) {
                map.put("result", CommonUtils.getInstance().RESULT_fAIL);
                map.put("msg", e.getMessage());
            }
        } else {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", "此次添加已超过大存储量，请修改");
            return map;
        }
        return map;
    }


}




