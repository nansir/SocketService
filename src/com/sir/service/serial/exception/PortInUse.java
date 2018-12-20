package com.sir.service.serial.exception;

public class PortInUse extends Exception {

    private static final long serialVersionUID = 1L;

    public PortInUse() {
    }

    @Override
    public String toString() {
        return "The port is already occupied";
    }

}
