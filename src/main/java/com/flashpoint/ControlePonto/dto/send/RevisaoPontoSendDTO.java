package com.flashpoint.ControlePonto.dto.send;

import java.time.LocalDate;

import com.flashpoint.ControlePonto.enumerators.StatusEnum;

public class RevisaoPontoSendDTO {
    private LocalDate data;
    private StatusEnum status;
    private String observacao;
                          
    public RevisaoPontoSendDTO() {
    }

    public RevisaoPontoSendDTO(Integer id, LocalDate data, StatusEnum status, String observação) {
        this.data = data;
        this.status = status;
        this.observacao = observação;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getobservacao() {
        return observacao;
    }

    public void setobservacao(String observação) {
        this.observacao = observação;
    }
}
