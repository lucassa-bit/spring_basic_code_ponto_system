package com.flashpoint.ControlePonto.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.flashpoint.ControlePonto.enumerators.TipoUsuario;
import com.flashpoint.ControlePonto.error.Data.DataRangeException;
import com.flashpoint.ControlePonto.error.Data.TokenInexistenteException;
import com.flashpoint.ControlePonto.error.Login.AcessoNegadoException;
import com.flashpoint.ControlePonto.error.Login.UsuarioNaoExisteException;
import com.flashpoint.ControlePonto.error.empregado.EmpregadoNaoExisteException;
import com.flashpoint.ControlePonto.security.TokenGenerate;
import com.flashpoint.ControlePonto.service.Login.VerificadorPermissoesService;
import com.flashpoint.ControlePonto.service.controleHoras.RelatorioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relatorio")
public class RelatorioController {
    private final VerificadorPermissoesService verificadorPermissoesService;
    private final RelatorioService relatorioService;

    List<String> temporaryTokens = new ArrayList<String>();

    @Autowired
    public RelatorioController(VerificadorPermissoesService verificadorPermissoesService,
            RelatorioService relatorioService) {
        this.verificadorPermissoesService = verificadorPermissoesService;
        this.relatorioService = relatorioService;
    }

    // pegar token temporario para conseguir fazer o download do relatorio
    @GetMapping("/token")
    public ResponseEntity<String> tokenTemporario(Authentication authentication)
            throws UsuarioNaoExisteException, AcessoNegadoException {
        List<TipoUsuario> cargosPermitidos = Arrays.asList(new TipoUsuario[] { TipoUsuario.ADMIN, TipoUsuario.LIDER });
        verificadorPermissoesService.verificarPermissao(authentication.getName(), cargosPermitidos);

        String temporaryToken = TokenGenerate.generateToken();
        temporaryTokens.add(temporaryToken);
        return new ResponseEntity<String>(temporaryToken, HttpStatus.OK);
    }

    // request do relatorio
    @GetMapping
    public ResponseEntity<InputStreamResource> generateRelatorio(
            @RequestParam(value = "temporaryToken", required = true) String temporaryToken,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") String data_inicial,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") String data_final,
            @RequestParam List<Integer> ids)
            throws UsuarioNaoExisteException, AcessoNegadoException, IOException, DataRangeException,
            EmpregadoNaoExisteException, TokenInexistenteException {

        if (!temporaryTokens.contains(temporaryToken))
            throw new TokenInexistenteException();

        temporaryTokens.remove(temporaryToken);
        relatorioService.createCSV(ids, data_inicial, data_final);

        File file = new File("workbook.xlsx");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "workbook.xlsx");

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
