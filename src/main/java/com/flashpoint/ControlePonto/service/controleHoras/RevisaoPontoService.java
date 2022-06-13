package com.flashpoint.ControlePonto.service.controleHoras;

import java.time.LocalDate;
import java.util.List;

import com.flashpoint.ControlePonto.dto.client.CadastrarRevisaoPontoDTO;
import com.flashpoint.ControlePonto.dto.send.RevisaoPontoSendDTO;
import com.flashpoint.ControlePonto.error.Data.DataRangeException;

public interface RevisaoPontoService {

    public void cadastrarRevisaoPonto(CadastrarRevisaoPontoDTO cadastrarRevisaoPontoDTO);
    public RevisaoPontoSendDTO getRevisaoByData(String data);
    public RevisaoPontoSendDTO getRevisaoByData(LocalDate data);
    public List<RevisaoPontoSendDTO> getRevisaoRange(String dataInicial, String dataFInal) throws DataRangeException;
    public void deleteRevisaoPonto(LocalDate data);
}
