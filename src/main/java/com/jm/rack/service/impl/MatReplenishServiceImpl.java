package com.jm.rack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jm.rack.pojo.MatReplenish;
import com.jm.rack.service.MatReplenishService;
import com.jm.rack.mapper.MatReplenishMapper;
import com.jm.rack.service.MaterialInfoService;
import com.jm.rack.untils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class MatReplenishServiceImpl extends ServiceImpl<MatReplenishMapper, MatReplenish>
    implements MatReplenishService{
    @Autowired
    MatReplenishMapper matReplenishMapper;

    @Override
    public Map<String, Object> getList(String rackInfoNo,Long curpage,Long size) {
        Map<String, Object> map = new HashMap<>();
        if (curpage == null || size ==null){
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        Page<MatReplenish> page = new Page<>();
        page.setSize(size);
        page.setCurrent(curpage);
        IPage<MatReplenish>  matReplenishIPage;
        if (CommonUtils.getInstance().IsStringEmpty(rackInfoNo)){
             matReplenishIPage = matReplenishMapper.selectPage(page,null);
        }else {
            QueryWrapper<MatReplenish> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("rackmatno",rackInfoNo);
            matReplenishIPage = matReplenishMapper.selectPage(page,queryWrapper);
        }
        List<MatReplenish> records = matReplenishIPage.getRecords();
        long total = matReplenishIPage.getTotal();
        map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        map.put("list",records);
        map.put("total",total);
        return map;
    }

}




