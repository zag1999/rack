package com.jm.rack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jm.rack.mapper.AaBomMapper;
import com.jm.rack.mapper.MesPlandetailMapper;
import com.jm.rack.mapper.StaSeatMapper;
import com.jm.rack.pojo.AaBom;
import com.jm.rack.pojo.MesPlan;
import com.jm.rack.pojo.MesPlandetail;
import com.jm.rack.service.MesPlanService;
import com.jm.rack.mapper.MesPlanMapper;
import com.jm.rack.untils.CommonUtils;
import com.sun.el.lang.ELArithmetic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */
@Service
public class MesPlanServiceImpl extends ServiceImpl<MesPlanMapper, MesPlan>
    implements MesPlanService{

    @Autowired
    MesPlanMapper mesPlanMapper;
    @Autowired
    MesPlandetailMapper mesPlandetailMapper;

    @Autowired
    AaBomMapper aaBomMapper;

    @Autowired
    StaSeatMapper staSeatMapper;
    public SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd ");
    @Override
    public Map selectMesPlanByLineNo(Integer type) {
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.getInstance().IsIntegerEmpty(type)){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        String LineNo ="";
        if (type==1){
            LineNo = CommonUtils.getInstance().middleLineNo;
        }else if (type==2){
            LineNo = CommonUtils.getInstance().lastLineNo;
        }else {
            LineNo = CommonUtils.getInstance().frontLineNo;
        }
        Map<String,String> stringMap = new HashMap<>();
        stringMap.put("lineno",LineNo);
        String format = ymdFormat.format(new Date());
        stringMap.put("time",format+"00:00:00.000");
        List<MesPlan> mesPlans = mesPlanMapper.selectMesPlanByLineName(stringMap);
        List<MesPlandetail> allPlan = new ArrayList<>();
        for (MesPlan mesPlan:mesPlans
             ) {
            List<MesPlandetail> mesPlandetails = mesPlandetailMapper.selectMesPlanDetailsByLineNo(mesPlan.getPlanno());
            for (MesPlandetail m:mesPlandetails
                 ) {
                if (m.getUpdatetime() == null){
                    m.setUpdatetime(m.getProducttime());
                }
                m.setUpdatetimeformat(CommonUtils.getInstance().ymdFormat.format(m.getUpdatetime()));
                allPlan.add(m);
            }
        }
        //升序排列
        Collections.sort(allPlan, new Comparator<MesPlandetail>() {
            public int compare(MesPlandetail s1, MesPlandetail s2) {
                return s1.getUpdatetime().compareTo(s2.getUpdatetime());
            }
        });
        //先找车身号
        List<String> stringList = new ArrayList<>();
        for (MesPlandetail m:allPlan
             ) {
            if (!stringList.contains(m.getCarvin())){
                stringList.add(m.getCarvin());
            }
        }
        //List<>
        List<MesPlandetail> lastList = new ArrayList<>();
        for (int i = 0; i < stringList.size(); i++) {
            String code = stringList.get(i);
            List<MesPlandetail> mList = new ArrayList<>();
            for (MesPlandetail m :allPlan
                 ) {
                if (code.equals(m.getCarvin())){
                    mList.add(m);
                }
            }

            if (mList.size()==2){
                lastList.add(mList.get(1));
                lastList.add(mList.get(0));
            }else {
                lastList.add(mList.get(0));
            }
        }
        map.put("result",CommonUtils.getInstance().RESULT_SUCCESS);
        map.put("list",lastList);
        return map;
    }

    @Override
    public Map selectPlanDetailByProNo(String prono) {
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.getInstance().IsStringEmpty(prono)){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        QueryWrapper<AaBom> aaBomQueryWrapper = new QueryWrapper<>();
        aaBomQueryWrapper.eq("ProNo",prono);
        List<AaBom> aaBoms = aaBomMapper.selectList(aaBomQueryWrapper);
        map.put("result",CommonUtils.getInstance().RESULT_SUCCESS);
        map.put("list",aaBoms);
        return map;
    }

    @Override
    public List<MesPlandetail> getMesPlanByLineNo(Integer type) {
        String LineNo ="";
        if (type==1){
            LineNo = CommonUtils.getInstance().middleLineNo;
        }else if (type==2){
            LineNo = CommonUtils.getInstance().lastLineNo;
        }else {
            LineNo = CommonUtils.getInstance().frontLineNo;
        }
        //查大于等于今天的计划
        Map<String,String> stringMap = new HashMap<>();
        stringMap.put("lineno",LineNo);
        String format = ymdFormat.format(new Date());
        stringMap.put("time",format+"00:00:00.000");
        List<MesPlan> mesPlans = mesPlanMapper.selectMesPlanByLineName(stringMap);
        List<MesPlandetail> allPlan = new ArrayList<>();
        for (MesPlan mesPlan:mesPlans
        ) {
            List<MesPlandetail> mesPlandetails = mesPlandetailMapper.selectMesPlanDetailsByLineNo(mesPlan.getPlanno());
            for (MesPlandetail m:mesPlandetails
            ) {
                //去seat表里查是否修改了时间
                if (m.getUpdatetime() == null){
                    m.setUpdatetime(m.getProducttime());
                }
                m.setUpdatetimeformat(CommonUtils.getInstance().ymdFormat.format(m.getUpdatetime()));
                allPlan.add(m);
            }
        }
        //升序排列
        Collections.sort(allPlan, new Comparator<MesPlandetail>() {
            public int compare(MesPlandetail s1, MesPlandetail s2) {
                return s1.getUpdatetime().compareTo(s2.getUpdatetime());
            }
        });
        //先找车身号
        List<String> stringList = new ArrayList<>();
        for (MesPlandetail m:allPlan
        ) {
            if (!stringList.contains(m.getCarvin())){
                stringList.add(m.getCarvin());
            }
        }
        //List<>
        List<MesPlandetail> lastList = new ArrayList<>();
        for (int i = 0; i < stringList.size(); i++) {
            String code = stringList.get(i);
            List<MesPlandetail> mList = new ArrayList<>();
            for (MesPlandetail m :allPlan
            ) {
                if (code.equals(m.getCarvin())){
                    mList.add(m);
                }
            }

            if (mList.size()==2){
                lastList.add(mList.get(1));
                lastList.add(mList.get(0));
            }else {
                lastList.add(mList.get(0));
            }
        }
        return lastList;
    }

    @Override
    public List<AaBom> getBomByProNo(String prono) {
        QueryWrapper<AaBom> aaBomQueryWrapper = new QueryWrapper<>();
        aaBomQueryWrapper.eq("ProNo",prono);
        List<AaBom> aaBoms = aaBomMapper.selectList(aaBomQueryWrapper);
        return aaBoms;
    }




}




