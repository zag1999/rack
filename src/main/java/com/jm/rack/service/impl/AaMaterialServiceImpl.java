package com.jm.rack.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jm.rack.pojo.AaMaterial;
import com.jm.rack.service.AaMaterialService;
import com.jm.rack.mapper.AaMaterialMapper;
import com.jm.rack.untils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
@DS("sqlserver")
public class AaMaterialServiceImpl extends ServiceImpl<AaMaterialMapper, AaMaterial>
    implements AaMaterialService{

    @Autowired
    AaMaterialMapper aaMaterialMapper;

    //如果加事务
   // @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Map select(){
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<AaMaterial> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("MatNo","MatName");
        List<AaMaterial> aaMaterials = aaMaterialMapper.selectList(queryWrapper);
        map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        map.put("list",aaMaterials);
        return map;
    }

}




