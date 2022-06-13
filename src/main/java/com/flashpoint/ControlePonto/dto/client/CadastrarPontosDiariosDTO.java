package com.flashpoint.ControlePonto.dto.client;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CadastrarPontosDiariosDTO {
    @NotNull(message = "Erro na criação do ponto diario: valor nulo (id)")
    private Integer idFuncionario;

    @NotNull(message = "Erro na criação do ponto diario: valor nulo (hora extra 50%)")
    @Pattern(regexp = "^$|\\d{1}:\\d{2}||\\d{2}:\\d{2}", message = "Modelo incorreto da hora extra de 50%")
    private String hora_extra_50;

    @NotNull(message = "Erro na criação do ponto diario: valor nulo (hora extra 100%)")
    @Pattern(regexp = "^$|\\d{1}:\\d{2}||\\d{2}:\\d{2}", message = "Modelo incorreto da hora extra de 100%")
    private String hora_extra_100;

    @NotNull(message = "Erro na criação do ponto diario: valor nulo (presente)")
    private boolean presente;

    public CadastrarPontosDiariosDTO() {
    }

    public void turnOff() {
        this.setHora_extra_100("");
        this.setHora_extra_50("");
        this.setPresente(false);
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

    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public boolean getPresente() {
        return presente;
    }

    public void setPresente(boolean presente) {
        this.presente = presente;
    }
}
