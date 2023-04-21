package com.jm.rack.service;

import com.jm.rack.pojo.MatReplenish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 *
 */
public interface MatReplenishService extends IService<MatReplenish> {
    Map<String, Object> getList(String rackInfoNo,Long curpage,Long size);
}
