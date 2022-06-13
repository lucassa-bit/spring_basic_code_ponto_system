package com.flashpoint.ControlePonto.service.controleHoras;

import java.util.List;

import com.flashpoint.ControlePonto.dto.client.CadastrarEmpregadoDTO;
import com.flashpoint.ControlePonto.dto.client.EditarEmpregadoDTO;
import com.flashpoint.ControlePonto.dto.send.EmpregadoSendDTO;
import com.flashpoint.ControlePonto.error.Login.UsuarioNaoExisteException;
import com.flashpoint.ControlePonto.error.empregado.EmpregadoNaoExisteException;
import com.flashpoint.ControlePonto.model.entities.Empregado;

public interface EmpregadoService {
    public Empregado getEmpregadoById(Integer id) throws EmpregadoNaoExisteException;
    public List<EmpregadoSendDTO> getAllEmpregados();
    public void createNewEmpregado(CadastrarEmpregadoDTO cdto);
    public void editEmpregado(EditarEmpregadoDTO editarEmpregadoDTO) throws UsuarioNaoExisteException;
    public void deleteEmpregado(Integer id);
}
