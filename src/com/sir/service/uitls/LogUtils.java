package com.sir.service.uitls;

/**
 * Created by zhuyinan on 2018/12/20.
 */
public class LogUtils {

    public static void i(String msg) {
        System.out.println(MyUtils.getTime() + " " + msg);
    }

    public static void e(String msg) {
        System.err.println(MyUtils.getTime() + " " + msg);
    }
}
