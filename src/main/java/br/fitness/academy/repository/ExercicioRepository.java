package br.fitness.academy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.fitness.academy.model.Exercicio;

public interface ExercicioRepository extends JpaRepository<Exercicio,Long>{
	public List<Exercicio> findByNome(String nome);
	public List<Exercicio> findByCarga(String carga);
}
