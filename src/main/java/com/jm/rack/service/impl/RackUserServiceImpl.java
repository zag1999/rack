package com.jm.rack.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jm.rack.pojo.RackUser;
import com.jm.rack.service.RackUserService;
import com.jm.rack.mapper.RackUserMapper;
import com.jm.rack.untils.CommonUtils;
import com.jm.rack.untils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.provider.MD5;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class RackUserServiceImpl extends ServiceImpl<RackUserMapper, RackUser>
    implements RackUserService{

    @Autowired
    RackUserMapper rackUserMapper;

    /*查*/
    public Map<String, Object> select(){
        Map<String, Object> map = new HashMap<>();
        List<RackUser> rackUsers = rackUserMapper.selectList(null);
        map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        map.put("list",rackUsers);
        return map;
    }

    public Map<String, Object> insert(RackUser rackUser){
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.getInstance().IsStringEmpty(rackUser.getUsername())||
                CommonUtils.getInstance().IsStringEmpty(rackUser.getPassword())||

                CommonUtils.getInstance().IsStringEmpty(rackUser.getSecondlevel())){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        rackUser.setCreatetime(new Date());
        rackUser.setPassword(MD5Util.encrypt(rackUser.getPassword()));
        rackUser.setSecondlevel(MD5Util.encrypt(rackUser.getSecondlevel()));
        RackUser rackUser1 =  rackUserMapper.selectOneByUsernameAndPassword(rackUser.getUsername(),rackUser.getPassword());
        if (rackUser1 != null){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg","已存在此用户，无需注册");
            return map;
        }
        int res = rackUserMapper.insert(rackUser);
        if (res>0){
            map.put("result",CommonUtils.getInstance().RESULT_SUCCESS);
            map.put("user",rackUser);
        }else {
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg","新增失败");
        }
        return map;
    }

    public Map<String, Object> update(RackUser rackUser){
        Map<String, Object> map = new HashMap<>();
        if (rackUser.getUserid() == null){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        if (!CommonUtils.getInstance().IsStringEmpty(rackUser.getPassword())){
            rackUser.setPassword(MD5Util.encrypt(rackUser.getPassword()));
        }
        if (!CommonUtils.getInstance().IsStringEmpty(rackUser.getSecondlevel())){
            rackUser.setSecondlevel(MD5Util.encrypt(rackUser.getSecondlevel()));
        }
        int res = rackUserMapper.updateById(rackUser);
        if (res>0){
            map.put("result",CommonUtils.getInstance().RESULT_SUCCESS);
        }else {
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg","修改失败");
        }
        return map;
    }

    public Map<String, Object> delete(Long id){
        Map<String, Object> map = new HashMap<>();
        if (id== null){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }

        int res = rackUserMapper.deleteById(id);
        if (res>0){
            map.put("result",CommonUtils.getInstance().RESULT_SUCCESS);
        }else {
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg","删除失败");
        }
        return map;
    }

    public Map<String, Object> login(String username,String password){
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.getInstance().IsStringEmpty(username)||
                CommonUtils.getInstance().IsStringEmpty(password)
        ){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        RackUser rackUser =  rackUserMapper.selectOneByUsernameAndPassword(username,MD5Util.encrypt(password));
        if (rackUser != null ){
            map.put("result",CommonUtils.getInstance().RESULT_SUCCESS);
            map.put("user",rackUser);
        }else {
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg","登录失败，账户名或密码错误");
        }
        return map;
    }



}




