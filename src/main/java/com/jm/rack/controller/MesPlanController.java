package com.jm.rack.controller;


import com.jm.rack.mapper.MesPlanMapper;
import com.jm.rack.pojo.MesPlan;
import com.jm.rack.service.MesPlanService;
import com.jm.rack.service.impl.MesPlanServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/*工厂内网数据库 计划表*/
@Controller
@RequestMapping("mesplan/")
public class MesPlanController {
    @Autowired
    MesPlanServiceImpl mesPlanService;



    @RequestMapping(value = "selectMesPlanByLineNo")
    @ResponseBody
    public Map selectMesPlanByLineNo(Integer type) {
        return mesPlanService.selectMesPlanByLineNo(type);
    }

    @RequestMapping(value = "selectPlanDetailByProNo")
    @ResponseBody
    public Map selectMesPlanByLineNo(String prono) {
        return mesPlanService.selectPlanDetailByProNo(prono);
    }
}







