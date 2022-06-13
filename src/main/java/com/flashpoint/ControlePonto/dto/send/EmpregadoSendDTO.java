package com.flashpoint.ControlePonto.dto.send;

import com.flashpoint.ControlePonto.enumerators.PagamentoEnum;
import com.flashpoint.ControlePonto.enumerators.VinculoEnum;

public class EmpregadoSendDTO {
    private Integer id;
    private String nome;
    private String rg;
    private String cpf;
    private String pis;
    private String celular;
    private String cargo;

    private VinculoEnum vinculo;
    private Double valorPago;
    
    private PagamentoEnum tipoPagamento;

    private String pix;
    
    private String banco;
    private String agencia;
    private String conta;
    private String operacao;

    public EmpregadoSendDTO() {

    }

    public EmpregadoSendDTO(Integer id, String nome, String cargo, VinculoEnum vinculo, Double valorPago, String rg, String cpf,
            String pis, String celular, PagamentoEnum tipoPagamento) {
        this.id = id;
        this.nome = nome;
        this.rg = rg;
        this.cpf = cpf;
        this.pis = pis;
        this.celular = celular;
        this.cargo = cargo;
        this.vinculo = vinculo;
        this.valorPago = valorPago;
        this.tipoPagamento = tipoPagamento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public VinculoEnum getVinculo() {
        return vinculo;
    }

    public void setVinculo(VinculoEnum vinculo) {
        this.vinculo = vinculo;
    }

    public Double getValorPago() {
        return valorPago;
    }

    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }

    public PagamentoEnum getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(PagamentoEnum tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public String getPix() {
        return pix;
    }

    public void setPix(String pix) {
        this.pix = pix;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }
}
