package br.fitness.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.fitness.academy.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>{
	public Funcionario findByMatricula(String matricula);
}
