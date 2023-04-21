package com.jm.rack.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jm.rack.pojo.ESign;
import com.jm.rack.service.ESignService;
import com.jm.rack.mapper.ESignMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Service
public class ESignServiceImpl extends ServiceImpl<ESignMapper, ESign> implements ESignService {
    @Resource
    private ESignMapper eSignMapper;

    @Override
    public List<String> getAllPlcIpAddress() {
        return eSignMapper.getAllPlcIpAddress();
    }
}




