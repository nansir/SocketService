package com.sir.service;

import com.sir.service.soket.SocketService;

/**
 * Created by zhuyinan on 2018/12/19.
 */
public class Entrance {

    public static void main(String[] args) {
        SocketService.getInstance().start();
    }
}
