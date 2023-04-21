package com.jm.rack.untils;


import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommonUtils {
    public String RESULT_SUCCESS = "success";
    public String RESULT_fAIL = "fail";
    public String PARAM_LOSE = "参数缺失";

    //前排中排后排线体编号
    public String frontLineNo = "6283";
    public String middleLineNo = "6285";
    public String lastLineNo = "6287";

    /*MultipartFile 文件转File 临时地址*/
    public String tempFileAddress = "D://excel//";


    public SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public ExecutorService executorService;


    public ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(20);
        }
        return executorService;
    }

    private CommonUtils() {

    }

    private static class CommonUtilsInstance {
        private static final CommonUtils INSTANCE = new CommonUtils();
    }

    public static CommonUtils getInstance() {
        return CommonUtilsInstance.INSTANCE;
    }

    public boolean IsStringEmpty(String str) {
        boolean result = false;
        if (str == null || "".equals(str)) {
            result = true;
        }
        return result;
    }

    public boolean IsIntegerEmpty(Integer arg) {
        boolean result = false;
        if (arg == null) {
            result = true;
        }
        return result;
    }

    public int getCarNo(int count) {
        double d1 = count;
        double d2 = 80;
        double result = d1 / d2;
        return (int) Math.ceil(result);
    }

    public static long makePrimaryKey() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = simpleDateFormat.format(new Date());
        StringBuilder result = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            result.append(random.nextInt(10));
        }
        return Long.parseLong(newDate + result.toString());
    }

    public Long makeLongData(String value) {
        long a = 0;
        if (IsStringEmpty(value)) {
            return a;
        } else {
            value = value.replaceAll(",", "");
            float b = Float.parseFloat(value);
            a = (long) b;
            return a;
        }
    }


    private String makeNo(Long no) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String str = "";
        if (no >= 0 && no < 10) {
            str = "000" + no;
        } else if (no >= 10 && no < 100) {
            str = "00" + no;
        } else if (no >= 100 && no < 1000) {
            str = "0" + no;
        } else {
            str = no + "";
        }
        return "BS" + simpleDateFormat.format(new Date()) + str;
    }


    public String makeMatNo(int no) {
        String str = "";
        if (no >= 0 && no < 10) {
            str = "00" + no;
        } else if (no >= 10 && no < 100) {
            str = "0" + no;
        } else {
            str = no + "";
        }
        return str;
    }


    /**
     * @author
     * @date: 2019年
     * 文字转语音并生成语音文件方法
     * input：	data：需要转的文字对象，path：语音文件保存位置对象
     */
    public static void readSpeech(String data) {
        // 创建一个ActiveXComponent对象，指定使用Windows系统自带的语音合成技术
        ActiveXComponent sap = new ActiveXComponent("Sapi.SpVoice");
        // 获取Dispatch对象
        Dispatch sapo = sap.getObject();
        try {
            // 设置音量 0-100
            sap.setProperty("Volume", new Variant(100));
            // 设置语音朗读速度 -10 到 +10
            sap.setProperty("Rate", new Variant(-4));

            // 获取默认语音
            Variant defalutVoice = sap.getProperty("Voice");
            // 获取所有语音
            Variant allVoices = Dispatch.call(sapo, "GetVoices");
            // 获取Dispatch对象
            Dispatch dispVoices = allVoices.toDispatch();
            // 指定使用第一个语音
            Dispatch setvoice = Dispatch.call(dispVoices, "Item", new Variant(1)).toDispatch();
            // 创建ActiveXComponent对象
            ActiveXComponent voiceActivex = new ActiveXComponent(defalutVoice.toDispatch());
            ActiveXComponent setvoiceActivex = new ActiveXComponent(setvoice);
            // 获取语音描述
            Variant item = Dispatch.call(setvoiceActivex, "GetDescription");
            // 执行朗读
            Dispatch.call(sapo, "Speak", new Variant(data));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            sapo.safeRelease();
            sap.safeRelease();
        }
    }


    public static void textToSpeech(String data, String path) {
        ActiveXComponent ax = null;
        try {
            ax = new ActiveXComponent("Sapi.SpVoice");

            // 运行时输出语音内容
            Dispatch spVoice = ax.getObject();
            // 音量 0-100
            ax.setProperty("Volume", new Variant(100));
            // 语音朗读速度 -10 到 +10
            ax.setProperty("Rate", new Variant(-2));
            // 执行朗读
            Dispatch.call(spVoice, "Speak", new Variant(data));

            // 下面是构建文件流把生成语音文件
            ax = new ActiveXComponent("Sapi.SpFileStream");
            Dispatch spFileStream = ax.getObject();

            ax = new ActiveXComponent("Sapi.SpAudioFormat");
            Dispatch spAudioFormat = ax.getObject();

            // 设置音频流格式
            Dispatch.put(spAudioFormat, "Type", new Variant(22));
            // 设置文件输出流格式
            Dispatch.putRef(spFileStream, "Format", spAudioFormat);
            // 调用输出 文件流打开方法，创建一个.wav文件
            Dispatch.call(spFileStream, "Open", new Variant(path + "/voice.wav"), new Variant(3), new Variant(true));
            // 设置声音对象的音频输出流为输出文件对象
            Dispatch.putRef(spVoice, "AudioOutputStream", spFileStream);
            // 设置音量 0到100
            Dispatch.put(spVoice, "Volume", new Variant(100));
            // 设置朗读速度
            Dispatch.put(spVoice, "Rate", new Variant(-4));
            // 开始朗读
            Dispatch.call(spVoice, "Speak", new Variant(data));
            // 关闭输出文件
            Dispatch.call(spFileStream, "Close");
            Dispatch.putRef(spVoice, "AudioOutputStream", null);

            spAudioFormat.safeRelease();
            spFileStream.safeRelease();
            spVoice.safeRelease();
            ax.safeRelease();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static File multipartFileToFile(@RequestParam MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = file.getInputStream();
            toFile = new File(CommonUtils.getInstance().tempFileAddress + file.getOriginalFilename());
            if (!toFile.getParentFile().exists()) {
                toFile.getParentFile().mkdirs();
            }
            inputStreamToFile(ins, toFile);
        }
        return toFile;

    }

    /**
     * File 转 MultipartFile
     *
     * @param file
     * @throws Exception
     */
//    public static void fileToMultipartFile( File file ) throws Exception {
//
//        FileInputStream fileInput = new FileInputStream(file);
//        MultipartFile toMultipartFile = new MockMultipartFile("file",file.getName(),"text/plain", IOUtils.toByteArray(fileInput));
//        toMultipartFile.getInputStream();
//
//    }
    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
