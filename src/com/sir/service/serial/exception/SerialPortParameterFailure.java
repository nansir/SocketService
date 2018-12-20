package com.sir.service.serial.exception;

public class SerialPortParameterFailure extends Exception {

    private static final long serialVersionUID = 1L;

    public SerialPortParameterFailure() {
    }

    @Override
    public String toString() {
        return "Setting serial port parameters failed";
    }

}
