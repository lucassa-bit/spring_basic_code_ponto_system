package com.flashpoint.ControlePonto.error.empregado;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EmpregadoNaoExisteException extends Exception {
    public static final String EMPREGADO_NAO_EXISTE = "Empregado %s nao existe";

    public EmpregadoNaoExisteException(String string) {
        super(String.format(EMPREGADO_NAO_EXISTE, string));
    }
}
