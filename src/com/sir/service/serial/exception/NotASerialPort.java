package com.sir.service.serial.exception;

public class NotASerialPort extends Exception {

    private static final long serialVersionUID = 1L;

    public NotASerialPort() {
    }

    @Override
    public String toString() {
        return "Port is not a serial port type";
    }
}
