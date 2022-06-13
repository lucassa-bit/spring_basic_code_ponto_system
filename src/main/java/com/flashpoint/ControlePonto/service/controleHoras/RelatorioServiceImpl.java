package com.flashpoint.ControlePonto.service.controleHoras;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.flashpoint.ControlePonto.enumerators.PagamentoEnum;
import com.flashpoint.ControlePonto.enumerators.VinculoEnum;
import com.flashpoint.ControlePonto.error.Data.DataRangeException;
import com.flashpoint.ControlePonto.error.empregado.EmpregadoNaoExisteException;
import com.flashpoint.ControlePonto.model.entities.Empregado;
import com.flashpoint.ControlePonto.model.entities.PontosDiarios;

import org.apache.poi.ss.usermodel.BorderExtent;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PropertyTemplate;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelatorioServiceImpl implements RelatorioService {

    @Autowired
    private EmpregadoService eService;

    @Autowired
    private PontosDiarioService pService;

    private List<String> funcionarios = new ArrayList<>();
    private List<String> salario = new ArrayList<>();
    private List<String> formaDePagamento = new ArrayList<>();

    private List<LocalDate> getRange(String minimun, String max) throws DataRangeException {
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

    // RECOMENDO SAIR DAQUI E NÃO OLHAR ISSO, CHAMAR O DESENVOLVEDOR MAIS PRÓXIMO
    @Override
    public void createCSV(List<Integer> cadastrarRelatoriosDTO, String data_inicial,
            String data_final)
            throws IOException, DataRangeException, EmpregadoNaoExisteException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        clearAll();

        List<LocalDate> datas = getRange(data_inicial, data_final);
        int posicao = 1;

        for (Integer cadastrarRelatorioDTO : cadastrarRelatoriosDTO) {
            Empregado empregado = eService.getEmpregadoById(cadastrarRelatorioDTO);
            funcionarios.add(empregado.getNome());

            List<PontosDiarios> pontos = pService.getPontosByEmpregado(empregado);

            posicao = firstCollumn(workbook, sheet, posicao);
            posicao = dadosGerais(workbook, sheet, empregado, data_inicial, data_final, posicao);
            posicao = contacaoPontos(workbook, sheet, pontos, datas, empregado, posicao);
        }

        posicao += 2;
        resumoInfos(workbook, sheet, posicao, data_inicial, data_final);

        try (OutputStream fileOut = new FileOutputStream("workbook.xlsx")) {
            workbook.write(fileOut);
        }

        workbook.close();
        workbook.close();
    }

    // Informações gerais dos funcionarios no final do csv
    private void resumoInfos(Workbook workbook, Sheet sheet, int posicao, String data_inicial, String data_final) {
        Row fileira = sheet.createRow(posicao);
        Cell cell = fileira.createCell(0);
        int valorInicial = posicao;

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        cell.setCellValue("Dados de pagamento");

        autoMerge(sheet, posicao, posicao, 0, 7);

        cell.setCellStyle(cellStyle);
        posicao++;

        autoDefiningInfos(sheet, posicao, 0, 3, cellStyle, cellStyle, "Periodo", data_inicial + " à " + data_final);
        posicao++;
        ///////////////////////////////

        fileira = sheet.createRow(posicao);
        cell = fileira.createCell(0);

        cell.setCellValue("Funcionario");

        autoMerge(sheet, posicao, posicao, 0, 2);

        cell.setCellStyle(cellStyle);

        /////////////////////////////////

        cell = fileira.createCell(3);

        cell.setCellValue("Salário");

        cell.setCellStyle(cellStyle);

        //////////////////////////////////

        cell = fileira.createCell(4);

        cell.setCellValue("Forma de pagamento");

        autoMerge(sheet, posicao, posicao, 4, 7);

        cell.setCellStyle(cellStyle);
        posicao++;

        /////////////////////////////////

        for (int index = 0; index < funcionarios.size(); index++, posicao++) {
            fileira = sheet.createRow(posicao);
            cell = fileira.createCell(0);

            cell.setCellValue(funcionarios.get(index));

            autoMerge(sheet, posicao, posicao, 0, 2);

            cell.setCellStyle(cellStyle);

            /////////////////////////////////

            cell = fileira.createCell(3);

            cell.setCellValue(salario.get(index));

            cell.setCellStyle(cellStyle);

            /////////////////////////////////

            cell = fileira.createCell(4);

            cell.setCellValue(formaDePagamento.get(index));

            autoMerge(sheet, posicao, posicao, 4, 7);

            cell.setCellStyle(cellStyle);
        }

        PropertyTemplate pt = new PropertyTemplate();

        pt.drawBorders(new CellRangeAddress(valorInicial, posicao - 1, 0, 7), BorderStyle.THIN, BorderExtent.OUTSIDE);
        pt.drawBorders(new CellRangeAddress(valorInicial, posicao - 1, 0, 7), BorderStyle.THIN, BorderExtent.INSIDE);
        pt.applyBorders(sheet);
    }

    // Coluna de informações básicas
    private int firstCollumn(Workbook workbook, Sheet sheet, int posicao) {
        Row fileira = sheet.createRow(posicao);
        Cell cell = fileira.createCell(0);

        cell.setCellValue("Folha de ponto individual por funcionário");

        CellStyle cellStyle = workbook.createCellStyle();

        autoMerge(sheet, posicao, posicao, 0, 7);

        PropertyTemplate pt = new PropertyTemplate();

        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        cell.setCellStyle(cellStyle);

        pt.drawBorders(new CellRangeAddress(posicao, posicao, 0, 7), BorderStyle.THIN,
                BorderExtent.OUTSIDE);
        pt.applyBorders(sheet);
        return posicao + 2;

    }

    // Dados gerais do cliente
    private int dadosGerais(Workbook workbook, Sheet sheet, Empregado empregado, String data_inicial,
            String data_final, int posicao) {
        int valorInicial = posicao;

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle valueCellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // nome
        autoDefiningInfos(sheet, posicao, 0, 2, valueCellStyle, cellStyle, "Nome:", empregado.getNome());
        posicao++;
        // cargo
        autoDefiningInfos(sheet, posicao, 0, 2, valueCellStyle, cellStyle, "Cargo:", empregado.getCargo());
        posicao++;
        // tipo de vinculo
        autoDefiningInfos(sheet, posicao, 0, 2, valueCellStyle, cellStyle, "Tipo de Vinculo:",
                empregado.getVinculo().toString());
        posicao++;
        // data inicio
        autoDefiningInfos(sheet, posicao, 0, 2, valueCellStyle, cellStyle, "Data Inicio:", data_inicial);
        posicao++;
        // data final
        autoDefiningInfos(sheet, posicao, 0, 2, valueCellStyle, cellStyle, "Data Final:", data_final);

        PropertyTemplate pt = new PropertyTemplate();

        pt.drawBorders(new CellRangeAddress(valorInicial, posicao, 0, 7), BorderStyle.THIN, BorderExtent.OUTSIDE);
        pt.drawBorders(new CellRangeAddress(valorInicial, posicao, 0, 7), BorderStyle.THIN, BorderExtent.INSIDE);
        pt.applyBorders(sheet);

        return posicao + 3;
    }

    private int contacaoPontos(Workbook workbook, Sheet sheet, List<PontosDiarios> pontos, List<LocalDate> datas,
            Empregado empregado, int posicao)
            throws DataRangeException {
        Row fileira = sheet.createRow(posicao);
        Cell cell = fileira.createCell(0);
        int presenca = 0, faltas = 0, posicaoInicial = posicao;
        int horas_extras50 = 0, minutos_extras50 = 0;
        int horas_extras100 = 0, minutos_extras100 = 0;

        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        autoMerge(sheet, posicao, posicao, 0, 1);
        autoMerge(sheet, posicao, posicao, 2, 3);
        autoMerge(sheet, posicao, posicao, 4, 5);
        autoMerge(sheet, posicao, posicao, 6, 7);
        posicao++;

        cell.setCellValue("Dias");
        cell.setCellStyle(style);

        cell = fileira.createCell(2);
        cell.setCellValue("Presença");
        cell.setCellStyle(style);

        cell = fileira.createCell(4);
        cell.setCellValue("Horas extras 50%");
        cell.setCellStyle(style);

        cell = fileira.createCell(6);
        cell.setCellValue("Horas extras 100%");
        cell.setCellStyle(style);

        for (int dataIndex = 0; dataIndex < datas.size(); dataIndex++, posicao++) {
            autoMerge(sheet, posicao, posicao, 0, 1);
            fileira = sheet.createRow(posicao);
            cell = fileira.createCell(0);
            cell.setCellValue((datas.get(dataIndex).getDayOfMonth()) + "");

            PontosDiarios ponto = temPonto(pontos, datas.get(dataIndex));
            if (ponto != null && (!datas.get(dataIndex).getDayOfWeek().equals(DayOfWeek.SATURDAY)
                    && !datas.get(dataIndex).getDayOfWeek().equals(DayOfWeek.SUNDAY))) {

                String horas50 = ponto.getHora_extra_50(), horas100 = ponto.getHora_extra_100();
                autoMerge(sheet, posicao, posicao, 2, 3);
                cell = fileira.createCell(2);
                cell.setCellValue("PRESENTE");
                cell.setCellStyle(style);

                autoMerge(sheet, posicao, posicao, 4, 5);
                cell = fileira.createCell(4);
                cell.setCellValue(horas50);
                if (!horas50.equals("")) {
                    horas_extras50 += Integer.parseInt(horas50.substring(0, horas50.indexOf(":")));
                    minutos_extras50 += Integer.parseInt(horas50.substring(horas50.indexOf(":") + 1, horas50.length()));
                }
                cell.setCellStyle(style);

                autoMerge(sheet, posicao, posicao, 6, 7);
                cell = fileira.createCell(6);
                cell.setCellValue(horas100);
                if (!horas100.equals("")) {
                    horas_extras100 += Integer.parseInt(horas100.substring(0, horas100.indexOf(":")));
                    minutos_extras100 += Integer
                            .parseInt(horas100.substring(horas100.indexOf(":") + 1, horas100.length()));
                }
                cell.setCellStyle(style);

                presenca++;
            } else {
                autoMerge(sheet, posicao, posicao, 2, 3);
                cell = fileira.createCell(2);
                if (datas.get(dataIndex).getDayOfWeek() == DayOfWeek.SATURDAY
                        || datas.get(dataIndex).getDayOfWeek() == DayOfWeek.SUNDAY) {
                    cell.setCellValue("-");
                } else {
                    cell.setCellValue("AUSENTE");
                    faltas++;
                }
                cell.setCellStyle(style);

                autoMerge(sheet, posicao, posicao, 4, 5);
                cell = fileira.createCell(4);
                cell.setCellValue("");
                cell.setCellStyle(style);

                autoMerge(sheet, posicao, posicao, 6, 7);
                cell = fileira.createCell(6);
                cell.setCellValue("");
                cell.setCellStyle(style);

            }
        }

        autoMerge(sheet, posicao, posicao, 0, 1);
        fileira = sheet.createRow(posicao);
        cell = fileira.createCell(0);
        cell.setCellValue("Total");
        cell.setCellStyle(style);

        // total de presenças
        autoMerge(sheet, posicao, posicao, 2, 3);
        cell = fileira.createCell(2);
        cell.setCellValue(presenca + "");
        cell.setCellStyle(style);

        // total de horas extras de 50% e 100%
        autoMerge(sheet, posicao, posicao, 4, 5);
        cell = fileira.createCell(4);

        while (minutos_extras100 >= 60) {
            minutos_extras100 -= 60;
            horas_extras100++;
        }

        while (minutos_extras50 >= 60) {
            minutos_extras50 -= 60;
            horas_extras50++;
        }

        if (horas_extras50 > 0 || minutos_extras50 > 0) {
            cell.setCellValue((horas_extras50 < 10 ? "0" + horas_extras50
                    : horas_extras50) + ":"
                    + (minutos_extras50 == 0 ? minutos_extras50 + "0"
                            : minutos_extras50 < 10 ? "0" + minutos_extras50 : minutos_extras50));
        } else {
            cell.setCellValue("0:00");
        }
        cell.setCellStyle(style);
        autoMerge(sheet, posicao, posicao, 6, 7);
        cell = fileira.createCell(6);
        if (horas_extras100 > 0 || minutos_extras100 > 0) {
            cell.setCellValue((horas_extras100 < 10 ? "0" + horas_extras100
                    : horas_extras100) + ":"
                    + (minutos_extras100 == 0 ? minutos_extras100 + "0"
                            : minutos_extras100 < 10 ? "0" + minutos_extras100 : minutos_extras100));
        } else {
            cell.setCellValue("0:00");
        }
        cell.setCellStyle(style);

        PropertyTemplate pt = new PropertyTemplate();
        pt.drawBorders(new CellRangeAddress(posicaoInicial, posicao, 0, 7), BorderStyle.THIN, BorderExtent.OUTSIDE);
        pt.drawBorders(new CellRangeAddress(posicaoInicial, posicao, 0, 7), BorderStyle.THIN, BorderExtent.INSIDE);
        pt.applyBorders(sheet);
        posicao += 2;

        // pagamentos
        posicao = tipoDePagamento(workbook, sheet, empregado, posicao, presenca, faltas, horas_extras50,
                minutos_extras50, horas_extras100, minutos_extras100);

        return posicao;
    }

    private int tipoDePagamento(Workbook workbook, Sheet sheet, Empregado empregado, int posicao, int presenca,
            int faltas, int horas_extras50, int minutos_extras50, int horas_extras100, int minutos_extras100) {
        String salarioFunc = String.format("R$ %.2f", getSalario(presenca, horas_extras50, minutos_extras50,
                horas_extras100, minutos_extras100, empregado, faltas));

        PropertyTemplate pt = new PropertyTemplate();
        int posicaoInicial = posicao;

        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        autoMerge(sheet, posicao, posicao, 0, 5);
        Row fileira = sheet.createRow(posicao);
        Cell cell = fileira.createCell(0);
        cell.setCellValue("SALÁRIO");
        cell.setCellStyle(style);
        autoMerge(sheet, posicao, posicao, 6, 7);

        cell = fileira.createCell(6);
        cell.setCellValue(salarioFunc);
        salario.add(salarioFunc);
        cell.setCellStyle(style);
        posicao++;

        autoDefiningInfos(sheet, posicao, 0, 6, style, style, "FORMA DE PAGAMENTO",
                empregado.getTipoPagamento().toString());
        posicao++;

        if (empregado.getTipoPagamento().equals(PagamentoEnum.PIX)) {
            autoDefiningInfos(sheet, posicao, 0, 6, style, style, "PIX", empregado.getPix());
            formaDePagamento.add(String.format("Pix: %s", empregado.getPix()));
        } else {
            autoDefiningInfos(sheet, posicao, 0, 2, style, style, "BANCO", empregado.getBanco());
            posicao++;
            autoDefiningInfos(sheet, posicao, 0, 6, style, style, "CONTA", empregado.getConta());
            posicao++;
            autoDefiningInfos(sheet, posicao, 0, 6, style, style, "AGÊNCIA", empregado.getAgencia());
            posicao++;
            autoDefiningInfos(sheet, posicao, 0, 6, style, style, "OPERAÇÃO", empregado.getOperacao());
            formaDePagamento.add(String.format("Banco: %s, Agencia: %s, Conta: %s, Operação: %s",
                    empregado.getBanco(), empregado.getAgencia(), empregado.getConta(), empregado.getOperacao()));
        }

        pt.drawBorders(new CellRangeAddress(posicaoInicial, posicao, 0, 7), BorderStyle.THIN, BorderExtent.OUTSIDE);
        pt.drawBorders(new CellRangeAddress(posicaoInicial, posicao, 0, 7), BorderStyle.THIN, BorderExtent.INSIDE);

        pt.applyBorders(sheet);

        return posicao + 3;
    }

    private void autoDefiningInfos(Sheet sheet, int posicaoColuna, int posicaoLatValor1, int posicaoLatValor2,
            CellStyle valueCellStyle,
            CellStyle cellStyle, String nome, String valor) {
        Row fileira = sheet.createRow(posicaoColuna);
        Cell cell = fileira.createCell(posicaoLatValor1);
        autoMerge(sheet, posicaoColuna, posicaoColuna, posicaoLatValor1, posicaoLatValor2 - 1);
        cell.setCellValue(nome);
        cell.setCellStyle(valueCellStyle);

        cell = fileira.createCell(posicaoLatValor2);
        autoMerge(sheet, posicaoColuna, posicaoColuna, posicaoLatValor2, 7);
        cell.setCellValue(valor);
        cell.setCellStyle(cellStyle);
    }

    private void autoMerge(Sheet sheet, int primeraFileira, int ultimaFileira, int primeiraColuna, int ultimaColuna) {
        CellRangeAddress cellRangeAddress = new CellRangeAddress(primeraFileira, ultimaFileira, primeiraColuna,
                ultimaColuna);
        if (!sheet.getMergedRegions().contains(cellRangeAddress))
            sheet.addMergedRegion(cellRangeAddress);
    }

    private PontosDiarios temPonto(List<PontosDiarios> pontos, LocalDate data) {
        for (PontosDiarios pontosDiarios : pontos) {
            if (pontosDiarios.getData().equals(data))
                return pontosDiarios;
        }
        return null;
    }

    private double getSalario(int presencas, int horas50, int min50, int horas100, int min100, Empregado empregado,
            int faltas) {
        VinculoEnum vinculo = empregado.getVinculo();
        double valorPago = empregado.getValorPago();
        if (vinculo.equals(VinculoEnum.DIARISTA) || vinculo.equals(VinculoEnum.FLUTUANTE)) {
            return presencas * valorPago + (valorPago / 8) * 1.5 * (horas50 + (min50 / 60))
                    + (valorPago / 8) * 2 * (horas100 + (min100 / 60));

        } else if (vinculo.equals(VinculoEnum.LIDER) || vinculo.equals(VinculoEnum.CLT)
                || vinculo.equals(VinculoEnum.MEI)) {
            return valorPago + (valorPago / 120) * 1.5 * (horas50 + (min50 / 60))
                    + (valorPago / 120) * 2 * (horas100 + (min100 / 60))
                    - (faltas * valorPago / 12);
        }
        return 0;
    }

    private void clearAll() {
        funcionarios.clear();
        formaDePagamento.clear();
        salario.clear();
    }
}
