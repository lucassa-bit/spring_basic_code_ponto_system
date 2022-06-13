package com.flashpoint.ControlePonto.service.controleHoras;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.flashpoint.ControlePonto.dto.client.CadastrarEmpregadoDTO;
import com.flashpoint.ControlePonto.dto.client.EditarEmpregadoDTO;
import com.flashpoint.ControlePonto.dto.send.EmpregadoSendDTO;
import com.flashpoint.ControlePonto.enumerators.PagamentoEnum;
import com.flashpoint.ControlePonto.error.Login.UsuarioNaoExisteException;
import com.flashpoint.ControlePonto.error.empregado.EmpregadoNaoExisteException;
import com.flashpoint.ControlePonto.model.entities.Empregado;
import com.flashpoint.ControlePonto.repository.EmpregadoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpregadoServiceImpl implements EmpregadoService {
    private final EmpregadoRepository empregadoRepository;

    @Autowired
    public EmpregadoServiceImpl(EmpregadoRepository empregadoRepository) {
        this.empregadoRepository = empregadoRepository;
    }

    // função que pega empregados by id
    @Override
    public Empregado getEmpregadoById(Integer id) throws EmpregadoNaoExisteException {
        Optional<Empregado> empregado = empregadoRepository.findById(id);
        if (!empregado.isPresent())
            throw new EmpregadoNaoExisteException(id.toString());

        return empregado.get();
    }

    // função para criar empregados
    @Override
    public void createNewEmpregado(CadastrarEmpregadoDTO cdto) {
        Empregado nEmpregado = Empregado.fromDTO(cdto);

        if (nEmpregado.getTipoPagamento() == PagamentoEnum.PIX) {
            nEmpregado.setPix(cdto.getPix());
        } else if (nEmpregado.getTipoPagamento() == PagamentoEnum.CONTA) {
            nEmpregado.setAgencia(cdto.getAgencia());
            nEmpregado.setBanco(cdto.getBanco());
            nEmpregado.setConta(cdto.getConta());
            nEmpregado.setOperacao(cdto.getOperacao());
        }

        empregadoRepository.save(nEmpregado);
    }

    // função para pegar todos os empregados
    @Override
    public List<EmpregadoSendDTO> getAllEmpregados() {
        List<EmpregadoSendDTO> empregados = new ArrayList<>();

        Iterator<Empregado> empre = empregadoRepository.findAll().iterator();
        while (empre.hasNext()) {
            Empregado empregado = empre.next();
            empregados.add(empregado.toSendDTO());
        }

        return empregados;
    }

    // função para editar empregado by id
    @Override
    public void editEmpregado(EditarEmpregadoDTO editarEmpregadoDTO) throws UsuarioNaoExisteException {
        Optional<Empregado> empregado = empregadoRepository.findById(editarEmpregadoDTO.getId());

        if (!empregado.isPresent())
            throw new UsuarioNaoExisteException("selecionado para edicao");

        empregado.get().instancefromEditarDto(editarEmpregadoDTO);
        
        empregadoRepository.save(empregado.get());
    }

    // função para deletar empregado
    @Override
    public void deleteEmpregado(Integer id) {
        empregadoRepository.deleteById(id);
    }
}
