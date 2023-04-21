package com.jm.rack.fl.plcConnect.controller;

import com.jm.rack.fl.common.bo.Result;
import com.jm.rack.fl.plcConnect.service.PlcOperateService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/plc-operator")
public class PlcOperateController {
    @Resource
    private PlcOperateService plcOperateService;

    /**
     * 根据类型让灯全亮
     *
     * @param type
     * @return
     */
    @PostMapping("/all-light-on/{type}")
    public Result<Integer> allLightOn(@PathVariable String type) {
        int res = plcOperateService.allLightOn(type);
        return Result.success(res);
    }

    /**
     * 根据类型让灯全灭
     *
     * @param type
     * @return
     */
    @PostMapping("/all-light-off/{type}")
    public Result<Integer> allLightOff(@PathVariable String type) {
        int res = plcOperateService.allLightOff(type);
        return Result.success(res);
    }

    /**
     * 复位
     *
     * @param type
     * @return
     */
    @PostMapping("/reset/{type}")
    public Result<Integer> reset(@PathVariable String type) {
        int res = plcOperateService.reset(type);
        return Result.success(res);
    }
}
