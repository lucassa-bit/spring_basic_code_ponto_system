package com.flashpoint.ControlePonto.service.Login;

import java.util.Optional;

import com.flashpoint.ControlePonto.Data.DetalheUsuarioData;
import com.flashpoint.ControlePonto.model.entities.Usuario;
import com.flashpoint.ControlePonto.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DetalheUsuarioServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        Optional<Usuario> usuario = usuarioRepository.findByLoginIgnoreCase(login);

        if (!usuario.isPresent())
            throw new UsernameNotFoundException("Usuário " + login + " não existe.");

        return new DetalheUsuarioData(usuario);
    }

}
