package br.fitness.academy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.fitness.academy.model.Grupo;
import br.fitness.academy.model.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Long>{
	
	//List<Permissao> findByGruposIn(Grupo grupo);
}
