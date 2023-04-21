package com.jm.rack.controller;

import com.jm.rack.mapper.PlanInfoMapper;
import com.jm.rack.untils.CommonUtils;
import com.jm.rack.untils.HslHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("planrun/")
public class PlanRunController {

    @Autowired
    PlanInfoMapper planInfoMapper;


    //线体放行
    @RequestMapping(value = "notify")
    @ResponseBody
    public Map notify(Integer type) {
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.getInstance().IsIntegerEmpty(type)){
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        String no = "";
        switch (type){
            case 0:
                no = CommonUtils.getInstance().frontLineNo;
                break;
            case 1:
                no = CommonUtils.getInstance().middleLineNo;
                break;
            case 2:
                no = CommonUtils.getInstance().lastLineNo;
                break;
        }
        try {
            //线体模块应该是从mes拉取到最新计划后根据对拉取到本地
            //查询当前线体中未开始及执行中的计划数量
            int QCount0 = planInfoMapper.countByStateAndLinetype(0, no);
            int QCount1 = planInfoMapper.countByStateAndLinetype(1, no);
            //当未开始的计划数量!=0,执行中的计划==0时,socket通知要箱子,找计划,刷新前排页面
            //0.0服务器通知plc放行空箱子
            //1.0plc通知服务器plc有空箱子可以放行
            //2.0服务器通知plc生产完成箱子放行
            //3.0plc通知服务器拣料工位是否有空位允许放行箱子
            if (QCount0 != 0 && QCount1 == 0) {
                //socket通知要箱子。找计划，刷新前排页面
                //先去读1.0
                //byte b = 1;
                byte b = HslHelper.getInstance().readByte(HslHelper.lastH, "1", type);
                map.put("result",CommonUtils.getInstance().RESULT_SUCCESS);
                map.put("byteb",b);
            }else {
                map.put("result",CommonUtils.getInstance().RESULT_SUCCESS);
                map.put("byteb",0);
            }
        }catch (Exception e){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",e.getMessage());
        }
        return map;
    }


}
