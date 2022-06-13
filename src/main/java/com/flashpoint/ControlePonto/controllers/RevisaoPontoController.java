package com.flashpoint.ControlePonto.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import com.flashpoint.ControlePonto.dto.client.CadastrarRevisaoPontoDTO;
import com.flashpoint.ControlePonto.dto.send.RevisaoPontoSendDTO;
import com.flashpoint.ControlePonto.enumerators.TipoUsuario;
import com.flashpoint.ControlePonto.error.Data.DataRangeException;
import com.flashpoint.ControlePonto.error.Login.AcessoNegadoException;
import com.flashpoint.ControlePonto.error.Login.UsuarioNaoExisteException;
import com.flashpoint.ControlePonto.error.Ponto.EdicaoRevisaoPontoException;
import com.flashpoint.ControlePonto.error.Ponto.RevisaoPontoNaoExisteException;
import com.flashpoint.ControlePonto.service.Login.VerificadorPermissoesService;
import com.flashpoint.ControlePonto.service.controleHoras.RevisaoPontoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/revisao_ponto")
public class RevisaoPontoController {

    private final VerificadorPermissoesService verificadorPermissoesService;
    private final RevisaoPontoService pService;

    private String EDICAO_REVISAO_REALIZADO = "Edicao de revisao realizado com sucesso!";

    @Autowired
    public RevisaoPontoController(RevisaoPontoService pService,
            VerificadorPermissoesService verificadorPermissoesService) {
        this.pService = pService;
        this.verificadorPermissoesService = verificadorPermissoesService;
    }

    // pegar revisao ponto by data
    @GetMapping
    public RevisaoPontoSendDTO revisaoPontoSendDTO(Authentication authentication,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") String data)
            throws UsuarioNaoExisteException, AcessoNegadoException {
        List<TipoUsuario> cargosPermitidos = Arrays
                .asList(new TipoUsuario[] { TipoUsuario.ADMIN, TipoUsuario.APONTADOR, TipoUsuario.LIDER });
        verificadorPermissoesService.verificarPermissao(authentication.getName(), cargosPermitidos);

        return pService.getRevisaoByData(data);
    }

    // editar revisao ponto by informações do body editarRevisaoPontoDTO
    @PostMapping
    public ResponseEntity<String> editarRevisaoPonto(Authentication authentication,
            @Valid @RequestBody CadastrarRevisaoPontoDTO editarRevisaoPontoDTO)
            throws UsuarioNaoExisteException, AcessoNegadoException, RevisaoPontoNaoExisteException,
            EdicaoRevisaoPontoException {
        List<TipoUsuario> cargosPermitidos = Arrays
                .asList(new TipoUsuario[] { TipoUsuario.ADMIN, TipoUsuario.APONTADOR, TipoUsuario.LIDER });
        verificadorPermissoesService.verificarPermissao(authentication.getName(), cargosPermitidos);

        pService.cadastrarRevisaoPonto(editarRevisaoPontoDTO);

        return new ResponseEntity<String>(EDICAO_REVISAO_REALIZADO, HttpStatus.OK);
    }

    // pegar todos os revisoes ponto by dataInicial e dataFinal
    @GetMapping("/range")
    public List<RevisaoPontoSendDTO> pegarRangeDeRevisoesPonto(Authentication authentication,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") String dataInicial,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") String dataFinal)
            throws UsuarioNaoExisteException, AcessoNegadoException, DataRangeException {

        List<TipoUsuario> cargosPermitidos = Arrays
                .asList(new TipoUsuario[] { TipoUsuario.ADMIN, TipoUsuario.APONTADOR, TipoUsuario.LIDER });
        verificadorPermissoesService.verificarPermissao(authentication.getName(), cargosPermitidos);

        List<RevisaoPontoSendDTO> listaReturn = pService.getRevisaoRange(dataInicial, dataFinal);

        return listaReturn;
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteUsuario(Authentication authentication,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") String data)
            throws UsuarioNaoExisteException, AcessoNegadoException {
        List<TipoUsuario> cargosPermitidos = Arrays
                .asList(new TipoUsuario[] { TipoUsuario.ADMIN, TipoUsuario.APONTADOR });
        verificadorPermissoesService.verificarPermissao(authentication.getName(), cargosPermitidos);

        pService.deleteRevisaoPonto(LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        return new ResponseEntity<String>("DELETE_USUARIO_REALIZADO", HttpStatus.OK);
    }
}
