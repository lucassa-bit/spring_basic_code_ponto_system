package com.flashpoint.ControlePonto.service.controleHoras;

import java.io.IOException;
import java.util.List;

import com.flashpoint.ControlePonto.error.Data.DataRangeException;
import com.flashpoint.ControlePonto.error.empregado.EmpregadoNaoExisteException;

public interface RelatorioService {
    public void createCSV(List<Integer> cadastrarRelatoriosDTO, String data_inicial,
            String data_final) throws IOException, DataRangeException, EmpregadoNaoExisteException;
}
