package com.jm.rack.service;

import com.jm.rack.pojo.ESign;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface ESignService extends IService<ESign> {

    List<String> getAllPlcIpAddress();
}
