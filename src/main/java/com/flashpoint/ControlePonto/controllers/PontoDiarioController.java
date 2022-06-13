package com.flashpoint.ControlePonto.controllers;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import com.flashpoint.ControlePonto.dto.client.CadastrarPontosDiariosDTO;
import com.flashpoint.ControlePonto.dto.send.PontoDiarioSendDTO;
import com.flashpoint.ControlePonto.enumerators.TipoUsuario;
import com.flashpoint.ControlePonto.error.Login.AcessoNegadoException;
import com.flashpoint.ControlePonto.error.Login.UsuarioNaoExisteException;
import com.flashpoint.ControlePonto.error.Ponto.PontoJaExisteException;
import com.flashpoint.ControlePonto.error.Ponto.pontoNaoExisteException;
import com.flashpoint.ControlePonto.error.empregado.EmpregadoNaoExisteException;
import com.flashpoint.ControlePonto.service.Login.VerificadorPermissoesService;
import com.flashpoint.ControlePonto.service.controleHoras.PontosDiarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/ponto")
public class PontoDiarioController {

    private final VerificadorPermissoesService verificadorPermissoesService;
    private final PontosDiarioService pService;

    private final String CADASTRO_PONTO_REALIZADO = "Mudança do Ponto feita com sucesso!";

    @Autowired
    public PontoDiarioController(PontosDiarioService pService,
            VerificadorPermissoesService verificadorPermissoesService) {
        this.pService = pService;
        this.verificadorPermissoesService = verificadorPermissoesService;
    }

    // marcar ponto por data
    @PostMapping
    public ResponseEntity<String> marcarPonto(Authentication authentication,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") String data,
            @Valid @RequestBody List<@Valid CadastrarPontosDiariosDTO> pontoDTO)
            throws UsuarioNaoExisteException, AcessoNegadoException, EmpregadoNaoExisteException, ParseException,
            PontoJaExisteException, pontoNaoExisteException {
        List<TipoUsuario> cargosPermitidos = Arrays
                .asList(new TipoUsuario[] { TipoUsuario.ADMIN, TipoUsuario.APONTADOR, TipoUsuario.LIDER });
        verificadorPermissoesService.verificarPermissao(authentication.getName(), cargosPermitidos);

        for (CadastrarPontosDiariosDTO pontosDiariosDTO : pontoDTO) {
            pService.organizacaoPontos(data, pontosDiariosDTO);
        }

        return new ResponseEntity<String>(CADASTRO_PONTO_REALIZADO, HttpStatus.OK);
    }

    // pegar ponto por data
    @GetMapping
    @ResponseStatus(code = HttpStatus.FOUND)
    public List<PontoDiarioSendDTO> getPontos(Authentication authentication,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") String data)
            throws UsuarioNaoExisteException, AcessoNegadoException {
        List<TipoUsuario> cargosPermitidos = Arrays
                .asList(new TipoUsuario[] { TipoUsuario.ADMIN, TipoUsuario.APONTADOR, TipoUsuario.LIDER });
        verificadorPermissoesService.verificarPermissao(authentication.getName(), cargosPermitidos);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataReturn = LocalDate.parse(data, formatter);

        return pService.getPontosByData(dataReturn);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteUsuario(Authentication authentication,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") String data)
            throws UsuarioNaoExisteException, AcessoNegadoException {
        List<TipoUsuario> cargosPermitidos = Arrays
                .asList(new TipoUsuario[] { TipoUsuario.ADMIN, TipoUsuario.APONTADOR });
        verificadorPermissoesService.verificarPermissao(authentication.getName(), cargosPermitidos);

        pService.deletePontoDiario(LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        return new ResponseEntity<String>("DELETE_USUARIO_REALIZADO", HttpStatus.OK);
    }
}
