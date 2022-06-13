package com.flashpoint.ControlePonto.service.Login;

import java.util.List;

import com.flashpoint.ControlePonto.dto.client.CadastrarUsuarioDTO;
import com.flashpoint.ControlePonto.dto.client.EditarUsuarioDTO;
import com.flashpoint.ControlePonto.dto.send.UsuarioSendDTO;
import com.flashpoint.ControlePonto.error.Login.EncodingPasswordException;
import com.flashpoint.ControlePonto.error.Login.LoginAlreadyExistsException;
import com.flashpoint.ControlePonto.error.Login.UsuarioNaoExisteException;
import com.flashpoint.ControlePonto.model.entities.Usuario;

import org.springframework.security.core.context.SecurityContextHolder;

public interface UsuarioServices {
    public List<UsuarioSendDTO> pickAllUsers();
    public void createNewUsuario(CadastrarUsuarioDTO dto) throws LoginAlreadyExistsException, EncodingPasswordException;
    public void editarUsuario(EditarUsuarioDTO editarUsuarioDTO) throws UsuarioNaoExisteException;
    public UsuarioSendDTO getUsuarioById(Integer id);
    public Usuario getUsuarioByLogin(String login) throws UsuarioNaoExisteException;
    public void deleteUsuario(int id);
    
    public static Usuario authenticated() {
        try {
            return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }
}
