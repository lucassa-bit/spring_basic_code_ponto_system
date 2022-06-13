package com.flashpoint.ControlePonto.dto.client;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.flashpoint.ControlePonto.enumerators.StatusEnum;

import org.springframework.format.annotation.DateTimeFormat;

public class CadastrarRevisaoPontoDTO {
    @NotBlank(message = "Erro na criação da revisao do ponto: valor em branco/nulo (data)")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String data;

    @NotNull(message = "Erro na criação do relatorio: valor nulo (status)")
    private StatusEnum status;
    
    private String observacao;

    public CadastrarRevisaoPontoDTO() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observação) {
        this.observacao = observação;
    }

}
