package br.fitness.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.fitness.academy.model.Turno;

public interface TurnoRepository extends JpaRepository<Turno,Long>{
	public Turno findByNome(String nome);
}
