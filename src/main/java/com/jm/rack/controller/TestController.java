package com.jm.rack.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jm.rack.mapper.ESignMapper;
import com.jm.rack.mapper.MatRackInfoMapper;
import com.jm.rack.pojo.ESign;
import com.jm.rack.pojo.MatRackInfo;
import com.jm.rack.untils.CommonUtils;
import com.jm.rack.untils.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Transactional
@RequestMapping("test/")
public class TestController {
    @Autowired
    ESignMapper eSignMapper;
    @Autowired
    MatRackInfoMapper matRackInfoMapper;




    @RequestMapping(value = "send")
    @ResponseBody
    public Map<String, Object> update() {
        Map<String,Object> map = new HashMap<>();
        List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("comno","com"+i);
            jsonObject.put("comchildno",i);
            jsonObject.put("count",i);
            list.add(jsonObject);
        }
        map.put("type","mat");
        map.put("content",list);
        QueryWrapper<ESign> eSignQueryWrapper = new QueryWrapper<>();
        eSignQueryWrapper.eq("com_no","com20".substring(3)).eq("com_child_no",15);
        ESign eSign = eSignMapper.selectOne(eSignQueryWrapper);
        System.out.println(eSign);
        //WebSocketServer.sendSingleInfo("mat",map);
        return map;
    }


    @RequestMapping(value = "sendSign")
    @ResponseBody
    public Map<String, Object> sendSign() {
        Map<String,Object> map = new HashMap<>();
//        JSONObject jsonObject1 = new JSONObject();
//        jsonObject1.put("type", 4);
//        jsonObject1.put("com", "com2");
//        jsonObject1.put("comchild","50");
//        jsonObject1.put("count", 1);//这个没任何含义
//        jsonObject1.put("color","绿");
//        WebSocketServer.sendSingleInfo("cplus1", jsonObject1);


        QueryWrapper<MatRackInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("rackno", "Q");
        List<MatRackInfo> matRackInfos = matRackInfoMapper.selectList(queryWrapper);
        List<MatRackInfo> matRackInfos1 = matRackInfoMapper.selectBymatmin("Q%");
        if (matRackInfos1.size()>0){
            StringBuilder string = new StringBuilder();
            for (MatRackInfo matRackInfo: matRackInfos1
            ) {
                string.append("前排报警").append(matRackInfo.getRackno()).append("物料").append(matRackInfo.getName());
            }
            string.append("库存不足，请及时补货");
            for (int i = 0; i < 3; i++) {
                CommonUtils.readSpeech(string.toString());
            }
        }
        List<JSONObject> QList = new ArrayList<>();
        List<String> codeList = new ArrayList<>();
        List<Integer> numList = new ArrayList<>();
        for (MatRackInfo item : matRackInfos
        ) {

            try {
                //补货口
                if (codeList.contains(item.getRackno())){
                    continue;
                }
                JSONObject qJsonObject = new JSONObject();
                qJsonObject.put("type", 1);
                QueryWrapper<ESign> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.eq("stocksign", 0).eq("matno", item.getRackno());
                ESign eSign = eSignMapper.selectOne(queryWrapper3);
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("com", "com" + eSign.getComNo());
                jsonObject1.put("comchild", eSign.getComChildNo());
                jsonObject1.put("count", item.getMatnum());
                jsonObject1.put("color", "无");
                QList.add(jsonObject1);
                qJsonObject.put("list", QList);
                WebSocketServer.sendSingleInfo("cplus1", qJsonObject);
                Thread.sleep(100);
                WebSocketServer.sendSingleInfo("cplus1", qJsonObject);
                Thread.sleep(100);
//                numList.add(eSign.getComChildNo());
                codeList.add(item.getRackno());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @RequestMapping(value = "sendSign1")
    @ResponseBody
    public Map<String, Object> sendSign1() {
        Map<String,Object> map = new HashMap<>();
        CommonUtils.getInstance().getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 57; i < 85; i++) {
                    List<JSONObject> QList = new ArrayList<>();
                    try {
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("com", "com10");
                        jsonObject1.put("comchild",i);
                        jsonObject1.put("count",i);
                        jsonObject1.put("color", "绿");
                        QList.add(jsonObject1);
                        JSONObject qJsonObject = new JSONObject();
                        qJsonObject.put("type", 1);
                        qJsonObject.put("list", QList);
                        WebSocketServer.sendSingleInfo("cplus1", qJsonObject);
                        Thread.sleep(100);
                        WebSocketServer.sendSingleInfo("cplus1", qJsonObject);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        map.put("result",CommonUtils.getInstance().RESULT_SUCCESS);
        return map;
    }


    @RequestMapping(value = "sendSign2")
    @ResponseBody
    public Map<String, Object> sendSign2() {
        Map<String,Object> map = new HashMap<>();
        List<JSONObject> QList = new ArrayList<>();
        for (int i = 30; i < 56; i++) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("com", "com1");
            jsonObject1.put("comchild",i);
            jsonObject1.put("count",456);
            jsonObject1.put("color", "绿");
            QList.add(jsonObject1);
        }
        JSONObject qJsonObject = new JSONObject();
        qJsonObject.put("type", 7);
        qJsonObject.put("list", QList);
        WebSocketServer.sendSingleInfo("cplus1", qJsonObject);
        return map;
    }


    @RequestMapping(value = "sendSign3")
    @ResponseBody
    public Map<String, Object> sendSign3() {
        Map<String,Object> map = new HashMap<>();
        List<JSONObject> QList = new ArrayList<>();
        for (int i = 1; i <= 28; i++) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("com", "com1");
            jsonObject1.put("comchild",i);
            jsonObject1.put("count",456);
            jsonObject1.put("color", "绿");
            QList.add(jsonObject1);
        }
        JSONObject qJsonObject = new JSONObject();
        qJsonObject.put("type", 7);
        qJsonObject.put("list", QList);
        WebSocketServer.sendSingleInfo("cplus1", qJsonObject);
        return map;
    }

    @RequestMapping(value = "sendSign4")
    @ResponseBody
    public Map<String, Object> sendSign4() {
        Map<String,Object> map = new HashMap<>();
        List<JSONObject> QList = new ArrayList<>();
        for (int i = 57; i <= 84; i++) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("com", "com1");
            jsonObject1.put("comchild",i);
            jsonObject1.put("count",456);
            jsonObject1.put("color", "绿");
            QList.add(jsonObject1);
        }
        JSONObject qJsonObject = new JSONObject();
        qJsonObject.put("type", 7);
        qJsonObject.put("list", QList);
        WebSocketServer.sendSingleInfo("cplus1", qJsonObject);
        return map;
    }

    @RequestMapping(value = "sendSign5")
    @ResponseBody
    public Map<String, Object> sendSign5() {
        Map<String,Object> map = new HashMap<>();
        List<JSONObject> QList = new ArrayList<>();
        for (int i = 85; i <= 112; i++) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("com", "com1");
            jsonObject1.put("comchild",i);
            jsonObject1.put("count",456);
            jsonObject1.put("color", "绿");
            QList.add(jsonObject1);
        }
        JSONObject qJsonObject = new JSONObject();
        qJsonObject.put("type", 7);
        qJsonObject.put("list", QList);
        WebSocketServer.sendSingleInfo("cplus1", qJsonObject);
        return map;
    }







}
