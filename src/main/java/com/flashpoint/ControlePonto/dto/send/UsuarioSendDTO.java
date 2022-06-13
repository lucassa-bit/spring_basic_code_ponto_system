package com.flashpoint.ControlePonto.dto.send;

import com.flashpoint.ControlePonto.enumerators.TipoUsuario;

public class UsuarioSendDTO {
    private Integer id;
    private String login;
    private String nome;
	private TipoUsuario cargo;

    public UsuarioSendDTO() {}

    public UsuarioSendDTO(Integer id, String login, String nome, TipoUsuario cargo) {
        this.id = id;
        this.login = login;
        this.nome = nome;
        this.cargo = cargo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
