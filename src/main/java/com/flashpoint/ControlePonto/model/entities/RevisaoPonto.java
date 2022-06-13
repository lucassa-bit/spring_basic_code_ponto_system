package com.flashpoint.ControlePonto.model.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.flashpoint.ControlePonto.dto.client.CadastrarRevisaoPontoDTO;
import com.flashpoint.ControlePonto.dto.send.RevisaoPontoSendDTO;
import com.flashpoint.ControlePonto.enumerators.StatusEnum;

@Entity
public class RevisaoPonto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private LocalDate data;
    private StatusEnum status;
    private String observação;

    public RevisaoPonto() {

    }

    public RevisaoPonto(LocalDate data, StatusEnum status, String observação) {
        this.status = status;
        this.observação = observação;
        this.data = data;
    }

    public static RevisaoPonto fromDTO(CadastrarRevisaoPontoDTO cadastrarRevisaoPontoDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return new RevisaoPonto(LocalDate.parse(cadastrarRevisaoPontoDTO.getData(), formatter), cadastrarRevisaoPontoDTO.getStatus(),
                cadastrarRevisaoPontoDTO.getObservacao());
    }

    public RevisaoPontoSendDTO toSendDTO() {
        return new RevisaoPontoSendDTO(id, data, status, observação);
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getObservação() {
        return observação;
    }

    public void setObservação(String observação) {
        this.observação = observação;
    }
}
