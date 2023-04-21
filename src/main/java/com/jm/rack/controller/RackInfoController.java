package com.jm.rack.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jm.rack.mapper.ESignMapper;
import com.jm.rack.mapper.MaterialInfoMapper;
import com.jm.rack.mapper.RackInfoMapper;
import com.jm.rack.pojo.ESign;
import com.jm.rack.pojo.MaterialInfo;
import com.jm.rack.pojo.RackInfo;
import com.jm.rack.service.impl.MaterialInfoServiceImpl;
import com.jm.rack.service.impl.RackInfoServiceImpl;
import com.jm.rack.untils.CommonUtils;
import com.jm.rack.untils.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Transactional
@RequestMapping("rackinfo/")
public class RackInfoController {

    @Autowired
    RackInfoServiceImpl rackInfoService;

    @Autowired
    RackInfoMapper rackInfoMapper;
    @Autowired
    MaterialInfoServiceImpl materialInfoService;

    @Autowired
    MaterialInfoMapper materialInfoMapper;

    @Autowired
    ESignMapper eSignMapper;


    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    @RequestMapping(value = "insert")
    @ResponseBody
    public Map insert(RackInfo rackInfo) {
        return rackInfoService.insert(rackInfo);
    }

    @RequestMapping(value = "update")
    @ResponseBody
    public Map update(RackInfo rackInfo) {
        return rackInfoService.update(rackInfo);
    }

    @RequestMapping(value = "select")
    @ResponseBody
    public Map select() {
        return rackInfoService.select();
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public Map delete(Long id) {
        return rackInfoService.delete(id);
    }

    @RequestMapping(value = "seletByParent")
    @ResponseBody
    public Map seletByParent() {
        return rackInfoService.seletByParent();
    }

    @RequestMapping(value = "seletAllMat")
    @ResponseBody
    public Map seletAllMat() {
        return rackInfoService.seletAllMat();
    }


    @RequestMapping(value = "selectAllMatRack")
    @ResponseBody
    public Map selectAllMatRack() {
        return rackInfoService.selectAllMatRack();
    }



    @RequestMapping(value = "getDate")
    @ResponseBody
    public Map getDate() {
        Map<String, Object> map = new HashMap<>();
        map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        map.put("time", simpleDateFormat.format(new Date()));
        return map;
    }

    //根据不同的物料去找不同的料架位置去找不同的标签
    @RequestMapping(value = "getEByRackInfo")
    @ResponseBody
    public Map getEByRackInfo(Long infoid) {
        Map<String, Object> map = new HashMap<>();
        if (infoid == null) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        List<RackInfo> rackInfos = new ArrayList<>();
        if (infoid < 0) {
            //找所有的物料
            rackInfos = rackInfoMapper.selectList(null);
        } else {
            QueryWrapper<RackInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("info_id", infoid);
            rackInfos = rackInfoMapper.selectList(queryWrapper);
        }
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (RackInfo r : rackInfos
        ) {
            QueryWrapper<ESign> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("rackid", r.getInfoId());
            queryWrapper.eq("stocksign", 0);
            queryWrapper.orderByAsc("eid");
            List<ESign> eSignList = eSignMapper.selectList(queryWrapper);
            QueryWrapper<ESign> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("rackid", r.getInfoId());
            queryWrapper1.eq("stocksign", 1);
            queryWrapper1.orderByAsc("eid");
            List<ESign> eSignList1 = eSignMapper.selectList(queryWrapper1);
            for (int i = 0; i < eSignList.size(); i++) {
                //入库
                ESign eSign = eSignList.get(i);
                //出库
                ESign eSign1 = eSignList1.get(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("eid", eSign.getEid());
                jsonObject.put("name", eSign.getMatno());
                jsonObject.put("comno", eSign.getComNo());
                jsonObject.put("comchildno", eSign.getComChildNo());
                jsonObject.put("teid", eSign1.getEid());
                jsonObject.put("tcomno", eSign1.getComNo());
                jsonObject.put("tcomchildno", eSign1.getComChildNo());
                jsonObjectList.add(jsonObject);
            }
        }
        map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        map.put("list", jsonObjectList);
        return map;
    }
    /*修改标签*/

    @PostMapping(value = "updateSign")
    @Transactional
    @ResponseBody
    public Map updateSign(Long id, Long tid, Integer com, Integer tcom,
                              Integer comchild, Integer tcomchild) {
        Map<String, Object> map = new HashMap<>();
        if (id == null || tid == null || CommonUtils.getInstance().IsIntegerEmpty(com)
                || CommonUtils.getInstance().IsIntegerEmpty(tcom) || CommonUtils.getInstance().IsIntegerEmpty(comchild)
                || CommonUtils.getInstance().IsIntegerEmpty(tcomchild)
        ) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        try {
            int res = eSignMapper.updateComNoAndComChildNoByEid(String.valueOf(com),comchild,id);
            int res1 = eSignMapper.updateComNoAndComChildNoByEid(String.valueOf(tcom),tcomchild,tid);
            if (res>0 && res1>0) {
                map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
                return map;
            } else {
                map.put("result", CommonUtils.getInstance().RESULT_fAIL);
                map.put("msg","修改异常");
                return map;
            }

        }catch (Exception e){
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",e.getMessage());
            return map;
        }
    }


}
