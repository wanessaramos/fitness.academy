package br.fitness.academy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.fitness.academy.model.Professor;

public interface ProfessorRepository extends JpaRepository<Professor,Long>{
	public List<Professor> findByNome(String nome);
	public Professor findByMatricula(String matricula);
	public Professor findByEmail(String email);
}
