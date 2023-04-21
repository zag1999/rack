package com.jm.rack.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jm.rack.mapper.ESignMapper;
import com.jm.rack.mapper.MatRackInfoMapper;
import com.jm.rack.mapper.MaterialInfoMapper;
import com.jm.rack.pojo.ESign;
import com.jm.rack.pojo.MatRackInfo;
import com.jm.rack.pojo.MaterialInfo;
import com.jm.rack.pojo.RackInfo;
import com.jm.rack.service.MaterialInfoService;
import com.jm.rack.service.RackInfoService;
import com.jm.rack.mapper.RackInfoMapper;
import com.jm.rack.untils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.*;

/**
 *  料架
 */
@Service
public class RackInfoServiceImpl extends ServiceImpl<RackInfoMapper, RackInfo>
    implements RackInfoService{

    @Autowired
    RackInfoMapper rackInfoMapper;

    @Autowired
    MaterialInfoService materialInfoService;

    @Autowired
    MaterialInfoMapper materialInfoMapper;

    @Autowired
    ESignServiceImpl eSignService;

    @Autowired
    ESignMapper eSignMapper;

    @Autowired
    MatRackInfoMapper matRackInfoMapper;

    public Map insert(RackInfo rackInfo){
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.getInstance().IsStringEmpty(rackInfo.getInfoName())
        ||CommonUtils.getInstance().IsIntegerEmpty(rackInfo.getCurrow())||
                CommonUtils.getInstance().IsIntegerEmpty(rackInfo.getColumns())){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        rackInfo.setCreatetime(new Date());
        //查现在有几个料架
        String[] splitStr = rackInfo.getInfoName().split("-");
        String  lastChar = rackInfo.getInfoName().substring(rackInfo.getInfoName().length()-1);
        String  code = "";
        switch (splitStr[0]){
            case "前排":
                code = "Q"+lastChar;
                break;
            case "中排":
                code = "Z"+lastChar;
                break;
            case "三排":
                code = "S"+lastChar;
                break;
        }
        //  Long aLong = rackInfoMapper.selectCount(null)+1;
        try {
            int count = rackInfoMapper.insert(rackInfo);
            if (count>0){
                //设置料位  默认储存最大 100,提示20
                long allPos = rackInfo.getColumns()*rackInfo.getCurrow();
                int col = rackInfo.getColumns();
                List<MaterialInfo> list = new ArrayList<>();
                List<ESign> eSignList = new ArrayList<>();
                for (int i = 0; i < allPos; i++) {
                    int row =  i/col;
                    int cols = i%col+1;
                    Date date = new Date();
                    MaterialInfo materialInfo = new MaterialInfo();
                    materialInfo.setMatno(code+"-"+CommonUtils.getInstance().makeMatNo(i+1));
                    materialInfo.setCreatetime(date);
                    materialInfo.setCurcolumn(cols);
                    materialInfo.setCurrow(row);
                    materialInfo.setRackInfoId(rackInfo.getInfoId());
                    list.add(materialInfo);
                    ESign eSign = new ESign();
                    eSign.setMatno(materialInfo.getMatno());
                    eSign.setComNo("");
                    eSign.setRackid(rackInfo.getInfoId());
//                    eSign.setComChildNo(-1);
                    eSign.setStocksign(0);
                    eSign.setCreatetime(date);
                    ESign eSign1 = new ESign();
                    eSign1.setMatno(materialInfo.getMatno());
                    eSign1.setComNo("");
//                    eSign1.setComChildNo(-1);
                    eSign1.setStocksign(1);
                    eSign1.setRackid(rackInfo.getInfoId());
                    eSign1.setCreatetime(date);
                    eSignList.add(eSign);
                    eSignList.add(eSign1);
                }
                materialInfoService.saveBatch(list);
                eSignService.saveBatch(eSignList);
                map.put("result",CommonUtils.getInstance().RESULT_SUCCESS);
            }else {
                map.put("result",CommonUtils.getInstance().RESULT_fAIL);
                map.put("msg","新增失败");
            }
        }catch (Exception e){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",e.getMessage());
        }
        return map;
    }

    /*料架设置*/
    public Map update(RackInfo rackInfo){
        Map<String, Object> map = new HashMap<>();
        if (rackInfo.getInfoId() == null){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        RackInfo rackInfo1 =  rackInfoMapper.selectById(rackInfo.getInfoId());
        if (rackInfo1 == null){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg","无料架");
            return map;
        }
        long aLong = rackInfoMapper.selectCount(null)+1;
        if (rackInfo.getColumns()!=null&&rackInfo.getCurrow()!=null){
            if (!(rackInfo1.getColumns().equals(rackInfo.getColumns()) &&
                    rackInfo1.getCurrow().equals(rackInfo.getCurrow()))){
                //将料位清空操作,重新编辑料位
                QueryWrapper<MaterialInfo> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("rack_info_id",rackInfo.getInfoId());
                materialInfoMapper.delete(queryWrapper);
                QueryWrapper<ESign> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("rackid",rackInfo.getInfoId());
                eSignMapper.delete(queryWrapper1);
                //重新编辑
                long allPos = rackInfo.getColumns()*rackInfo.getCurrow();
                int col = rackInfo.getColumns();
                List<MaterialInfo> list = new ArrayList<>();
                List<ESign> eSignList = new ArrayList<>();
                for (int i = 0; i < allPos; i++) {
                    int row = i/col;
                    int cols = i%col+1;
                    MaterialInfo materialInfo = new MaterialInfo();
                    Date date =new Date();
                    materialInfo.setMatno(aLong+"-"+CommonUtils.getInstance().makeMatNo(i+1));
                    materialInfo.setCreatetime(date);
                    materialInfo.setCurcolumn(cols);
                    materialInfo.setCurrow(row);
                    materialInfo.setRackInfoId(rackInfo.getInfoId());
                    list.add(materialInfo);
                    ESign eSign = new ESign();
                    eSign.setMatno(materialInfo.getMatno());
                    eSign.setComNo("");
//                    eSign.setComChildNo(-1);
                    eSign.setStocksign(0);
                    eSign.setCreatetime(date);
                    ESign eSign1 = new ESign();
                    eSign1.setMatno(materialInfo.getMatno());
                    eSign1.setComNo("");
//                    eSign1.setComChildNo(-1);
                    eSign1.setStocksign(1);
                    eSign1.setCreatetime(date);
                    eSignList.add(eSign);
                    eSignList.add(eSign1);
                }
                eSignService.saveBatch(eSignList);
                materialInfoService.saveBatch(list);
            }
        }else {
            rackInfo.setColumns(null);
            rackInfo.setCurrow(null);
        }
        rackInfo.setUpdatetime(new Date());
        try {
            int count = rackInfoMapper.updateById(rackInfo);
            if (count>0){
                map.put("result",CommonUtils.getInstance().RESULT_SUCCESS);
            }else {
                map.put("result",CommonUtils.getInstance().RESULT_fAIL);
                map.put("msg","修改失败");
            }
        }catch (Exception e){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",e.getMessage());
        }
        return map;
    }

    /*查询*/
    public Map select(){
        Map<String, Object> map = new HashMap<>();
        try {
            List<RackInfo> rackInfos = rackInfoMapper.selectList(null);
            map.put("result",CommonUtils.getInstance().RESULT_SUCCESS);
            map.put("list",rackInfos);
        }catch (Exception e){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",e.getMessage());
        }
        return map;
    }

    /*删除*/
    public Map delete(Long id){
        Map<String, Object> map = new HashMap<>();
        if (id == null){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        try {
            RackInfo rackInfo = new RackInfo();
            rackInfo.setInfoId(id);
            int res = rackInfoMapper.deleteById(rackInfo);
            int res1=0;
            if (res>0){
                res1 = materialInfoMapper.deleteByRackInfoId(id);
            }
            if (res>0 && res1 >=0){
                map.put("result",CommonUtils.getInstance().RESULT_SUCCESS);
            }else {
                map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            }
        }catch (Exception e){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",e.getMessage());
        }
        return map;
    }
    
    /*查找所有料架下的料位*/
    public Map seletByParent(){
        Map<String, Object> map = new HashMap<>();
        try {
            List<RackInfo> rackInfos = rackInfoMapper.selectList(null);
            List<JSONObject> jsonObjectList = new ArrayList<>();
            for (RackInfo rackInfo:rackInfos
            ) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("label",rackInfo.getInfoName());
                List<JSONObject> jsonObjectList1 = new ArrayList<>();
                List<MaterialInfo> list = materialInfoMapper.searchAllByRackInfoId(rackInfo.getInfoId());
                for (MaterialInfo m:list
                     ) {
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("label",m.getMatno());
                    jsonObjectList1.add(jsonObject1);
                }
                jsonObject.put("options",jsonObjectList1);
                jsonObjectList.add(jsonObject);
            }
            map.put("result",CommonUtils.getInstance().RESULT_SUCCESS);
            map.put("list",jsonObjectList);
        }catch (Exception e){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",e.getMessage());
        }
        return map;
    }

    /*查找所有料架下的料位*/
    public Map seletAllMat(){
        Map<String, Object> map = new HashMap<>();
        try {
            List<RackInfo> rackInfos = rackInfoMapper.selectList(null);
            List<JSONObject> jsonObjectList = new ArrayList<>();
            for (RackInfo rackInfo:rackInfos
            ) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("label",rackInfo.getInfoName());
                List<MaterialInfo> list = materialInfoMapper.searchAllByRackInfoId(rackInfo.getInfoId());
                jsonObject.put("list",list);
                jsonObjectList.add(jsonObject);
            }
            map.put("result",CommonUtils.getInstance().RESULT_SUCCESS);
            map.put("list",jsonObjectList);
        }catch (Exception e){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",e.getMessage());
        }
        return map;
    }



    public Map selectAllMatRack(){
        Map<String, Object> map = new HashMap<>();
        List<RackInfo> rackInfos = rackInfoMapper.selectList(null);
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (RackInfo rackInfo:rackInfos
             ) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("lable",rackInfo.getInfoName());
            List<JSONObject> jsonObjectList1 = new ArrayList<>();
            List<MaterialInfo> list = materialInfoMapper.searchAllByRackInfoId(rackInfo.getInfoId());
            for (MaterialInfo m:list
                 ) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("rackmatno",m.getMatno());
                QueryWrapper<MatRackInfo> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("rackno",m.getMatno());
                List<MatRackInfo> matRackInfos = matRackInfoMapper.selectList(queryWrapper);
                boolean show = false;
                for (MatRackInfo matRackInfo :matRackInfos
                        ) {
                    if (matRackInfo.getMatnum()<matRackInfo.getMatmin()){
                        show = true;
                        break;
                    }
                }
                jsonObject1.put("list",matRackInfos);
                jsonObject1.put("show",show);
                jsonObjectList1.add(jsonObject1);
            }
            jsonObject.put("list",jsonObjectList1);
            jsonObjectList.add(jsonObject);
        }
        map.put("result",CommonUtils.getInstance().RESULT_SUCCESS);
        map.put("list",jsonObjectList);
        return map;
    }

}




