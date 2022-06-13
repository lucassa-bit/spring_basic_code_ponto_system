package com.flashpoint.ControlePonto.service.Login;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.flashpoint.ControlePonto.dto.client.CadastrarUsuarioDTO;
import com.flashpoint.ControlePonto.dto.client.EditarUsuarioDTO;
import com.flashpoint.ControlePonto.dto.send.UsuarioSendDTO;
import com.flashpoint.ControlePonto.error.Login.EncodingPasswordException;
import com.flashpoint.ControlePonto.error.Login.LoginAlreadyExistsException;
import com.flashpoint.ControlePonto.error.Login.UsuarioNaoExisteException;
import com.flashpoint.ControlePonto.model.entities.Usuario;
import com.flashpoint.ControlePonto.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicesImpl implements UsuarioServices {

    private final UsuarioRepository uRepository;

    @Autowired
    private PasswordEncoder pEncoder;

    @Autowired
    public UsuarioServicesImpl(UsuarioRepository uRepository) {
        this.uRepository = uRepository;
    }

    @Override
    public void createNewUsuario(CadastrarUsuarioDTO dto)
            throws LoginAlreadyExistsException, EncodingPasswordException {
        if (uRepository.existsByLoginIgnoreCase(dto.getLogin()))
            throw new LoginAlreadyExistsException(dto.getLogin());

        uRepository.save(Usuario.fromDto(dto, pEncoder));
    }

    @Override
    public List<UsuarioSendDTO> pickAllUsers() {
        List<UsuarioSendDTO> usuariosList = new ArrayList<>();
        Iterator<Usuario> usuarios = uRepository.findAll().iterator();
        while(usuarios.hasNext()) {
            usuariosList.add(usuarios.next().toSendDTO());
        }

        return usuariosList;
    }

    @Override
    public void editarUsuario(EditarUsuarioDTO editarUsuarioDTO) throws UsuarioNaoExisteException {
        Optional<Usuario> pOptional = uRepository.findByLoginIgnoreCase(editarUsuarioDTO.getLogin());

        if (!pOptional.isPresent())
            throw new UsuarioNaoExisteException(editarUsuarioDTO.getNome());

        pOptional.get().setCargo(editarUsuarioDTO.getCargo());
        pOptional.get().setLogin(editarUsuarioDTO.getLogin());
        pOptional.get().setNome(editarUsuarioDTO.getNome());

        String senhaCriptografada = pEncoder.encode(editarUsuarioDTO.getSenha());
        pOptional.get().setPassword(senhaCriptografada);

        uRepository.save(pOptional.get());
    }

    
    @Override
    public Usuario getUsuarioByLogin(String login) throws UsuarioNaoExisteException {
        Optional<Usuario> usuario_optional = uRepository.findByLoginIgnoreCase(login);
        if (!usuario_optional.isPresent())
            throw new UsuarioNaoExisteException(login);

        return usuario_optional.get();
    }

    @Override
    public void deleteUsuario(int id) {
        uRepository.deleteById(id);
    }

    @Override
    public UsuarioSendDTO getUsuarioById(Integer id) {
        return uRepository.findById(id).get().toSendDTO();
    }
}
