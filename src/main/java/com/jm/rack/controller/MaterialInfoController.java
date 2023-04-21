package com.jm.rack.controller;

import com.jm.rack.mapper.ESignMapper;
import com.jm.rack.mapper.MatRackInfoMapper;
import com.jm.rack.pojo.ESign;
import com.jm.rack.pojo.MatRackInfo;
import com.jm.rack.pojo.MaterialInfo;
import com.jm.rack.pojo.RackInfo;
import com.jm.rack.service.MatRackInfoService;
import com.jm.rack.service.impl.MaterialInfoServiceImpl;
import com.jm.rack.service.impl.RackInfoServiceImpl;
import com.jm.rack.untils.CommonUtils;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@Controller
@Transactional
@RequestMapping("material/")
public class MaterialInfoController {
    @Autowired
    MaterialInfoServiceImpl materialInfoService;
    @Autowired
    MatRackInfoMapper matRackInfoMapper;
    @Autowired
    MatRackInfoService matRackInfoService;

    @Autowired
    ESignMapper eSignMapper;


    @RequestMapping(value = "selectAllBycode")
    @ResponseBody
    public Map selectAllBycode(Long id) {
        return materialInfoService.selectAllBycode(id);
    }

    @RequestMapping(value = "scanRecognition")
    @ResponseBody
    public Map scanRecognition(String code) {
        return materialInfoService.scanRecognition(code);
    }

    @RequestMapping(value = "scanWarehouse")
    @ResponseBody
    public Map scanWarehouse(String code,Integer count,String staffname) {
        return materialInfoService.scanWarehouse(code,count,staffname);
    }

    @RequestMapping(value = "insertMatRack")
    @ResponseBody
    public Map insertMatRack(MatRackInfo matRackInfo) {
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.getInstance().IsStringEmpty(matRackInfo.getRackno())||
                CommonUtils.getInstance().IsStringEmpty(matRackInfo.getCode())||
                CommonUtils.getInstance().IsStringEmpty(matRackInfo.getName())||
                CommonUtils.getInstance().IsIntegerEmpty(matRackInfo.getMatnum())||
                CommonUtils.getInstance().IsIntegerEmpty(matRackInfo.getMatmax())||
                CommonUtils.getInstance().IsIntegerEmpty(matRackInfo.getMatmin())
        ){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        try {
            matRackInfo.setCreatetime(new Date());
            matRackInfoMapper.insert(matRackInfo);
            map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        }catch (Exception e){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",e.getMessage());
        }
        return map;
    }

    @RequestMapping(value = "delMatRack")
    @ResponseBody
    public Map delMatRack(MatRackInfo matRackInfo) {
        Map<String, Object> map = new HashMap<>();
        if (matRackInfo.getMrid() == null
        ){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        try {
            matRackInfoMapper.deleteById(matRackInfo);
            map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        }catch (Exception e){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",e.getMessage());
        }
        return map;
    }


    @RequestMapping(value = "update")
    @ResponseBody
    public Map update(MatRackInfo matRackInfo) {
        Map<String, Object> map = new HashMap<>();
        if (matRackInfo.getMrid() == null
        ){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        try {
            matRackInfoMapper.updateById(matRackInfo);
            map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        }catch (Exception e){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",e.getMessage());
        }
        return map;
    }


    /*支持excel导入*/
    @RequestMapping(value = "importDataByExcel")
    @ResponseBody
    public Map export(String file) {
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.getInstance().IsStringEmpty(file)
        ){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        File Inputfile = new File(file);
        try {
            Workbook workbook = null;
            //使用字符流接File的数据
            FileInputStream fileInputStream = new FileInputStream(Inputfile);
            //workbook去接fileInputStream,读取到excel文件
            workbook = Workbook.getWorkbook(fileInputStream);
            //判断是哪一个工作簿，用Sheet类
            Sheet readfirst = workbook.getSheet(0);
            //获取有效行数
            int rows = readfirst.getRows();
            //获取有效列数
            int clomns = readfirst.getColumns();
            System.out.println("row:" + rows);
            System.out.println("clomns:" + clomns);
            List<MatRackInfo> matRackInfos = new ArrayList<>();
            Date date = new Date();
            for (int i = 1; i < rows; i++) {
                //循环得到每一行的单元格对象
                Cell[] item = readfirst.getRow(i);
                MatRackInfo matRackInfo = new MatRackInfo();
                matRackInfo.setRackno(item[0].getContents());
                matRackInfo.setCreatetime(date);
                matRackInfo.setCode(item[1].getContents());
                matRackInfo.setName(item[2].getContents());
                matRackInfo.setMatmax(Integer.parseInt(item[3].getContents()));
                matRackInfo.setMatmin(Integer.parseInt(item[4].getContents()));
                matRackInfo.setMatnum(0);
                matRackInfos.add(matRackInfo);
            }
            matRackInfoMapper.delete(null);
            matRackInfoService.saveBatch(matRackInfos);
            map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        } catch (Exception e) {
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",e.getMessage());
        }
        return map;
    }



    /*电子标签*/
    /*支持excel导入*/
    @RequestMapping(value = "importESignByExcel")
    @ResponseBody
    public Map exportESign(String file) {
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.getInstance().IsStringEmpty(file)
        ){
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",CommonUtils.getInstance().PARAM_LOSE);
            return map;
        }
        File Inputfile = new File(file);
        try {
            Workbook workbook = null;
            //使用字符流接File的数据
            FileInputStream fileInputStream = new FileInputStream(Inputfile);
            //workbook去接fileInputStream,读取到excel文件
            workbook = Workbook.getWorkbook(fileInputStream);
            //判断是哪一个工作簿，用Sheet类
            Sheet readfirst = workbook.getSheet(0);
            //获取有效行数
            int rows = readfirst.getRows();
            //获取有效列数
            int clomns = readfirst.getColumns();
            System.out.println("row:" + rows);
            System.out.println("clomns:" + clomns);
            List<MatRackInfo> matRackInfos = new ArrayList<>();
            Date date = new Date();
            for (int i = 1; i < rows; i++) {
                //循环得到每一行的单元格对象
                Cell[] item = readfirst.getRow(i);
                //
                String code =  item[0].getContents();
                String com0 = item[1].getContents();
                Integer comc0 = Integer.parseInt(item[2].getContents());
                String com1 = item[3].getContents();
                Integer comc1 = Integer.parseInt(item[4].getContents());
                eSignMapper.updateComNoAndComChildNoByMatnoAndStocksign(com0,comc0,code,0);
                eSignMapper.updateComNoAndComChildNoByMatnoAndStocksign(com1,comc1,code,1);

            }

            map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
        } catch (Exception e) {
            map.put("result",CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg",e.getMessage());
        }
        return map;
    }

}
