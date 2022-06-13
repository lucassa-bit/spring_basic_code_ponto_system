package com.flashpoint.ControlePonto.dto.client;

import javax.validation.constraints.NotNull;

public class CadastrarRelatorioDTO {
    @NotNull(message = "Erro na criação do relatorio: valor nulo (selecionado)")
    boolean selecionado;
    
    @NotNull(message = "Erro na criação do relatorio: valor nulo (id do funcionario)")
    Integer idFuncionarios;

    public CadastrarRelatorioDTO() {
    }

    public boolean getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }

    public Integer getIdFuncionarios() {
        return idFuncionarios;
    }

    public void setIdFuncionarios(Integer idFuncionarios) {
        this.idFuncionarios = idFuncionarios;
    }
}
