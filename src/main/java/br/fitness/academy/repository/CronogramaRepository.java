package br.fitness.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.fitness.academy.model.Cronograma;

public interface CronogramaRepository extends JpaRepository<Cronograma,Long>{
	//public List<Cronograma> findByCarga(Date data_inicio);
}
