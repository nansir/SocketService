package com.sir.service.serial.exception;

public class NoSuchPort extends Exception {

    private static final long serialVersionUID = 1L;

    public NoSuchPort() {

    }

    @Override
    public String toString() {
        return "No matching serial port found";
    }

}
