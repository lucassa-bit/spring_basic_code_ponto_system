package com.flashpoint.ControlePonto.repository;

import java.time.LocalDate;
import java.util.Optional;

import com.flashpoint.ControlePonto.model.entities.Empregado;
import com.flashpoint.ControlePonto.model.entities.PontosDiarios;

import org.springframework.data.repository.CrudRepository;

public interface PontosDiariosRepository extends CrudRepository<PontosDiarios, Integer> {
    public Optional<PontosDiarios> findByDataAndEmpregado(LocalDate data, Empregado empregado);
    public boolean existsByData(LocalDate data);
    public boolean existsByDataAndEmpregado(LocalDate data, Empregado empregado);
    public Iterable<PontosDiarios> findAllByEmpregado(Empregado empregado);
    public boolean existsById(Integer id);
    public void deleteAllByData(LocalDate data);

}
