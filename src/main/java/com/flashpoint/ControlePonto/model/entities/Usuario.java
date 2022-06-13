package com.flashpoint.ControlePonto.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.flashpoint.ControlePonto.dto.client.CadastrarUsuarioDTO;
import com.flashpoint.ControlePonto.dto.send.UsuarioSendDTO;
import com.flashpoint.ControlePonto.enumerators.TipoUsuario;
import com.flashpoint.ControlePonto.error.Login.EncodingPasswordException;

import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String login;

    private String password;

    private String nome;

    private TipoUsuario cargo;

    public Usuario() {
    }

    public Usuario(String login, String password, String nome, TipoUsuario cargo) {
        this.login = login;
        this.password = password;
        this.nome = nome;
        this.cargo = cargo;
    }

    public static Usuario fromDto(CadastrarUsuarioDTO dto, PasswordEncoder pEncoder) throws EncodingPasswordException {
        String passwordEncoded = pEncoder.encode(dto.getSenha());

        return new Usuario(dto.getLogin(), passwordEncoded, dto.getNome(), dto.getCargo());
    }

    public UsuarioSendDTO toSendDTO() {
        return new UsuarioSendDTO(id, login, nome, cargo);
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

    public String getPassword() { 
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
