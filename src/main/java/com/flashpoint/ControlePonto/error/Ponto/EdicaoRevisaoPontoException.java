package com.flashpoint.ControlePonto.error.Ponto;

public class EdicaoRevisaoPontoException extends Exception {
    private static final String REVISAO_NAO_PODE_SER_EDITADA = "Nao pode alterar a revisao apos ser aprovada.";
	
	public EdicaoRevisaoPontoException() {
		super(REVISAO_NAO_PODE_SER_EDITADA);
	}
}
