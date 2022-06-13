package com.flashpoint.ControlePonto.service.controleHoras;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.flashpoint.ControlePonto.dto.client.CadastrarRevisaoPontoDTO;
import com.flashpoint.ControlePonto.dto.send.RevisaoPontoSendDTO;
import com.flashpoint.ControlePonto.enumerators.StatusEnum;
import com.flashpoint.ControlePonto.error.Data.DataRangeException;
import com.flashpoint.ControlePonto.model.entities.RevisaoPonto;
import com.flashpoint.ControlePonto.repository.RevisaoPontoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RevisaoPontoServiceImpl implements RevisaoPontoService {

    private final RevisaoPontoRepository revisaoPontoRepository;

    @Autowired
    public RevisaoPontoServiceImpl(RevisaoPontoRepository revisaoPontoRepository) {
        this.revisaoPontoRepository = revisaoPontoRepository;
    }

    // Função para cadastrar o ponto caso não exista e alterar caso exista
    @Override
    public void cadastrarRevisaoPonto(CadastrarRevisaoPontoDTO cadastrarRevisaoPontoDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data = (LocalDate.parse(cadastrarRevisaoPontoDTO.getData(), formatter));
        Optional<RevisaoPonto> revisaoPonto = revisaoPontoRepository.findByData(data);

        if (!revisaoPonto.isPresent()) {
            revisaoPontoRepository.save(RevisaoPonto.fromDTO(cadastrarRevisaoPontoDTO));
            return;
        }

        if (cadastrarRevisaoPontoDTO.getStatus().equals(StatusEnum.APROVADO)
                || cadastrarRevisaoPontoDTO.getStatus().equals(StatusEnum.CADASTRADO)) {
            cadastrarRevisaoPontoDTO.setObservacao("");
        }

        revisaoPonto.get().setData(data);
        revisaoPonto.get().setObservação(cadastrarRevisaoPontoDTO.getObservacao());
        revisaoPonto.get().setStatus(cadastrarRevisaoPontoDTO.getStatus());

        revisaoPontoRepository.save(revisaoPonto.get());
    }

    // pegar revisao ponto by data
    @Override
    public RevisaoPontoSendDTO getRevisaoByData(String data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Optional<RevisaoPonto> revisaoPonto = revisaoPontoRepository.findByData(LocalDate.parse(data, formatter));

        if (!revisaoPonto.isPresent()) {
            RevisaoPonto nRevisaoPonto = new RevisaoPonto(LocalDate.parse(data, formatter), StatusEnum.NAO_CADASTRADO,
                    "");
            revisaoPontoRepository.save(nRevisaoPonto);
            return nRevisaoPonto.toSendDTO();
        }

        return revisaoPonto.get().toSendDTO();
    }

    // Variação do anterior só que com localDate, ele é usado no getRevisaoRange
    @Override
    public RevisaoPontoSendDTO getRevisaoByData(LocalDate data) {
        Optional<RevisaoPonto> revisaoPonto = revisaoPontoRepository.findByData(data);

        if (!revisaoPonto.isPresent()) {
            RevisaoPonto nRevisaoPonto = new RevisaoPonto(data, StatusEnum.NAO_CADASTRADO,
                    "");
            revisaoPontoRepository.save(nRevisaoPonto);
            return nRevisaoPonto.toSendDTO();
        }

        return revisaoPonto.get().toSendDTO();
    }

    // pegar o range de revisoes ponto por data
    @Override
    public List<RevisaoPontoSendDTO> getRevisaoRange(String dataInicial, String dataFInal) throws DataRangeException {
        List<RevisaoPontoSendDTO> listaReturn = new ArrayList<>();

        for (LocalDate data : getRange(dataInicial, dataFInal)) {
            listaReturn.add(getRevisaoByData(data));
        }

        return listaReturn;
    }

    // Robei do relatorioService
    public List<LocalDate> getRange(String minimun, String max) throws DataRangeException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate minimunDate = LocalDate.parse(minimun, formatter);
        LocalDate maxDate = LocalDate.parse(max, formatter);

        List<LocalDate> rangeList = new ArrayList<>();

        if (minimunDate.isAfter(maxDate))
            throw new DataRangeException();

        for (LocalDate date = minimunDate; maxDate.isAfter(date); date = date.plusDays(1L)) {
            rangeList.add(date);
        }
        rangeList.add(maxDate);

        return rangeList;
    }

    @Override
    public void deleteRevisaoPonto(LocalDate data) {
        for (RevisaoPonto valor : revisaoPontoRepository.findAll()) {
            if(valor.getData().equals(data)) {
                revisaoPontoRepository.delete(valor);
            }
        }
    }
}
