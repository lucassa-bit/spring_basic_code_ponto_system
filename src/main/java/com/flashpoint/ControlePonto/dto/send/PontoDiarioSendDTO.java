package com.flashpoint.ControlePonto.dto.send;

import java.time.LocalDate;

public class PontoDiarioSendDTO {
    private LocalDate data;
    private String hora_extra_50;
    private String hora_extra_100;
    private boolean presente;

    private EmpregadoSendDTO empregado;

    public PontoDiarioSendDTO() {
    }

    public PontoDiarioSendDTO(LocalDate data, String hora_extra_50, String hora_extra_100,
            EmpregadoSendDTO empregado, boolean presente) {
        this.data = data;
        this.hora_extra_50 = hora_extra_50;
        this.hora_extra_100 = hora_extra_100;
        this.empregado = empregado;
        this.presente = presente;
    }


    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getHora_extra_50() {
        return hora_extra_50;
    }

    public void setHora_extra_50(String hora_extra_50) {
        this.hora_extra_50 = hora_extra_50;
    }

    public String getHora_extra_100() {
        return hora_extra_100;
    }

    public void setHora_extra_100(String hora_extra_100) {
        this.hora_extra_100 = hora_extra_100;
    }

    public EmpregadoSendDTO getEmpregado() {
        return empregado;
    }

    public void setEmpregado(EmpregadoSendDTO empregado) {
        this.empregado = empregado;
    }

    public boolean getPresente() {
        return presente;
    }

    public void setPresente(boolean presente) {
        this.presente = presente;
    }
}
