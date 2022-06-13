package com.flashpoint.ControlePonto.model.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.flashpoint.ControlePonto.dto.client.CadastrarPontosDiariosDTO;
import com.flashpoint.ControlePonto.dto.send.PontoDiarioSendDTO;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "data", "empregado_id" }) })
public class PontosDiarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate data;
    private String hora_extra_50;
    private String hora_extra_100;
    private boolean presente;

    @ManyToOne
    private Empregado empregado;

    public PontosDiarios() {

    }

    public PontosDiarios(Empregado empregado, LocalDate data, String hora_extra_50, String hora_extra_100,
            boolean presente) {
        this.empregado = empregado;
        this.data = data;
        this.hora_extra_50 = hora_extra_50;
        this.hora_extra_100 = hora_extra_100;
        this.presente = presente;
    }

    public static PontosDiarios fromDTO(CadastrarPontosDiariosDTO cadastrar, Empregado empregado, LocalDate data) {
        return new PontosDiarios(empregado, data, cadastrar.getHora_extra_50(), cadastrar.getHora_extra_100(),
                cadastrar.getPresente());
    }

    public PontoDiarioSendDTO toSendDTO() {
        return new PontoDiarioSendDTO(data, hora_extra_50, hora_extra_100, empregado.toSendDTO(), presente);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Empregado getEmpregado() {
        return empregado;
    }

    public void setEmpregado(Empregado empregado) {
        this.empregado = empregado;
        empregado.addPontoDiario(this);
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getHora_extra_50() {
        return hora_extra_50;
    }

    public void setHora_extra_50(String hora_extra_50) {
        this.hora_extra_50 = hora_extra_50;
    }

    public String getHora_extra_100() {
        return hora_extra_100;
    }

    public void setHora_extra_100(String hora_extra_100) {
        this.hora_extra_100 = hora_extra_100;
    }

    public boolean getPresente() {
        return presente;
    }

    public void setPresente(boolean presente) {
        this.presente = presente;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((empregado == null) ? 0 : empregado.hashCode());
        result = prime * result + ((hora_extra_100 == null) ? 0 : hora_extra_100.hashCode());
        result = prime * result + ((hora_extra_50 == null) ? 0 : hora_extra_50.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + (presente ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PontosDiarios other = (PontosDiarios) obj;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        if (empregado == null) {
            if (other.empregado != null)
                return false;
        } else if (!empregado.equals(other.empregado))
            return false;
        if (hora_extra_100 == null) {
            if (other.hora_extra_100 != null)
                return false;
        } else if (!hora_extra_100.equals(other.hora_extra_100))
            return false;
        if (hora_extra_50 == null) {
            if (other.hora_extra_50 != null)
                return false;
        } else if (!hora_extra_50.equals(other.hora_extra_50))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (presente != other.presente)
            return false;
        return true;
    }

}
