package com.flashpoint.ControlePonto.error.Ponto;

public class RevisaoPontoNaoExisteException extends Exception {
    private static final String REVISAO_PONTO_NAO_EXISTE = "Revisao do Ponto da data de %s não existe.";
	
	public RevisaoPontoNaoExisteException(String data) {
		super(String.format(REVISAO_PONTO_NAO_EXISTE, data));
	}
}
