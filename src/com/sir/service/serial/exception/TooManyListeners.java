package com.sir.service.serial.exception;

public class TooManyListeners extends Exception {

    private static final long serialVersionUID = 1L;

    public TooManyListeners() {
    }

    @Override
    public String toString() {
        return "Too many serial listening classes";
    }

}
