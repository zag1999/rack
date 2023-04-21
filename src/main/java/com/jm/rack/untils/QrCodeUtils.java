package com.jm.rack.untils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码工具类
 *
 * @author gu
 */
public class QrCodeUtils {

    /**
     * 黑色
     */
    private static final int BLACK = 0xFF000000;
    /**
     * 白色
     */
    private static final int WHITE = 0xFFFFFFFF;
    /**
     * 宽
     */
    private static final int WIDTH = 300;
    /**
     * 高
     */
    private static final int HEIGHT = 200;
    private static final int PIC_HEIGHT = 150;

    /**
     * 二维码传图片
     *
     * @param matrix
     * @return
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        BufferedImage image = new BufferedImage(WIDTH, PIC_HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < PIC_HEIGHT; y++) {
                image.setRGB(x, y, WHITE);
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    /**
     * 生成二维码
     *
     * @param content 扫描二维码的内容
     * @param format  图片格式 jpg
     *                文件
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static BufferedImage generateQrCode(String content, String format) throws Exception {

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        @SuppressWarnings("rawtypes")
        Map hints = new HashMap();
        // 设置UTF-8， 防止中文乱码
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 设置二维码四周白色区域的大小
        hints.put(EncodeHintType.MARGIN, 10);
        // 设置二维码的容错性
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 画二维码
        BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 140, 140, hints);
        BufferedImage image = toBufferedImage(bitMatrix);
        return image;
    }

    /**
     * 把生成的图片写到本地磁盘
     *
     * @param qrcFile       路径
     * @throws Exception
     */
    public static void generateQrCode(File qrcFile,QRCodeInfo qrCodeInfo) throws Exception {


        BufferedImage image = generateQrCode(qrCodeInfo.code, "jpg");

        Graphics g = image.getGraphics();
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //设置字体
        Font font = new Font("宋体", Font.PLAIN, 20);
        g.setFont(font);
        g.setColor(Color.black);
        // FontMetrics metrics = g.getFontMetrics(font);
        // 文字在图片中的坐标 这里设置在中间
//        int startX = (WIDTH - metrics.stringWidth(pressText)) / 2;
//        int startY=HEIGHT+(PIC_HEIGHT-HEIGHT)/2;
//        g.drawString(pressText, startX, startY);
        g.drawString("名称:"+qrCodeInfo.name, 110, 60);
        g.drawString("编码:"+ qrCodeInfo.code,110, 90);
        g.dispose();
        image.flush();
        try {
            ImageIO.write(image, "jpg", qrcFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * test
     *
     * @param
     * @throws Exception
     */
    public  static String makeStr(int s){
        String a = "";
        if (s<10){
            a= "00"+s;
        }else {
            a = "0"+s;}
        return a;
    }

    public static void main(String[] args) throws Exception {
        List<QRCodeInfo> qrCodeInfos = new ArrayList<>();
        //前排 2
//        for (int i = 1; i <= 28; i++) {
//            QRCodeInfo qrCodeInfo = new QRCodeInfo();
//            qrCodeInfo.code = "Q1-"+makeStr(i);
//            qrCodeInfo.name = "前排-01";
//            qrCodeInfos.add(qrCodeInfo);
//        }
//        for (int i = 1; i <= 28; i++) {
//            QRCodeInfo qrCodeInfo = new QRCodeInfo();
//            qrCodeInfo.code = "Q2-"+makeStr(i);
//            qrCodeInfo.name = "前排-02";
//            qrCodeInfos.add(qrCodeInfo);
//        }
//        //中排 3
//        for (int i = 1; i <= 28; i++) {
//            QRCodeInfo qrCodeInfo = new QRCodeInfo();
//            qrCodeInfo.code = "Z1-"+makeStr(i);
//            qrCodeInfo.name = "中排-01";
//            qrCodeInfos.add(qrCodeInfo);
//        }
//        for (int i = 1; i <= 28; i++) {
//            QRCodeInfo qrCodeInfo = new QRCodeInfo();
//            qrCodeInfo.code = "Z2-"+makeStr(i);
//            qrCodeInfo.name = "中排-02";
//            qrCodeInfos.add(qrCodeInfo);
//        }
//        for (int i = 1; i <= 28; i++) {
//            QRCodeInfo qrCodeInfo = new QRCodeInfo();
//            qrCodeInfo.code = "Z3-"+makeStr(i);
//            qrCodeInfo.name = "中排-03";
//            qrCodeInfos.add(qrCodeInfo);
//        }
        for (int i = 1; i <= 28; i++) {
            QRCodeInfo qrCodeInfo = new QRCodeInfo();
            qrCodeInfo.code = "S1-"+makeStr(i);
            qrCodeInfo.name = "三排-01";
            qrCodeInfos.add(qrCodeInfo);
        }
        for (int i = 0; i < qrCodeInfos.size(); i++) {
            makrECode(qrCodeInfos.get(i));
        }
    }

    public static void makrECode(QRCodeInfo qrCodeInfo)  {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        File file1 = new File("E:\\eqrcode\\" + simpleDateFormat.format(date) + "\\" + simpleDateFormat1.format(date));
        try {
            if (file1.mkdirs()) {
                System.out.println(file1.getAbsolutePath());
                File file = new File(file1.getAbsolutePath() + "\\" + qrCodeInfo.code+ ".jpg");
                if (file.createNewFile()) {
                    generateQrCode(file,qrCodeInfo);
                }
            }else {
                File file = new File(file1.getAbsolutePath() + "\\" + qrCodeInfo.code + ".jpg");
                if (file.createNewFile()) {
                    generateQrCode(file,qrCodeInfo);
                }
            }
        }catch (Exception e){

        }
    }


}