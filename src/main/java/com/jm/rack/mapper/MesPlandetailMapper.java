package com.jm.rack.mapper;
import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jm.rack.pojo.MesPlandetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Entity com.jm.rack.pojo.MesPlandetail
 */
@Repository
@DS("sqlserver")
public interface MesPlandetailMapper extends BaseMapper<MesPlandetail> {
    List<MesPlandetail> selectMesPlanDetailsByLineNo(String planno);
}




