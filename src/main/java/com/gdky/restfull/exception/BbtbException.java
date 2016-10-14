package com.gdky.restfull.exception;
/**
 * 报表填报类异常
 */
public class BbtbException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private String message;
	
	public BbtbException(String message) {
        this.message = message;
    }	

    public String getMessage() {
        return this.message;
    }

}
