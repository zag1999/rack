package com.jm.rack.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.jm.rack.pojo.MaterialInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Entity com.jm.rack.pojo.MaterialInfo
 */
@Repository
public interface MaterialInfoMapper extends BaseMapper<MaterialInfo> {
    int deleteByRackInfoId(@Param("rackInfoId") Long rackInfoId);
    List<MaterialInfo> searchAllByRackInfoId(@Param("rackInfoId") Long rackInfoId);

    int updateMatcountByCode(@Param("matcount") Integer matcount, @Param("code") String code);
}




