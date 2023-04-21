package com.jm.rack.mapper;

import org.apache.ibatis.annotations.Param;

import com.jm.rack.pojo.MatRackInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Entity com.jm.rack.pojo.MatRackInfo
 */
@Repository
public interface MatRackInfoMapper extends BaseMapper<MatRackInfo> {

    int updateMatnumByCode(@Param("matnum") Integer matnum, @Param("code") String code);

    int updateMatNumByOneCode(Map<String, Object> map);

    List<MatRackInfo> selectBymatmin(String code);
}




