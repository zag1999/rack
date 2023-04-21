package com.jm.rack.mapper;
import org.apache.ibatis.annotations.Param;

import com.jm.rack.pojo.RackUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Entity com.jm.rack.pojo.RackUser
 */
@Repository
public interface RackUserMapper extends BaseMapper<RackUser> {
    RackUser selectOneByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

}




