package com.sir.service.serial.exception;

public class SerialPortOutputStreamCloseFailure extends Exception {

	private static final long serialVersionUID = 1L;

	public SerialPortOutputStreamCloseFailure() {}

	@Override
	public String toString() {
		return "Error closing serial output stream";
	}
}
