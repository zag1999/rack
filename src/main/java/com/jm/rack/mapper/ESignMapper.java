package com.jm.rack.mapper;
import org.apache.ibatis.annotations.Param;

import com.jm.rack.pojo.ESign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Entity com.jm.rack.pojo.ESign
 */
@Repository
public interface ESignMapper extends BaseMapper<ESign> {

    int updateComNoAndComChildNoByEid(@Param("comNo") String comNo, @Param("comChildNo") Integer comChildNo, @Param("eid") Long eid);

    int updateComNoAndComChildNoByMatnoAndStocksign(@Param("comNo") String comNo, @Param("comChildNo") Integer comChildNo, @Param("matno") String matno, @Param("stocksign") Integer stocksign);

    List<String> getAllPlcIpAddress();
}




