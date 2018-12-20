package com.sir.service.uitls;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by zhuyinan on 2018/12/20.
 */

public class MyUtils {


    /**
     * 读取属性文件config.properties
     *
     * @param key
     * @return
     */
    public static String getConfig(String key) {
        Properties prop = new Properties();
        String value = "";
        try {
            FileInputStream stream = new FileInputStream("config.properties");
            InputStream in = new BufferedInputStream(stream);
            prop.load(in);
            value = prop.getProperty(key);
            in.close();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * bytes数组转字符串
     *
     * @param src
     * @return
     */
    public static String bytesToString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length < 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv + " ");
        }
        return stringBuilder.toString();
    }
}
