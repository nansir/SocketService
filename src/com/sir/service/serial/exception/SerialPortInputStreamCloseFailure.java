package com.sir.service.serial.exception;

public class SerialPortInputStreamCloseFailure extends Exception {

    private static final long serialVersionUID = 1L;

    public SerialPortInputStreamCloseFailure() {
    }

    @Override
    public String toString() {
        return "Error closing serial input stream";
    }


}
