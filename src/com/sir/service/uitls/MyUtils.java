package com.sir.service.uitls;

import java.io.*;
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


    /**
     * 获取JSON文件
     *
     * @param fileName
     * @return
     */
    public static String getJson(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(fileContent, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 为空判断
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }


    /**
     * 目标网络检查
     *
     * @param str
     * @return
     */
    public static boolean ping(String str) {
        Runtime runtime = Runtime.getRuntime();
        Process process;
        try {
            process = runtime.exec("ping " + str);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            isr.close();
            br.close();
            if (null != sb && !sb.toString().equals("")) {
                return sb.toString().indexOf("TTL") > 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
