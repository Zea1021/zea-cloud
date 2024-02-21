package com.zea.cloud.common.utils;

import cn.hutool.extra.qrcode.QrConfig;
import lombok.Getter;

import java.io.*;

public class QrCodeUtil {

    private static final String IMG_PATH = "img/qrcode/resource/zea.png";
    private static final String OUTPUT_PATH = "qrcode/qrcode.png";

    public static void main(String[] args) {
        String content = "https://www.baidu.com";
        generateQrCode(content, QrCodeStyleEnum.SIZE_600_600);

        OutputStream out = new ByteArrayOutputStream();
        generateQrCode(content, QrCodeStyleEnum.SIZE_600_600, out);
        System.out.println(out);
    }

    private static QrConfig getQrCodeConfig(QrCodeStyleEnum qrCodeStyleEnum) {
        return new QrConfig(qrCodeStyleEnum.getWidth(), qrCodeStyleEnum.getHeight());
    }

    // 生成二维码，保存到默认路径
    public static void generateQrCode(String content, QrCodeStyleEnum qrEnum) {
        generateQrCode(content, qrEnum, OUTPUT_PATH);
    }

    // 生成二维码，保存到指定路径
    public static void generateQrCode(String content, QrCodeStyleEnum qrEnum, String filePath) {
        // 保存到指定路径
        QrConfig config = getQrCodeConfig(qrEnum);
        cn.hutool.extra.qrcode.QrCodeUtil.generate(content, config, new File(filePath));
    }

    // 生成二维码到输出流
    public static void generateQrCode(String content, QrCodeStyleEnum qrEnum, OutputStream out) {
        QrConfig config = getQrCodeConfig(qrEnum);
        config.setImg(IMG_PATH);
        cn.hutool.extra.qrcode.QrCodeUtil.generate(content, config, "svg", out);
    }

    @Getter
    public enum QrCodeStyleEnum {
        SIZE_600_600(600,600),
        SIZE_1000_1000(1000,1000),
        ;

        private final Integer width;
        private final Integer height;

        QrCodeStyleEnum(Integer width, Integer height) {
            this.width = width;
            this.height = height;
        }
    }

}
