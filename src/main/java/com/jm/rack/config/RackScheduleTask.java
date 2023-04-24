package com.jm.rack.config;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jm.rack.mapper.*;
import com.jm.rack.pojo.*;
import com.jm.rack.service.PlanInfoService;
import com.jm.rack.service.PlanMatInfoService;
import com.jm.rack.service.impl.MesPlanServiceImpl;
import com.jm.rack.untils.CommonUtils;
import com.jm.rack.untils.HslHelper;
import com.jm.rack.untils.WebSocketServer;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.*;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Log4j2
public class RackScheduleTask {

    @Autowired
    PlanInfoMapper planInfoMapper;

    @Autowired
    MatRackInfoMapper matRackInfoMapper;

    @Autowired
    ESignMapper eSignMapper;

    @Autowired
    MesPlanMapper mesPlanMapper;

    @Autowired
    MesPlandetailMapper mesPlandetailMapper;

    @Autowired
    MesPlanServiceImpl mesPlanService;


    @Autowired
    PlanMatInfoService planMatInfoService;

    @Autowired
    PlanInfoService planInfoService;

    @Autowired
    StaSeatMapper staSeatMapper;

    SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd ");


    //5分钟显示一次数量 前排 并且有声
    @Scheduled(fixedRate = (60000*5))
    private void checkQRack() {
        //前排
        QueryWrapper<MatRackInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("rackno", "Q");
        /*查找所有的前排料架信息*/
        List<MatRackInfo> matRackInfos = matRackInfoMapper.selectList(queryWrapper);
        /*查找所有的前排料架警戒信息*/
        List<MatRackInfo> minmMtRackInfos = matRackInfoMapper.selectBymatmin("Q%");
        /*货物不足，语音播报*/
        if (minmMtRackInfos.size() > 0) {
            StringBuilder string = new StringBuilder();
            for (MatRackInfo matRackInfo : minmMtRackInfos) {
                string.append("前排报警").append(matRackInfo.getRackno()).append("物料").append(matRackInfo.getName());
            }
            string.append("库存不足，请及时补货");
            for (int i = 0; i < 3; i++) {
                CommonUtils.readSpeech(string.toString());
            }
        }
        /*避免一个料架多种物料量多个灯*/
        CommonUtils.getInstance().getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                List<String> codeList = new ArrayList<>();
                for (MatRackInfo item : matRackInfos) {
                    try {
                        //补货口
                        if (codeList.contains(item.getRackno())) {
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
                        List<JSONObject> QList = new ArrayList<>();
                        QList.add(jsonObject1);
                        qJsonObject.put("list", QList);
//                        WebSocketServer.sendSingleInfo("cplus1", qJsonObject);
                        Thread.sleep(100);
//                        WebSocketServer.sendSingleInfo("cplus1", qJsonObject);
                        Thread.sleep(100);
                        codeList.add(item.getRackno());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
//    8分钟显示一次数量 中排 并且有声
    @Scheduled(fixedRate = (60000*8))
    private void checkZRack() {
        //中排
        QueryWrapper<MatRackInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("rackno", "Z");
        List<MatRackInfo> matRackInfos = matRackInfoMapper.selectList(queryWrapper);

        List<MatRackInfo> matRackInfos1 = matRackInfoMapper.selectBymatmin("Z%");
        if (matRackInfos1.size()>0){
            StringBuilder string = new StringBuilder();
            for (MatRackInfo matRackInfo: matRackInfos1
            ) {
                string.append("中排报警").append(matRackInfo.getRackno()).append("物料").append(matRackInfo.getName());
            }
            string.append("库存不足，请及时补货");
            for (int i = 0; i < 3; i++) {
                CommonUtils.readSpeech(string.toString());
            }
        }
        List<JSONObject> QList = new ArrayList<>();
        List<String> codeList = new ArrayList<>();

        for (MatRackInfo item : matRackInfos
        ) {
            //补货口
            if (codeList.contains(item.getRackno())){
                continue;
            }
            QueryWrapper<ESign> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("stocksign", 0).eq("matno", item.getRackno());
            ESign eSign = eSignMapper.selectOne(queryWrapper3);
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("com", "com" + eSign.getComNo());
            jsonObject1.put("comchild", eSign.getComChildNo());
            jsonObject1.put("count", item.getMatnum());
            jsonObject1.put("color", "无");
            QList.add(jsonObject1);
            codeList.add(item.getRackno());
        }
        JSONObject qJsonObject = new JSONObject();
        qJsonObject.put("type", 1);
        qJsonObject.put("list", QList);
        WebSocketServer.sendSingleInfo("cplus2", qJsonObject);
    }

//    3分钟显示一次数量 后排 并且有声
    @Scheduled(fixedRate = (60000*3))
    private void checkSRack() {
        //中排
        QueryWrapper<MatRackInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("rackno", "S");
        List<MatRackInfo> matRackInfos = matRackInfoMapper.selectList(queryWrapper);

        List<MatRackInfo> matRackInfos1 = matRackInfoMapper.selectBymatmin("S%");
        if (matRackInfos1.size()>0){
            StringBuilder string = new StringBuilder();
            for (MatRackInfo matRackInfo: matRackInfos1
            ) {
                string.append("三排报警").append(matRackInfo.getRackno()).append("物料").append(matRackInfo.getName());
            }
            string.append("库存不足，请及时补货");
            for (int i = 0; i < 3; i++) {
                CommonUtils.readSpeech(string.toString());
            }
        }
        List<JSONObject> QList = new ArrayList<>();
        List<String> codeList = new ArrayList<>();

        for (MatRackInfo item : matRackInfos
        ) {
            //补货口
            if (codeList.contains(item.getRackno())){
                continue;
            }
            QueryWrapper<ESign> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("stocksign", 0).eq("matno", item.getRackno());
            ESign eSign = eSignMapper.selectOne(queryWrapper3);
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("com", "com" + eSign.getComNo());
            jsonObject1.put("comchild", eSign.getComChildNo());
            jsonObject1.put("count", item.getMatnum());
            jsonObject1.put("color", "无");
            QList.add(jsonObject1);
            codeList.add(item.getRackno());
        }
        JSONObject qJsonObject = new JSONObject();
        qJsonObject.put("type", 1);
        qJsonObject.put("list", QList);
        WebSocketServer.sendSingleInfo("cplus3", qJsonObject);
    }

    //定时 半小时 更新计划
    @Scheduled(cron = "0 0/10 * * * ?")
    private void loadPlan() {
        for (int i = 0; i < 3; i++) {
            String LineNo = "";
            if (i == 1) {
                LineNo = CommonUtils.getInstance().middleLineNo;
            } else if (i == 2) {
                LineNo = CommonUtils.getInstance().lastLineNo;
            } else {
                LineNo = CommonUtils.getInstance().frontLineNo;
            }
            Map<String, String> stringMap = new HashMap<>();
            stringMap.put("lineno", LineNo);
            String format = ymdFormat.format(new Date());
            stringMap.put("time", format + "00:00:00.000");
            List<MesPlan> mesPlans = mesPlanMapper.selectMesPlanByLineName(stringMap);
            //如果
            for (MesPlan plan : mesPlans) {
                //查 plan_mat_info有没有对应的计划号
                QueryWrapper<PlanInfo> planMatInfoQueryWrapper = new QueryWrapper<>();
                planMatInfoQueryWrapper.eq("planno", plan.getPlanno());
                Long aLong = planInfoMapper.selectCount(planMatInfoQueryWrapper);
                if (aLong == 0) {
                    //插入详情
                    List<MesPlandetail> mesPlandetails = mesPlandetailMapper.selectMesPlanDetailsByLineNo(plan.getPlanno());
                    for (int k = 0; k < mesPlandetails.size(); k++) {
                        MesPlandetail m = mesPlandetails.get(k);
                        long no = k + 1;
                        PlanInfo info = new PlanInfo(m.getPlanno(), no, m.getUpdatetime(), m.getCarvin(), m.getProno(), m.getProname(), m.getCartype(), LineNo, 0, new Date());
                        planInfoMapper.insert(info);
                    }
                }
            }

        }
    }


    //定时去数据库查时间通过carvin更新时间
    @Scheduled(cron = "0 0/1 * * * ?")
    private void updateProdate() {
        List<PlanInfo> planInfoList = planInfoMapper.selectByCarvinAndTime();
        List<PlanInfo> list = new ArrayList<>();
        for (PlanInfo plan : planInfoList) {
            PlanInfo planInfo = new PlanInfo();
            planInfo.setPlanId(plan.getPlanId());
            StaSeat staSeat = staSeatMapper.selectOneByVin(plan.getCarvin());
            if (staSeat != null && staSeat.getUpdateTime() != null) {
                planInfo.setProductdate(staSeat.getUpdateTime());
                list.add(planInfo);
            }
        }
        if (list.size() > 0) {
            planInfoService.saveOrUpdateBatch(list);
        }
    }






}

