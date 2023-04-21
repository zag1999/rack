package com.jm.rack.service;

import com.jm.rack.pojo.AaBom;
import com.jm.rack.pojo.MesPlan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jm.rack.pojo.MesPlandetail;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface MesPlanService extends IService<MesPlan> {
    Map selectMesPlanByLineNo(Integer type);

    Map selectPlanDetailByProNo(String prono);

    List<MesPlandetail> getMesPlanByLineNo(Integer type);

    List<AaBom> getBomByProNo( String prono);

}
