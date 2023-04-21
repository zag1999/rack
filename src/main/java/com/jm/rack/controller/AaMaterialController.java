package com.jm.rack.controller;

import com.jm.rack.service.impl.AaMaterialServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("Aamaterial/")
public class AaMaterialController {

    @Autowired
    AaMaterialServiceImpl aaMaterialService;

    @RequestMapping(value = "select")
    @ResponseBody
    public Map select() {
        return aaMaterialService.select();
    }




}
