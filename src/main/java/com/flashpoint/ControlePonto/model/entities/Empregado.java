package com.flashpoint.ControlePonto.model.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flashpoint.ControlePonto.dto.client.CadastrarEmpregadoDTO;
import com.flashpoint.ControlePonto.dto.client.EditarEmpregadoDTO;
import com.flashpoint.ControlePonto.dto.send.EmpregadoSendDTO;
import com.flashpoint.ControlePonto.enumerators.PagamentoEnum;
import com.flashpoint.ControlePonto.enumerators.VinculoEnum;

@Entity
public class Empregado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    @Column(unique = true)
    private String rg;
    @Column(unique = true)
    private String cpf;
    @Column(unique = true)
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

    @OneToMany(mappedBy = "empregado", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PontosDiarios> pontosFuncionario;

    public Empregado() {

    }

    public Empregado(String nome, String cargo, VinculoEnum vinculo, Double valorPago, String rg, String cpf,
            String pis, String celular, PagamentoEnum tipoPagamento) {
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

    public void addPontoDiario(PontosDiarios ponto) {
        if (pontosFuncionario == null) {
            pontosFuncionario = new ArrayList<>();
        }

        pontosFuncionario.add(ponto);
    }

    public static Empregado fromDTO(CadastrarEmpregadoDTO cdto) {
        return new Empregado(cdto.getNome(), cdto.getCargo(), cdto.getVinculo(), cdto.getValor(),
                cdto.getRg(), cdto.getCpf(), cdto.getPis(), cdto.getCelular(), cdto.getPagamento());
    }

    public EmpregadoSendDTO toSendDTO() {
        EmpregadoSendDTO sDto = new EmpregadoSendDTO(id, nome, cargo, vinculo, valorPago, rg, cpf, pis, celular,
                tipoPagamento);

        if (getTipoPagamento() == PagamentoEnum.PIX) {
            sDto.setPix(getPix());
        } else {
            sDto.setAgencia(getAgencia());
            sDto.setBanco(getBanco());
            sDto.setOperacao(getOperacao());
            sDto.setConta(getConta());
        }
        return sDto;
    }

    public static Empregado fromSendDTO(EmpregadoSendDTO empregadoSendDTO) {
        Empregado empregado = new Empregado(empregadoSendDTO.getNome(), empregadoSendDTO.getCargo(),
                empregadoSendDTO.getVinculo(), empregadoSendDTO.getValorPago(),
                empregadoSendDTO.getRg(), empregadoSendDTO.getCpf(), empregadoSendDTO.getPis(),
                empregadoSendDTO.getCelular(), empregadoSendDTO.getTipoPagamento());

        if (empregadoSendDTO.getTipoPagamento() == PagamentoEnum.PIX) {
            empregado.setPix(empregadoSendDTO.getPix());
        } else {
            empregado.setAgencia(empregadoSendDTO.getAgencia());
            empregado.setBanco(empregadoSendDTO.getBanco());
            empregado.setOperacao(empregadoSendDTO.getOperacao());
            empregado.setConta(empregadoSendDTO.getConta());
        }
        return empregado;
    }

    public static Empregado fromEditarDto(EditarEmpregadoDTO editarEmpregadoDTO) {
        Empregado empregado = new Empregado(editarEmpregadoDTO.getNome(), editarEmpregadoDTO.getCargo(),
                editarEmpregadoDTO.getVinculo(), editarEmpregadoDTO.getValor(),
                editarEmpregadoDTO.getRg(), editarEmpregadoDTO.getCpf(), editarEmpregadoDTO.getPis(),
                editarEmpregadoDTO.getCelular(), editarEmpregadoDTO.getPagamento());

        if (editarEmpregadoDTO.getPagamento() == PagamentoEnum.PIX) {
            empregado.setPix(editarEmpregadoDTO.getPix());
        } else {
            empregado.setAgencia(editarEmpregadoDTO.getAgencia());
            empregado.setBanco(editarEmpregadoDTO.getBanco());
            empregado.setOperacao(editarEmpregadoDTO.getOperacao());
            empregado.setConta(editarEmpregadoDTO.getConta());
        }
        return empregado;
    }

    public void instancefromEditarDto(EditarEmpregadoDTO editarEmpregadoDTO) {
        setNome(editarEmpregadoDTO.getNome());
        setCargo(editarEmpregadoDTO.getCargo());
        setVinculo(editarEmpregadoDTO.getVinculo());
        setValorPago(editarEmpregadoDTO.getValor());
        setRg(editarEmpregadoDTO.getRg());
        setCpf(editarEmpregadoDTO.getCpf());
        setPis(editarEmpregadoDTO.getPis());
        setCelular(editarEmpregadoDTO.getCelular());
        setTipoPagamento(editarEmpregadoDTO.getPagamento());

        if (editarEmpregadoDTO.getPagamento() == PagamentoEnum.PIX) {
            setPix(editarEmpregadoDTO.getPix());
        } else {
            setAgencia(editarEmpregadoDTO.getAgencia());
            setBanco(editarEmpregadoDTO.getBanco());
            setOperacao(editarEmpregadoDTO.getOperacao());
            setConta(editarEmpregadoDTO.getConta());
        }
    }

    // ------------------------------------Getters/Setters--------------------------------------
    // \\

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

    public List<PontosDiarios> getPontosFuncionario() {
        return pontosFuncionario;
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

    public void setPontosFuncionario(List<PontosDiarios> pontosFuncionario) {
        this.pontosFuncionario = pontosFuncionario;
    }
}
