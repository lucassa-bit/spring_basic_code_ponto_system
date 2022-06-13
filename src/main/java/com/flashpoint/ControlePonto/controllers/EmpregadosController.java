package com.flashpoint.ControlePonto.controllers;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import com.flashpoint.ControlePonto.dto.client.CadastrarEmpregadoDTO;
import com.flashpoint.ControlePonto.dto.client.EditarEmpregadoDTO;
import com.flashpoint.ControlePonto.dto.send.EmpregadoSendDTO;
import com.flashpoint.ControlePonto.enumerators.TipoUsuario;
import com.flashpoint.ControlePonto.error.Login.AcessoNegadoException;
import com.flashpoint.ControlePonto.error.Login.UsuarioNaoExisteException;
import com.flashpoint.ControlePonto.error.empregado.EmpregadoNaoExisteException;
import com.flashpoint.ControlePonto.service.Login.VerificadorPermissoesService;
import com.flashpoint.ControlePonto.service.controleHoras.EmpregadoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/funcionario")
public class EmpregadosController {

    @Autowired
    private EmpregadoService eService;
    @Autowired
    private VerificadorPermissoesService verificadorPermissoesService;

    private final String CADASTRO_FUNCIONARIO_REALIZADO = "Cadastro de funcionario realizado com sucesso!";
    private final String EDICAO_FUNCIONARIO_REALIZADA = "Edicao de funcionario realizada com sucesso!";
    private final String REMOVE_FUNCIONARIO_REALIZADO = "Remocao de funcionario realizada com sucesso!";

    // Registro de empregado
    @PostMapping
    public ResponseEntity<String> RegistrarEmpregado(Authentication authentication,
            @Valid @RequestBody CadastrarEmpregadoDTO empregadoDTO)
            throws ParseException, UsuarioNaoExisteException, AcessoNegadoException {
        List<TipoUsuario> cargosPermitidos = Arrays.asList(new TipoUsuario[] { TipoUsuario.ADMIN, TipoUsuario.LIDER });
        verificadorPermissoesService.verificarPermissao(authentication.getName(), cargosPermitidos);

        eService.createNewEmpregado(empregadoDTO);

        return new ResponseEntity<String>(CADASTRO_FUNCIONARIO_REALIZADO, HttpStatus.CREATED);
    }

    // encontrar empregado by id
    @GetMapping("/find")
    @ResponseStatus(code = HttpStatus.FOUND)
    public EmpregadoSendDTO receberEmpregado(Authentication authentication, @RequestParam int idEmpregado)
            throws EmpregadoNaoExisteException, UsuarioNaoExisteException, AcessoNegadoException {
        List<TipoUsuario> cargosPermitidos = Arrays
                .asList(new TipoUsuario[] { TipoUsuario.ADMIN, TipoUsuario.APONTADOR, TipoUsuario.LIDER });
        verificadorPermissoesService.verificarPermissao(authentication.getName(), cargosPermitidos);

        return eService.getEmpregadoById(idEmpregado).toSendDTO();
    }

    // achar todos os empregados
    @GetMapping
    @ResponseStatus(code = HttpStatus.FOUND)
    public ResponseEntity<List<EmpregadoSendDTO>> getAllEmpregado(Authentication authentication)
            throws UsuarioNaoExisteException, AcessoNegadoException {
        List<TipoUsuario> cargosPermitidos = Arrays
                .asList(new TipoUsuario[] { TipoUsuario.ADMIN, TipoUsuario.LIDER, TipoUsuario.APONTADOR });
        verificadorPermissoesService.verificarPermissao(authentication.getName(), cargosPermitidos);

        return new ResponseEntity<List<EmpregadoSendDTO>>(eService.getAllEmpregados(), HttpStatus.OK);
    }

    // editar empregado
    @PostMapping("/edit")
    public ResponseEntity<String> editarEmpregado(Authentication authentication,
            @Valid @RequestBody EditarEmpregadoDTO editarEmpregadoDTO)
            throws UsuarioNaoExisteException, AcessoNegadoException {
        List<TipoUsuario> cargosPermitidos = Arrays.asList(new TipoUsuario[] { TipoUsuario.ADMIN, TipoUsuario.LIDER });
        verificadorPermissoesService.verificarPermissao(authentication.getName(), cargosPermitidos);

        eService.editEmpregado(editarEmpregadoDTO);

        return new ResponseEntity<String>(EDICAO_FUNCIONARIO_REALIZADA, HttpStatus.OK);
    }

    // deletar empregado
    @PostMapping("/delete")
    public ResponseEntity<String> deleteEmpregado(Authentication authentication,
            @RequestParam Integer id) throws UsuarioNaoExisteException, AcessoNegadoException {
        List<TipoUsuario> cargosPermitidos = Arrays.asList(new TipoUsuario[] { TipoUsuario.ADMIN, TipoUsuario.LIDER });
        verificadorPermissoesService.verificarPermissao(authentication.getName(), cargosPermitidos);

        eService.deleteEmpregado(id);

        return new ResponseEntity<String>(REMOVE_FUNCIONARIO_REALIZADO, HttpStatus.OK);
    }
}
