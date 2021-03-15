package br.fitness.academy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.fitness.academy.model.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno,Long>{	
	public List<Aluno> findByNome(String nome);
	public Aluno findByMatricula(String matricula);
	public Aluno findByEmail(String email);
}

