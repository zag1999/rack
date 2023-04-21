package com.jm.rack.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jm.rack.fl.common.bo.Result;
import com.jm.rack.fl.commonCache.CommonCache;
import com.jm.rack.fl.storeOperate.service.StoreOperateService;
import com.jm.rack.mapper.*;
import com.jm.rack.pojo.*;
import com.jm.rack.service.PlanInfoService;
import com.jm.rack.service.PlanMatInfoService;
import com.jm.rack.service.impl.MesPlanServiceImpl;
import com.jm.rack.untils.CommonUtils;
import com.jm.rack.untils.HslHelper;
import com.jm.rack.untils.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "mesfact/")
public class PlanController {
    SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd ");
    SimpleDateFormat ymdFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    PlanInfoMapper planInfoMapper;

    @Autowired
    PlanInfoService planInfoService;

    @Autowired
    PlanMatInfoMapper planMatInfoMapper;

    @Autowired
    PlanMatInfoService planMatInfoService;

    @Autowired
    MesPlanServiceImpl mesPlanService;

    @Autowired
    MaterialInfoMapper materialInfoMapper;

    @Autowired
    ESignMapper eSignMapper;

    @Autowired
    MatRackInfoMapper rackInfoMapper;

    @Autowired
    MatRackInfoMapper matRackInfoMapper;

    @Autowired
    MesPlanMapper mesPlanMapper;

    @Autowired
    MesPlandetailMapper mesPlandetailMapper;

    @Autowired
    OutmaintainMapper outmaintainMapper;
    @Resource
    private StoreOperateService storeOperateService;
    @Resource
    private CommonCache commonCache;

    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");


    //不同的线去自己查自己的计划
    @RequestMapping(value = "getplanByType")
    @ResponseBody
    public Map<String, Object> getplanByType(Integer type) {
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.getInstance().IsIntegerEmpty(type)) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        String LineNo = "";
        if (type == 1) {
            LineNo = CommonUtils.getInstance().middleLineNo;
        } else if (type == 2) {
            LineNo = CommonUtils.getInstance().lastLineNo;
        } else {
            LineNo = CommonUtils.getInstance().frontLineNo;
        }
        //先去mes里查计划号
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("lineno", LineNo);
        String format = ymdFormat.format(new Date());
        stringMap.put("time", format + " 00:00:00.000");
        List<MesPlan> mesPlans = mesPlanMapper.selectMesPlanByLineName(stringMap);
        for (MesPlan plan : mesPlans) {
            //查 plan_info有没有对应的计划号
            QueryWrapper<PlanInfo> planMatInfoQueryWrapper = new QueryWrapper<>();
            planMatInfoQueryWrapper.eq("planno", plan.getPlanno()).eq("linetype", LineNo);
            Long aLong = planInfoMapper.selectCount(planMatInfoQueryWrapper);
            if (aLong == 0) {
                //插入详情 加入序号
                List<MesPlandetail> mesPlandetails = mesPlandetailMapper.selectMesPlanDetailsByLineNo(plan.getPlanno());
                for (int i = 0; i < mesPlandetails.size(); i++) {
                    MesPlandetail m = mesPlandetails.get(i);
                    long no = i + 1;
                    PlanInfo info = new PlanInfo(m.getPlanno(), no, m.getUpdatetime(), m.getCarvin(), m.getProno(), m.getProname(), m.getCartype(), LineNo, 0, new Date());
                    planInfoMapper.insert(info);
                }
            }
        }
        QueryWrapper<PlanInfo> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("linetype", LineNo).ne("state", 2);
        Page<PlanInfo> page = new Page<>(1, 10);
        IPage<PlanInfo> planInfoIPage = planInfoMapper.selectPage(page, queryWrapper2);
        List<PlanInfo> lastList = new ArrayList<>();
        if (planInfoIPage.getRecords() != null) {
            List<String> stringList = new ArrayList<>();
            for (PlanInfo m : planInfoIPage.getRecords()) {
                if (CommonUtils.getInstance().IsStringEmpty(m.getCarvin())) {
                    stringList.add("0");
                } else {
                    if (!stringList.contains(m.getCarvin())) {
                        stringList.add(m.getCarvin());
                    }
                }
            }
            int index = 0;
            for (String code : stringList) {
                List<PlanInfo> list = planInfoIPage.getRecords();
                if ("0".equals(code)) {
                    lastList.add(list.get(index));
                    ++index;
                } else {
                    List<PlanInfo> mList = new ArrayList<>();
                    for (PlanInfo m : planInfoIPage.getRecords()) {
                        if (code.equals(m.getCarvin())) {
                            mList.add(m);
                        }
                    }
                    if (mList.size() == 2) {
                        lastList.add(mList.get(1));
                        lastList.add(mList.get(0));
                        ++index;
                    } else {
                        lastList.add(mList.get(0));
                        ++index;
                    }
                }
            }
        }
        map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        map.put("list", lastList);
        return map;
    }

    //通过车身号和prono去选择物料并通知亮灯
    @RequestMapping(value = "findMatByVinAndPro")
    @ResponseBody
    public Map<String, Object> findMatByVinAndPro(Long id, Integer type) throws InterruptedException {
        Map<String, Object> map = new HashMap<>();
        if (id == null) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }

        // 缓存计划id
        commonCache.setPlanId(type, id);
        /**/
        QueryWrapper<PlanInfo> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("plan_id", id);
        //属于哪个计划
        PlanInfo planInfo = planInfoMapper.selectOne(queryWrapper1);
        //查 PlanMatInfo 里是否已经有此计划了
        QueryWrapper<PlanMatInfo> planMatInfoQueryWrapper = new QueryWrapper<>();

        planMatInfoQueryWrapper.eq("prono", planInfo.getProno());
        Long aLong = planMatInfoMapper.selectCount(planMatInfoQueryWrapper);
        if (aLong == 0) {
            //去查bom表里的物料
            List<AaBom> bomByProNo = mesPlanService.getBomByProNo(planInfo.getProno());
            List<PlanMatInfo> list1 = new ArrayList<>();
            for (AaBom a : bomByProNo) {
                //用bom表的prono 和 matno 去 查柜外物料是否有
                PlanMatInfo planMatInfo;
                QueryWrapper<Outmaintain> outmaintainQueryWrapper = new QueryWrapper<>();
                outmaintainQueryWrapper.eq("prono", a.getProno()).eq("matcode", a.getMatno());
                List<Outmaintain> outmaintains = outmaintainMapper.selectList(outmaintainQueryWrapper);
                if (outmaintains.size() > 0) {
                    //柜外物料存在
                    double d = outmaintains.get(0).getCount();
                    planMatInfo = new PlanMatInfo(0, a.getProno(), planInfo.getCarvin(), a.getMatno(), a.getMatname(), d, new Date(), planInfo.getPlanId());
                    list1.add(planMatInfo);
                } else {
                    //找柜内物料
                    QueryWrapper<MatRackInfo> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("code", a.getMatno());
                    List<MatRackInfo> matRackInfos = matRackInfoMapper.selectList(queryWrapper);
                    if (matRackInfos.size() > 0) {
                        planMatInfo = new PlanMatInfo(0, a.getProno(), planInfo.getCarvin(), a.getMatno(), a.getMatname(), a.getQuantity(), new Date(), planInfo.getPlanId());
                        list1.add(planMatInfo);
                    }
                }
            }
            planMatInfoService.saveBatch(list1);
        } else {
            planMatInfoMapper.updateMatstateByProno(0, planInfo.getProno(), id);
        }
        /*根据计划去找bom*/
        QueryWrapper<PlanMatInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planid", id);
        List<PlanMatInfo> planMatInfos = planMatInfoMapper.selectList(queryWrapper);
        /*根据 type  知道是哪一排的数据*/
        String rackSign = "";
        switch (type) {
            case 0:
                rackSign = "Q";
                break;
            case 1:
                rackSign = "Z";
                break;
            case 2:
                rackSign = "S";
                break;
        }

        List<JSONObject> jsonObjectList = new ArrayList<>();
        List<JSONObject> eSignList11 = new ArrayList<>();
        //库存数
        ArrayList<MatRackInfo> kucunList = new ArrayList<>();
        for (PlanMatInfo planMatInfo : planMatInfos) {
            //物料编码取找某通道的物料
            //从最新的matrackinfo表去找
            QueryWrapper<MatRackInfo> matRackInfoQueryWrapper = new QueryWrapper<>();
            matRackInfoQueryWrapper.likeRight("rackno", rackSign).eq("code", planMatInfo.getMatno());
            MatRackInfo matRackInfo = rackInfoMapper.selectOne(matRackInfoQueryWrapper);
            kucunList.add(matRackInfo);
            //找到的物料信息
            JSONObject jsonObject = new JSONObject();
            String sign = "";//outside
            String color = "";
            String planInfoName = planInfo.getProname();
            if (planInfoName.contains("左")) {
                sign = "left";
                color = "黄";
            } else if (planInfoName.equals("右")) {
                sign = "right";
                color = "绿";
            } else {
                String substring = planInfoName.substring(0, 1);
                if ("驾".equals(substring)) {
                    sign = "left";
                    color = "黄";
                } else if ("副".equals(substring)) {
                    sign = "right";
                    color = "绿";
                } else {
                    sign = "ordinary";
                    color = "蓝";
                }
            }
            if (matRackInfo != null) {
                jsonObject.put("pos", matRackInfo.getRackno());
                //电子标签需要亮灯  1 取货口
                QueryWrapper<ESign> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.eq("stocksign", 1).eq("matno", matRackInfo.getRackno());
                ESign eSign = eSignMapper.selectOne(queryWrapper3);
                //显数亮灯
                HslHelper.getInstance().writeShort(eSign.getComNo() + eSign.getComChildNo(), planMatInfo.getQuantity().shortValue(), rackSign);
                HslHelper.getInstance().writeShort(eSign.getComNo() + eSign.getOpenLight(), HslHelper.LV_DENG, rackSign);
                List<JSONObject> eSignList = new ArrayList<>();
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("com", "com" + eSign.getComNo());
                jsonObject1.put("comchild", eSign.getComChildNo());
                jsonObject1.put("openlight", eSign.getOpenLight());
                jsonObject1.put("count", (int) Math.floor(planMatInfo.getQuantity()));
                jsonObject1.put("color", color);
                eSignList.add(jsonObject1);
                eSignList11.add(jsonObject1);
                jsonObject.put("type", 7);
                jsonObject.put("list", eSignList);
                planMatInfo.setRackno(matRackInfo.getRackno());
                jsonObject.put("rackno", matRackInfo.getRackno());
            } else {
                sign = "outside";
                jsonObject.put("pos", "柜外物料");
                planMatInfo.setRackno("");
                jsonObject.put("rackno", "");
            }
            planMatInfo.setPos(sign);
            jsonObject.put("code", planMatInfo.getMatno());
            jsonObject.put("name", planMatInfo.getMatname());
            jsonObject.put("count", planMatInfo.getQuantity());
            jsonObject.put("sign", sign);
            jsonObjectList.add(jsonObject);
        }
        //最后要根据type判断向哪个料架地址发送交互字告诉plc进行显数亮灯
        if (null != rackSign) {
            HslHelper.getInstance().writeJiaoHuZi(rackSign);
        }
        //写完交互字要显示库存数,写完库存数也要写交互字,交互字地址就是对应的101的显数地址
//        QueryWrapper<ESign> wrapper = new QueryWrapper<>();
//        if (null != kucunList && kucunList.size() > 0){
//            //关联e_sign表根据matno,查出库存数及库存对应的db地址
//            for (MatRackInfo matRackInfo : kucunList) {
//                wrapper.select().likeRight("matno",rackSign).eq("stocksign",0);
//                ESign eSign = eSignMapper.selectOne(wrapper);
//                if (null != eSign){
//                    //写入库存数
//                    HslHelper.getInstance().writeShort(eSign.getComNo()+eSign.getComChildNo(),matRackInfo.getMatnum().shortValue());
//                    //交互字
//                    HslHelper.getInstance().writeShort(HslHelper.JIAO_HU_ZI_301,HslHelper.JIAO_HU_ZI_VALUE);
//                    HslHelper.getInstance().writeShort(HslHelper.JIAO_HU_ZI_401,HslHelper.JIAO_HU_ZI_VALUE);
//                }
//            }
//        }
        map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        map.put("list", planMatInfos);
        map.put("eSignList", eSignList11);
        map.put("groundlist", jsonObjectList);
        return map;
    }

    /**
     * 查询状态,修改页面当前计划物料状态,修改库存数
     * @param type
     * @return
     * @throws InterruptedException
     */
    @RequestMapping(value = "findState")
    @ResponseBody
    public Map<String, Object> findState(Integer type) throws InterruptedException {
        //根据type读取对应db地址的总完成
        HashMap<String, Object> resultMap = new HashMap<>();
        boolean readState = false;
        if (0 == type) {
            //修改物料状态
            storeOperateService.updateMatStatus(type);
            //前排取货口完成总信号
            Integer sign1 = HslHelper.getInstance().readInt(HslHelper.QUAN_MIE_OVER_101, "Q");
            Integer sign2 = HslHelper.getInstance().readInt(HslHelper.QUAN_MIE_OVER_201, "Q");
            if (sign1 == 1 && sign2 == 1) {
                // 读到完成总信号后给plc回个2
                HslHelper.getInstance().writeShort(HslHelper.QUAN_MIE_OVER_101, HslHelper.QUAN_MIE_OVER_VALUE, "Q");
                HslHelper.getInstance().writeShort(HslHelper.QUAN_MIE_OVER_201, HslHelper.QUAN_MIE_OVER_VALUE, "Q");
                // 执行减库存
                boolean success = storeOperateService.subtractStore(type);
                //避免plc还没有进行赋值
                Thread.sleep(3000);
                //读库存数完成信号118
                Integer kucun1 = HslHelper.getInstance().readInt("DB301.DBW118.0", "Q");
                Integer kucun2 = HslHelper.getInstance().readInt("DB401.DBW118.0", "Q");
                if (kucun1 == 1){
                    HslHelper.getInstance().writeShort("DB301.DBW118.0", HslHelper.QUAN_MIE_OVER_VALUE, "Q");
                }
                if (kucun2 == 1){
                    HslHelper.getInstance().writeShort("DB401.DBW118.0", HslHelper.QUAN_MIE_OVER_VALUE, "Q");
                }
                // 如果减库存成功才会通知前端切换计划
                readState = success;
            }
        } else if (1 == type) {
            //修改物料状态
            storeOperateService.updateMatStatus(type);
            //中排5,6,7取货口完成信号
            Integer sign5 = HslHelper.getInstance().readInt("DB501." + HslHelper.QUAN_MIE_OVER_170, "Z1");
            Integer sign6 = HslHelper.getInstance().readInt("DB601." + HslHelper.QUAN_MIE_OVER_170, "Z1");
            Integer sign7 = HslHelper.getInstance().readInt("DB701." + HslHelper.QUAN_MIE_OVER_170, "Z1");
            if (sign5 == 1 && sign6 == 1 && sign7 == 1) {
                // 读到完成总信号后告诉plc进行复位
                HslHelper.getInstance().writeShort("DB501." + HslHelper.QUAN_MIE_OVER_170, HslHelper.QUAN_MIE_OVER_VALUE, "Z1");
                HslHelper.getInstance().writeShort("DB601." + HslHelper.QUAN_MIE_OVER_170, HslHelper.QUAN_MIE_OVER_VALUE, "Z1");
                HslHelper.getInstance().writeShort("DB701." + HslHelper.QUAN_MIE_OVER_170, HslHelper.QUAN_MIE_OVER_VALUE, "Z1");
                // 执行减库存
                boolean success = storeOperateService.subtractStore(type);
                //避免plc还没有进行赋值
                Thread.sleep(3000);
                //读库存数完成信号118
                Integer kucun1 = HslHelper.getInstance().readInt("DB801.DBW118.0", "Z2");
                Integer kucun2 = HslHelper.getInstance().readInt("DB901.DBW118.0", "Z2");
                Integer kucun3 = HslHelper.getInstance().readInt("DB1001.DBW118.0", "Z2");
                if (kucun1 == 1){
                    HslHelper.getInstance().writeShort("DB801.DBW118.0", HslHelper.QUAN_MIE_OVER_VALUE, "Z2");
                }
                if (kucun2 == 1){
                    HslHelper.getInstance().writeShort("DB901.DBW118.0", HslHelper.QUAN_MIE_OVER_VALUE, "Z2");
                }
                if (kucun3 == 1){
                    HslHelper.getInstance().writeShort("DB1001.DBW118.0", HslHelper.QUAN_MIE_OVER_VALUE, "Z2");
                }
                // 如果减库存成功才会通知前端切换计划
                readState = success;
            }
        } else if (2 == type) {
            //修改物料状态
            storeOperateService.updateMatStatus(type);
            //三排取货口完成信号
            Integer sign1101 = HslHelper.getInstance().readInt("DB1101." + HslHelper.QUAN_MIE_OVER_170, "S");
            if (sign1101 == 1) {
                // 读到完成总信号后告诉plc进行复位
                HslHelper.getInstance().writeShort("DB1101." + HslHelper.QUAN_MIE_OVER_170, HslHelper.QUAN_MIE_OVER_VALUE, "S");
                // 执行减库存
                boolean success = storeOperateService.subtractStore(type);
                //避免plc还没有进行赋值
                Thread.sleep(3000);
                //读库存数完成信号118
                Integer kucun1 = HslHelper.getInstance().readInt("DB1201.DBW118.0", "S");
                if (kucun1 == 1){
                    HslHelper.getInstance().writeShort("DB1201.DBW118.0", HslHelper.QUAN_MIE_OVER_VALUE, "S");
                }
                // 如果减库存成功才会通知前端切换计划
                readState = success;
            }
        }
        resultMap.put("result", "success");
        resultMap.put("readstate", readState);
        return resultMap;
    }

    //拣货完成  通知状态修改  以及减少库存数量 并且 通知刷新库存变化
//    @RequestMapping(value = "notifycount")
//    @ResponseBody
//    public Map<String, Object> notifycount(String prono,String carvin) {
//        Map<String, Object> map = new HashMap<>();
//        if (CommonUtils.getInstance().IsStringEmpty(prono)){
//            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
//            map.put("msg",CommonUtils.getInstance().PARAM_LOSE);
//            return map;
//        }
//        try {
//            planInfoMapper.updateStateByPronoAndCarvin(2,prono,carvin);
//            planMatInfoMapper.updateMatstateByPronoAndCarvin(1,prono,carvin);
//            //修改库存
//            QueryWrapper<PlanMatInfo> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("prono",prono).eq("carvin",carvin);
//            List<PlanMatInfo> planMatInfos = planMatInfoMapper.selectList(queryWrapper);
//            List<JSONObject> eList = new ArrayList<>();
//            String pos = "";
//            for (int i = 0; i < planMatInfos.size(); i++) {
//                //按Matno 修改 数量
//                QueryWrapper<MatRackInfo> queryWrapper1 = new QueryWrapper<>();
//                queryWrapper1.eq("code",planMatInfos.get(i).getMatno());
//                MatRackInfo matRackInfo = matRackInfoMapper.selectOne(queryWrapper1);
//                String s = planMatInfos.get(i).getQuantity()+"";
//                matRackInfoMapper.updateMatnumByCode(matRackInfo.getMatnum()-Integer.parseInt(s),matRackInfo.getCode());
//                QueryWrapper<ESign> queryWrapper3 = new QueryWrapper<>();
//                queryWrapper3.eq("stocksign", 0).eq("matno", matRackInfo.getRackno());
//                ESign eSign = eSignMapper.selectOne(queryWrapper3);
//                JSONObject jsonObject1 = new JSONObject();
//                jsonObject1.put("com", "com" + eSign.getComNo());
//                jsonObject1.put("comchild", eSign.getComChildNo());
//                jsonObject1.put("count", matRackInfo.getMatnum());
//                jsonObject1.put("color", "无");
//                eList.add(jsonObject1);
//                if (CommonUtils.getInstance().IsStringEmpty(pos)){
//                    if (eSign.getMatno().contains("Q")){
//                        pos = "cplus1";
//                    }else if (eSign.getMatno().contains("Z")){
//                        pos = "cplus2";
//                    }else{
//                        pos = "cplus3";
//                    }
//                }
//
//            }
//            JSONObject qJsonObject = new JSONObject();
//            qJsonObject.put("type", 1);
//            qJsonObject.put("list", eList);
//            //WebSocketServer.sendSingleInfo(pos,qJsonObject);
//            map.put("result",CommonUtils.getInstance().RESULT_SUCCESS);
//        }catch (Exception e){
//            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
//            map.put("msg",e.getMessage());
//        }
//        return map;
//    }
    /*计划变更 通知刷新库存*/
    @RequestMapping(value = "planUpdate")
    @ResponseBody
    public Map<String, Object> planUpdate(PlanInfo planInfo) throws InterruptedException {
        Map<String, Object> map = new HashMap<>();
        if (planInfo.getPlanId() == null) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        if (CommonUtils.getInstance().IsIntegerEmpty(planInfo.getState())) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }

        try {
            if (planInfo.getState() == 2) {
                planMatInfoMapper.updateMatstateByPlanid(1, planInfo.getPlanId());
                List<PlanMatInfo> planMatInfos = planMatInfoMapper.selectAllByPlanid(planInfo.getPlanId());
                //查数量修改数量
                for (PlanMatInfo planMatInfo : planMatInfos) {
                    //当计划变更,那么上次计划物料相对应的库存数应减一,
                    // 只需要将库存数减一就行,定时任务会定时查库存数
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("code", planMatInfo.getMatno());
                    map1.put("count", planMatInfo.getQuantity());
                    matRackInfoMapper.updateMatNumByOneCode(map1);
                }
            }
            planInfoMapper.updateById(planInfo);
            if (!CommonUtils.getInstance().IsStringEmpty(planInfo.getStr())) {
                QueryWrapper<MatRackInfo> queryWrapper = new QueryWrapper<>();
                queryWrapper.likeRight("rackno", "Q");
                /*查找所有的前排料架信息*/
                List<MatRackInfo> matRackInfos = matRackInfoMapper.selectList(queryWrapper);
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
//                                HslHelper.getInstance().writeShort(eSign.getComNo()+eSign.getComChildNo(),item.getMatnum().shortValue());
//                                WebSocketServer.sendSingleInfo("cplus1", qJsonObject);
                                Thread.sleep(100);
//                                HslHelper.getInstance().writeShort(eSign.getComNo()+eSign.getComChildNo(),item.getMatnum().shortValue());
//                                WebSocketServer.sendSingleInfo("cplus1", qJsonObject);
                                Thread.sleep(100);
                                codeList.add(item.getRackno());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
            map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        } catch (Exception e) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", e.getMessage());
        }
        return map;
    }

    /*分页查计划*/
    @RequestMapping(value = "selectPlanByPage")
    @ResponseBody
    public Map<String, Object> selectPlanByPage(String planno) {
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.getInstance().IsStringEmpty(planno)) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        QueryWrapper<PlanInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planno", planno);
        List<PlanInfo> records = planInfoMapper.selectList(queryWrapper);
        for (PlanInfo p : records) {
            if (p.getProductdate() == null) {
                continue;
            }
            p.setTime(ymdFormat1.format(p.getProductdate()));
        }
        map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        map.put("list", records);
        return map;
    }

    //线体查计划号
    @RequestMapping(value = "selectPlanNoByLine")
    @ResponseBody
    public Map<String, Object> selectPlanNoByLine(String lineno) {
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.getInstance().IsStringEmpty(lineno)) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        List<PlanInfo> records = planInfoMapper.selectPlannoByLinetype(lineno);
        map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        map.put("list", records);
        return map;
    }

    /*修改计划状态*/
    @RequestMapping(value = "updatePlanTime")
    @ResponseBody
    public Map<String, Object> updatePlanTime(Long planid, Integer state) {
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.getInstance().IsIntegerEmpty(state) || planid == null) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        PlanInfo planInfo = new PlanInfo();
        planInfo.setPlanId(planid);
        planInfo.setState(state);
        try {
            int i = planInfoMapper.updateById(planInfo);
            map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        } catch (Exception e) {
            map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
            map.put("msg", e.getMessage());
        }
        return map;
    }

    //通知计划要调整
    @RequestMapping(value = "notifyPlanAdjust")
    @ResponseBody
    public Map<String, Object> notifyPlanAdjust(Integer state) {
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.getInstance().IsIntegerEmpty(state)) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "adjustment");
            jsonObject.put("state", state);
            WebSocketServer.sendSingleInfo("mat0", jsonObject);
            WebSocketServer.sendSingleInfo("mat1", jsonObject);
            WebSocketServer.sendSingleInfo("mat2", jsonObject);
            map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        } catch (Exception e) {
            map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
            map.put("msg", e.getMessage());
        }
        return map;
    }

    //批量调整计划状态
    @PostMapping(value = "updateStateByBatch")
    @ResponseBody
    public Map<String, Object> updateStateByBatch(String ids, Integer state) {
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.getInstance().IsIntegerEmpty(state)) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        try {
            if (ids.contains(",")) {
                //说明是批量修改
                List<PlanInfo> planInfoList = new ArrayList<>();
                String substring = ids.substring(0, ids.length() - 1);
                String[] split = substring.split(",");
                for (String s : split) {
                    PlanInfo planInfo = new PlanInfo();
                    planInfo.setState(state);
                    planInfo.setPlanId(Long.parseLong(s));
                    planInfoList.add(planInfo);
                }
                planInfoService.saveOrUpdateBatch(planInfoList);
            } else {
                //用计划号  修改所有
                planInfoMapper.updateStateByPlanno(state, ids);
            }
            map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        } catch (Exception e) {
            map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
            map.put("msg", e.getMessage());
        }
        return map;
    }

    /*按过点时间排序*/
    @RequestMapping(value = "updateTimeByPlanId")
    @ResponseBody
    public Map<String, Object> updateTimeByPlanId(Integer index, String code, String time) {
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.getInstance().IsIntegerEmpty(index) || CommonUtils.getInstance().IsStringEmpty(code)) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        try {
            if (index == 0) {
                QueryWrapper<PlanInfo> planInfoQueryWrapper = new QueryWrapper<>();
                planInfoQueryWrapper.eq("planno", code);
                List<PlanInfo> planInfoList = planInfoMapper.selectList(planInfoQueryWrapper);
                long time1 = ymdFormat1.parse(time).getTime();
                List<PlanInfo> planInfoList1 = new ArrayList<>();
                for (PlanInfo p : planInfoList) {
                    PlanInfo planInfo = new PlanInfo();
                    planInfo.setPlanId(p.getPlanId());
                    time1 += 1000;
                    Date date = new Date(time1);
                    planInfo.setProductdate(date);
                    planInfoList1.add(planInfo);
                }
                planInfoService.saveOrUpdateBatch(planInfoList1);
            } else {
                QueryWrapper<PlanInfo> planInfoQueryWrapper = new QueryWrapper<>();
                planInfoQueryWrapper.eq("plan_id", code);
                PlanInfo planInfo = new PlanInfo();
                planInfo.setProductdate(ymdFormat1.parse(time));
                planInfoMapper.update(planInfo, planInfoQueryWrapper);
            }
            map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        } catch (Exception e) {
            map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
            map.put("msg", e.getMessage());
        }
        return map;
    }

    /**
     * 根据计划id查询当前计划所需物料信息
     * @param planId
     * @return
     */
    @PostMapping("getPlanMatInfoListByPlanId/{planId}")
    @ResponseBody
    public Result<List<PlanMatInfo>> getPlanMatInfoListByPlanId(@PathVariable Long planId) {
        List<PlanMatInfo> res = planMatInfoMapper.selectAllByPlanid(planId);
        return Result.success(res);
    }
}
