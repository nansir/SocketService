package com.sir.service.serial;

/**
 * Created by zhuyinan on 2019/6/13.
 */
public class DeviceBean {

    String name;

    String ip;

    int port;

    String mac;

    int sleep;

    int type;

    boolean on_line;

    byte[] pro_no;

    byte[] pro_nc;

    int wt_no;

    int wt_nc;

    public int getSleep() {
        return sleep;
    }

    public String getName() {
        return name;
    }

    public void setOnLine(boolean on_line) {
        this.on_line = on_line;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public boolean isOnLine() {
        return on_line;
    }

    public String getMac() {
        return mac;
    }

    public int getType() {
        return type;
    }

    public byte[] getProNo() {
        return pro_no;
    }

    public byte[] getProNc() {
        return pro_nc;
    }

    public int getWtNo() {
        return wt_no;
    }

    public int getWtNc() {
        return wt_nc;
    }
}
