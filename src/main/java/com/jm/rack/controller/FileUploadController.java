package com.jm.rack.controller;

import com.jm.rack.mapper.OutmaintainMapper;
import com.jm.rack.pojo.MatRackInfo;
import com.jm.rack.pojo.Outmaintain;
import com.jm.rack.service.OutmaintainService;
import com.jm.rack.untils.CommonUtils;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Log4j2
@Controller
public class FileUploadController {

    @Autowired
    OutmaintainService outmaintainService;

    @Autowired
    OutmaintainMapper outmaintainMapper;

    @RequestMapping("outExcel")
    @ResponseBody
    public Map<String,Object> importExcel(@RequestParam("file") MultipartFile file) throws IOException, InterruptedException {
        Map<String,Object> map =new HashMap<>();
        if (file.isEmpty()){
            map.put("result","fail");
            map.put("msg","文件不能为空");
            return map;
        }
        try {
            File file1 = CommonUtils.multipartFileToFile(file);
            try {
                Workbook workbook = null;
                //使用字符流接File的数据
                FileInputStream fileInputStream = new FileInputStream(file1);
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
                List<Outmaintain> outmaintains = new ArrayList<>();
                Date date = new Date();
                for (int i = 1; i < rows; i++) {
                    //循环得到每一行的单元格对象
                    Cell[] item = readfirst.getRow(i);
                    Outmaintain outmaintain = new Outmaintain();
                    outmaintain.setProno(item[0].getContents());
                    outmaintain.setMatname(item[2].getContents());
                    outmaintain.setMatcode(item[1].getContents());
                    outmaintain.setCount(Integer.parseInt(item[3].getContents()));
                    outmaintain.setCreatrtime(date);
                    outmaintains.add(outmaintain);
                }
                outmaintainMapper.delete(null);
                outmaintainService.saveBatch(outmaintains);
                fileInputStream.close();
                File myObj = new File(file1.getAbsolutePath());
                if (myObj.delete()){}
                map.put("result", CommonUtils.getInstance().RESULT_SUCCESS);
            } catch (Exception e) {
                map.put("result",CommonUtils.getInstance().RESULT_fAIL);
                map.put("msg",e.getMessage());
            }
        } catch (Exception e) {
            map.put("result", CommonUtils.getInstance().RESULT_fAIL);
            map.put("msg", e.getMessage());
        }
        return map;
    }

}
