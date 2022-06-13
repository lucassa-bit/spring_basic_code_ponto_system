package com.flashpoint.ControlePonto.service.controleHoras;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.flashpoint.ControlePonto.dto.client.CadastrarPontosDiariosDTO;
import com.flashpoint.ControlePonto.dto.send.PontoDiarioSendDTO;
import com.flashpoint.ControlePonto.error.Ponto.PontoJaExisteException;
import com.flashpoint.ControlePonto.error.Ponto.pontoNaoExisteException;
import com.flashpoint.ControlePonto.error.empregado.EmpregadoNaoExisteException;
import com.flashpoint.ControlePonto.model.entities.Empregado;
import com.flashpoint.ControlePonto.model.entities.PontosDiarios;
import com.flashpoint.ControlePonto.repository.EmpregadoRepository;
import com.flashpoint.ControlePonto.repository.PontosDiariosRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PontosDiarioServiceImpl implements PontosDiarioService {
    private final EmpregadoService eService;
    private final PontosDiariosRepository pontosDiariosRepository;
    private final EmpregadoRepository eRepository;

    @Autowired
    public PontosDiarioServiceImpl(EmpregadoService eService, PontosDiariosRepository pontosDiariosRepository,
            EmpregadoRepository eRepository) {
        this.eService = eService;
        this.pontosDiariosRepository = pontosDiariosRepository;
        this.eRepository = eRepository;
    }

    // função para organizar a marcação de ponto
    @Override
    public void organizacaoPontos(String data, CadastrarPontosDiariosDTO cdto)
            throws EmpregadoNaoExisteException, ParseException, PontoJaExisteException, pontoNaoExisteException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localData = LocalDate.parse(data, formatter);

        // se o funcionario não estiver presente ele vai deixar vazio os atributos de
        // horas extras
        if (!cdto.getPresente()) {
            cdto.turnOff();
        }
        Empregado empregado = eService.getEmpregadoById(cdto.getIdFuncionario());

        Optional<PontosDiarios> pontoDiario = pontosDiariosRepository.findByDataAndEmpregado(localData, empregado);

        // deixei por precaução, caso algo dê errado
        if (!pontoDiario.isPresent()) {
            pontosDiariosRepository.save(PontosDiarios.fromDTO(cdto, empregado, localData));
            return;
        }

        pontoDiario.get().setHora_extra_100(cdto.getHora_extra_100());
        pontoDiario.get().setHora_extra_50(cdto.getHora_extra_50());
        pontoDiario.get().setPresente(cdto.getPresente());

        pontosDiariosRepository.save(pontoDiario.get());

    }

    // Função para pegar todos ponto naquela data em específico
    @Override
    public List<PontoDiarioSendDTO> getPontosByData(LocalDate data) {
        List<PontoDiarioSendDTO> pDtos = new ArrayList<>();

        Iterator<PontosDiarios> pontosIterator = pontosDiariosRepository.findAll().iterator();
        if (pontosDiariosRepository.existsByData(data)) {

            while (pontosIterator.hasNext()) {

                PontosDiarios pDiarios = pontosIterator.next();

                if (data.compareTo(pDiarios.getData()) == 0) {
                    pDtos.add(new PontoDiarioSendDTO(data, pDiarios.getHora_extra_50(),
                            pDiarios.getHora_extra_100(), pDiarios.getEmpregado().toSendDTO(), pDiarios.getPresente()));
                }

            }

        }

        // caso não exista o ponto de determinados funcionarios, eles são criados como
        // vázios para serem usados
        Iterator<Empregado> empregados = eRepository.findAll().iterator();

        while (empregados.hasNext()) {

            Empregado empregado = empregados.next();

            if (!pontosDiariosRepository.existsByDataAndEmpregado(data, empregado)) {

                PontosDiarios pontos = new PontosDiarios(empregado, data, "", "", false);
                pontosDiariosRepository.save(pontos);

                pDtos.add(pontos.toSendDTO());

            }

        }

        return pDtos;
    }

    //
    @Override
    public void deletePontoDiario(LocalDate data) {
        for (PontosDiarios valor : pontosDiariosRepository.findAll()) {
            if(valor.getData().equals(data)) {
                pontosDiariosRepository.delete(valor);
            }
        }
    }

    // USADO NO RELATORIO EM ESPECÍFICO NEM PENSE EM TIRAR
    @Override
    public List<PontosDiarios> getPontosByEmpregado(Empregado empregado) {
        List<PontosDiarios> pDtos = new ArrayList<>();

        Iterator<PontosDiarios> pontosIterator = pontosDiariosRepository.findAllByEmpregado(empregado).iterator();

        while (pontosIterator.hasNext()) {
            pDtos.add(pontosIterator.next());

        }

        return pDtos;
    }
}