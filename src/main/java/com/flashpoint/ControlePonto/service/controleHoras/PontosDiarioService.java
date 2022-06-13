package com.flashpoint.ControlePonto.service.controleHoras;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import com.flashpoint.ControlePonto.dto.client.CadastrarPontosDiariosDTO;
import com.flashpoint.ControlePonto.dto.send.PontoDiarioSendDTO;
import com.flashpoint.ControlePonto.error.Ponto.PontoJaExisteException;
import com.flashpoint.ControlePonto.error.Ponto.pontoNaoExisteException;
import com.flashpoint.ControlePonto.error.empregado.EmpregadoNaoExisteException;
import com.flashpoint.ControlePonto.model.entities.Empregado;
import com.flashpoint.ControlePonto.model.entities.PontosDiarios;

public interface PontosDiarioService {
    public void organizacaoPontos(String data, CadastrarPontosDiariosDTO cdto) throws EmpregadoNaoExisteException, ParseException, PontoJaExisteException, pontoNaoExisteException;
    public List<PontoDiarioSendDTO> getPontosByData(LocalDate data);
    public List<PontosDiarios> getPontosByEmpregado(Empregado data);
    public void deletePontoDiario(LocalDate data);
}
