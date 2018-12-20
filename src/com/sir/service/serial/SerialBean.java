package com.sir.service.serial;

/**
 * 串口信息
 * Created by zhuyinan on 2018/12/20.
 */
public class SerialBean {

    private String name;

    private int rate;

    private byte[] order;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public byte[] getOrder() {
        return order;
    }

    public void setOrder(byte[] order) {
        this.order = order;
    }
}
