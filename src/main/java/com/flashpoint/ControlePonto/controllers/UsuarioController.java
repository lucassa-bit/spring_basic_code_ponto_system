package com.flashpoint.ControlePonto.controllers;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import com.flashpoint.ControlePonto.dto.client.CadastrarUsuarioDTO;
import com.flashpoint.ControlePonto.dto.client.EditarUsuarioDTO;
import com.flashpoint.ControlePonto.dto.send.UsuarioSendDTO;
import com.flashpoint.ControlePonto.enumerators.TipoUsuario;
import com.flashpoint.ControlePonto.error.Login.AcessoNegadoException;
import com.flashpoint.ControlePonto.error.Login.EncodingPasswordException;
import com.flashpoint.ControlePonto.error.Login.LoginAlreadyExistsException;
import com.flashpoint.ControlePonto.error.Login.UsuarioNaoExisteException;
import com.flashpoint.ControlePonto.model.entities.Usuario;
import com.flashpoint.ControlePonto.service.Login.UsuarioServices;
import com.flashpoint.ControlePonto.service.Login.VerificadorPermissoesService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
    private final UsuarioServices uServices;
    private final VerificadorPermissoesService verificadorPermissoesService;

    private final String CADASTRO_USUARIO_REALIZADO = "Cadastro do usuario realizado com sucesso!";
    private final String EDICAO_USUARIO_REALIZADO = "Edicao de usuario realizado com sucesso!";
    private final String DELETE_USUARIO_REALIZADO = "Delete de usuario realizado com sucesso!";

    public UsuarioController(UsuarioServices uServices, VerificadorPermissoesService verificadorPermissoesService) {
        this.uServices = uServices;
        this.verificadorPermissoesService = verificadorPermissoesService;
    }

    // Criar novo usuario
    @PostMapping
    public ResponseEntity<String> createNewUsuario(@Valid @RequestBody CadastrarUsuarioDTO usuarioDTO)
            throws LoginAlreadyExistsException, EncodingPasswordException, UsuarioNaoExisteException,
            AcessoNegadoException {

        if (usuarioDTO.getSenha() == null)
            throw new EncodingPasswordException("Senha possui valor nulo");

        uServices.createNewUsuario(usuarioDTO);

        return new ResponseEntity<String>(CADASTRO_USUARIO_REALIZADO, HttpStatus.CREATED);
    }

    // pegar todos os usuarios
    @GetMapping
    @ResponseStatus(code = HttpStatus.FOUND)
    public List<UsuarioSendDTO> getAllUsuarios(Authentication authentication)
            throws UsuarioNaoExisteException, AcessoNegadoException {
        List<TipoUsuario> cargosPermitidos = Arrays.asList(new TipoUsuario[] { TipoUsuario.ADMIN });
        verificadorPermissoesService.verificarPermissao(authentication.getName(), cargosPermitidos);

        return uServices.pickAllUsers();
    }

    // pegar usuario por id
    @GetMapping("/find")
    @ResponseStatus(code = HttpStatus.FOUND)
    public UsuarioSendDTO getUsuarioById(Authentication authentication, @RequestParam int id)
            throws UsuarioNaoExisteException, AcessoNegadoException {
        List<TipoUsuario> cargosPermitidos = Arrays.asList(new TipoUsuario[] { TipoUsuario.ADMIN });
        verificadorPermissoesService.verificarPermissao(authentication.getName(), cargosPermitidos);

        return uServices.getUsuarioById(id);
    }

    // editar usuario
    @PostMapping("/edit")
    public ResponseEntity<String> editUsuario(Authentication authentication,
            @Valid @RequestBody EditarUsuarioDTO usuarioDTO) throws UsuarioNaoExisteException, AcessoNegadoException {
        List<TipoUsuario> cargosPermitidos = Arrays.asList(new TipoUsuario[] { TipoUsuario.ADMIN });
        verificadorPermissoesService.verificarPermissao(authentication.getName(), cargosPermitidos);

        uServices.editarUsuario(usuarioDTO);

        return new ResponseEntity<String>(EDICAO_USUARIO_REALIZADO, HttpStatus.CREATED);
    }

    // retornar usuario logado
    @GetMapping("/me")
    public Usuario getUsuarioLogado(Authentication authentication)
            throws UsuarioNaoExisteException, AcessoNegadoException {
        List<TipoUsuario> cargosPermitidos = Arrays
                .asList(new TipoUsuario[] { TipoUsuario.ADMIN, TipoUsuario.APONTADOR, TipoUsuario.LIDER });
        verificadorPermissoesService.verificarPermissao(authentication.getName(), cargosPermitidos);

        return uServices.getUsuarioByLogin(authentication.getName());
    }

    // deletar usuario
    @PostMapping("/delete")
    public ResponseEntity<String> deleteUsuario(Authentication authentication,
            @RequestParam Integer id) throws UsuarioNaoExisteException, AcessoNegadoException {
        List<TipoUsuario> cargosPermitidos = Arrays
                .asList(new TipoUsuario[] { TipoUsuario.ADMIN, TipoUsuario.APONTADOR });
        verificadorPermissoesService.verificarPermissao(authentication.getName(), cargosPermitidos);

        uServices.deleteUsuario(id);

        return new ResponseEntity<String>(DELETE_USUARIO_REALIZADO, HttpStatus.OK);
    }
}
