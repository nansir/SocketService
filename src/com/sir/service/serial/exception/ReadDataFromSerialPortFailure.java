package com.sir.service.serial.exception;

public class ReadDataFromSerialPortFailure extends Exception {

    private static final long serialVersionUID = 1L;

    public ReadDataFromSerialPortFailure() {
    }

    @Override
    public String toString() {
        return "Error reading data";
    }

}
