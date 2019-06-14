package com.sir.service;

import com.sir.service.soket.SocketTCPService;
import com.sir.service.soket.SocketUDPService;
import com.sir.service.uitls.Key;
import com.sir.service.uitls.MyUtils;

import java.io.IOException;

/**
 * 启动服务
 * Created by zhuyinan on 2018/12/19.
 * Contact by 445181052@qq.com
 */
public class Entrance {

    public static void main(String[] args) {
        try {
            String value = MyUtils.getConfig(Key.MODEL);
            if ("UDP".equals(value.toUpperCase())) {
                SocketUDPService.getInstance().start();
            } else {
                SocketTCPService.getInstance().start();
            }
        } catch (IOException e) {
            SocketTCPService.getInstance().start();
        }
    }
}
