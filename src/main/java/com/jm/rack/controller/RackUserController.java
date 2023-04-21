package com.jm.rack.controller;


import com.jm.rack.pojo.RackUser;
import com.jm.rack.service.impl.RackUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@Transactional
@RequestMapping("rackuser/")
public class RackUserController {

    @Autowired
    RackUserServiceImpl rackUserService;

    @RequestMapping(value = "insert")
    @ResponseBody
    public Map insert(RackUser rackUser) {
        return rackUserService.insert(rackUser);
    }

    @RequestMapping(value = "update")
    @ResponseBody
    public Map update(RackUser rackUser) {
        return rackUserService.update(rackUser);
    }

    @RequestMapping(value = "select")
    @ResponseBody
    public Map select() {
        return rackUserService.select();
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public Map delete(Long id) {
        return rackUserService.delete(id);
    }

    @RequestMapping(value = "login")
    @ResponseBody
    public Map login(String username,String password) {
        return rackUserService.login(username,password);
    }
}
