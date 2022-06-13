package com.flashpoint.ControlePonto.dto.client;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.flashpoint.ControlePonto.enumerators.PagamentoEnum;
import com.flashpoint.ControlePonto.enumerators.VinculoEnum;

public class EditarEmpregadoDTO {
    @NotNull(message = "Erro na edição do funcionário: valor em branco/nulo (id)")
    private Integer id;

    @NotBlank(message = "Erro na edição do funcionário: valor em branco/nulo (nome)")
    private String nome;

    @NotBlank(message = "Erro na edição do funcionário: valor em branco/nulo (rg)")
    private String rg;

    @NotBlank(message = "Erro na edição do funcionário: valor em branco/nulo (cpf)")
    private String cpf;

    private String pis;

    @NotBlank(message = "Erro na edição do funcionário: valor em branco/nulo (celular)")
    private String celular;

    @NotBlank(message = "Erro na edição do funcionário: valor em branco/nulo (cargo)")
    private String cargo;

    @NotNull(message = "Erro na edição do funcionário: valor nulo (vinculo)")
    private VinculoEnum vinculo;

    @NotNull(message = "Erro na edição do funcionário: valor nulo (valor em dinheiro)")
    @Min(value = 0, message = "Erro na edição do funcionário: valor menor que 0 (valor em dinheiro)")
    private Double valor;

    @NotNull(message = "Erro na edição do funcionário: valor nulo (tipo de pagamento)")
    private PagamentoEnum pagamento;

    private String pix;

    private String banco;
    private String agencia;
    private String conta;
    private String operacao;

    public EditarEmpregadoDTO() {
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

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public PagamentoEnum getPagamento() {
        return pagamento;
    }

    public void setPagamento(PagamentoEnum pagamento) {
        this.pagamento = pagamento;
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
