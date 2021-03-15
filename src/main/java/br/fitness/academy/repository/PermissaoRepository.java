package br.fitness.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.fitness.academy.model.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Long>{
	public Permissao findByNome(String nome);
}
