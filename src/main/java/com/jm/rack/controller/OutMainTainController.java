package com.jm.rack.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jm.rack.mapper.OutmaintainMapper;
import com.jm.rack.pojo.Outmaintain;
import com.jm.rack.untils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("outmaintain/")
public class OutMainTainController {

    @Autowired
    OutmaintainMapper outmaintainMapper;


    @RequestMapping(value = "select")
    @ResponseBody
    public Map select(String prono) {
        Map<String, Object> map = new HashMap<>();
        List<Outmaintain> outmaintains;
        if (CommonUtils.getInstance().IsStringEmpty(prono)){
            outmaintains  = outmaintainMapper.selectList(null);
        }else {
            QueryWrapper<Outmaintain> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("prono",prono);
            outmaintains = outmaintainMapper.selectList(queryWrapper);
        }
        map.put("result",CommonUtils.getInstance().RESULT_SUCCESS);
        map.put("list",outmaintains);
        return map;
    }

    @RequestMapping(value = "updatecount")
    @ResponseBody
    public Map updatecount(Outmaintain outmaintain) {
        Map<String, Object> map = new HashMap<>();
        if (outmaintain.getOutid() == null){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        if (CommonUtils.getInstance().IsIntegerEmpty(outmaintain.getCount())){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        try {
            int i = outmaintainMapper.updateById(outmaintain);
            if (i>0){
                map.put("result",CommonUtils.getInstance().RESULT_SUCCESS);
            }else {
                map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            }
        }catch (Exception e){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",e.getMessage());
        }
        return map;
    }

}
