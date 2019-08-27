package com.nxlh.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 各种id生成策略
 */
public class IDUtils {

    /**
     * 图片名生成
     */
    public static String genImageName() {
        //取当前时间的长整形值包含毫秒
        long millis = System.currentTimeMillis();
        //long millis = System.nanoTime();
        //加上三位随机数
        Random random = new Random();
        int end3 = random.nextInt(999);
        //如果不足三位前面补0
        String str = millis + String.format("%03d", end3);
        return str;
    }


    public static String genUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String empty() {
        return "00000000000000000000000000000000";
    }
}