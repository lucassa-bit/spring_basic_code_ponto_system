package com.flashpoint.ControlePonto.error.Ponto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class PontoJaExisteException extends Exception{
    private static final String PONTO_EXISTENTE = "Ponto de %s já existente na data de %s.";
	
	public PontoJaExisteException(String nome, String data) {
		super(String.format(PONTO_EXISTENTE, nome, data));
	}
}
