package com.jm.rack.controller;

import com.jm.rack.service.impl.MatReplenishServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@Transactional
@RequestMapping("matreplenish/")
public class MatReplenishController {
    @Autowired
    MatReplenishServiceImpl matReplenishService;

    @RequestMapping(value = "selectByPage")
    @ResponseBody
    public Map scanWarehouse(String rackInfoNo,Long curpage,Long size) {
        return matReplenishService.getList(rackInfoNo,curpage,size);
    }
}
