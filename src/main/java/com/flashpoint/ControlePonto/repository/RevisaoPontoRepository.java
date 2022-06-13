package com.flashpoint.ControlePonto.repository;

import java.time.LocalDate;
import java.util.Optional;

import com.flashpoint.ControlePonto.model.entities.RevisaoPonto;

import org.springframework.data.repository.CrudRepository;

public interface RevisaoPontoRepository extends CrudRepository<RevisaoPonto, Integer> {
    public Optional<RevisaoPonto> findByData(LocalDate data);
    public void findAllByData(LocalDate data);
}
