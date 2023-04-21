package com.jm.rack.controller;

import com.jm.rack.mapper.OverPointPlanMapper;
import com.jm.rack.pojo.OverPointPlan;
import com.jm.rack.untils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("opplan/")

public class OverPointPlanController {

    @Autowired
    OverPointPlanMapper overPointPlanMapper;

    @RequestMapping(value = "selectone")
    @ResponseBody
    public Map selectone() {
        Map<String, Object> map = new HashMap<>();
        try {
            List<OverPointPlan> overPointPlans = overPointPlanMapper.selectList(null);
            if (overPointPlans!=null && overPointPlans.size()>0){
                map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
                map.put("overplan",overPointPlans.get(0).getOverPointSign());
                return map;
            }else {
                map.put("result", CommonUtils.getInstance().RESULT_fAIL);
                map.put("overplan",0);
                return map;
            }

        }catch (Exception e){
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("overplan",0);
            return map;
        }

    }


    @RequestMapping(value = "update")
    @ResponseBody
    public Map update(Byte sign) {
        Map<String, Object> map = new HashMap<>();
        if (sign == null){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        try {
            int i = overPointPlanMapper.updateOverPointSign(sign);
            if (i>=0){
                map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
            }else {
                map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            }
        }catch (Exception e){
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("overplan",0);
        }
        return map;
    }




}
