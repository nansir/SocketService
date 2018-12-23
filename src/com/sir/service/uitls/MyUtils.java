package com.sir.service.uitls;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Created by zhuyinan on 2018/12/20.
 */
public class MyUtils {

    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    /**
     * 读取属性文件config.properties
     *
     * @param key
     * @return
     */
    public static String getConfig(String key) throws IOException {
        Properties prop = new Properties();
        FileInputStream stream = new FileInputStream("config.properties");
        InputStream in = new BufferedInputStream(stream);
        prop.load(in);
        String value = prop.getProperty(key);
        in.close();
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

    /**
     * 获取系统时间
     *
     * @return
     */
    public static String getTime() {
        return df.format(new Date());
    }
}
