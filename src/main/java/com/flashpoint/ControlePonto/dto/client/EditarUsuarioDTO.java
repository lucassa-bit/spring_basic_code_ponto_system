package com.flashpoint.ControlePonto.dto.client;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flashpoint.ControlePonto.enumerators.TipoUsuario;

public class EditarUsuarioDTO {
    @NotBlank(message = "Erro na criação do usuario: valor em branco/nulo (login)")
    private String login;

    @JsonProperty(value = "senha")
    @NotBlank(message = "Erro na criação do usuario: valor em branco/nulo (senha)")
    private String senha;

    @NotBlank(message = "Erro na criação do usuario: valor em branco/nulo (nome)")
    private String nome;

    @NotNull(message = "Erro na criação do usuario: valor nulo (cargo)")
    private TipoUsuario cargo;

    public EditarUsuarioDTO() {}

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoUsuario getCargo() {
        return cargo;
    }

    public void setCargo(TipoUsuario cargo) {
        this.cargo = cargo;
    }
}
