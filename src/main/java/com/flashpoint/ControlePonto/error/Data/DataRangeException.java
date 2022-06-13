package com.flashpoint.ControlePonto.error.Data;

public class DataRangeException extends Exception{
    private static final String RANGE_DATA = "Range de data informada não é possível";
	
	public DataRangeException() {
		super(String.format(RANGE_DATA));
	}
}
