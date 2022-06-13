package com.flashpoint.ControlePonto.repository;

import java.util.Optional;

import com.flashpoint.ControlePonto.model.entities.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    public Optional<Usuario> findByLoginIgnoreCase(String login);

    public boolean existsByLoginIgnoreCase(String login);
}
