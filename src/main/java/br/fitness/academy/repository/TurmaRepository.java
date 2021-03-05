package br.fitness.academy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.fitness.academy.model.Turma;

public interface TurmaRepository extends JpaRepository<Turma,Long>{
	public List<Turma> findByNome(String nome);
}
