package com.flashpoint.ControlePonto.repository;

import java.util.Optional;

import com.flashpoint.ControlePonto.model.entities.Empregado;

import org.springframework.data.repository.CrudRepository;

public interface EmpregadoRepository extends CrudRepository<Empregado, Integer> {
    public Optional<Empregado> findByCpf(String cpf);
}
