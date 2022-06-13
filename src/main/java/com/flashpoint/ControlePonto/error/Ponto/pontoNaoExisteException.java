package com.flashpoint.ControlePonto.error.Ponto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class pontoNaoExisteException extends Exception {
    private static final String PONTO_NAO_EXISTE = "Ponto de %s da data de %s não existe para fazer alteração.";
	
	public pontoNaoExisteException(String nome, String data) {
		super(String.format(PONTO_NAO_EXISTE, nome, data));
	}
}
