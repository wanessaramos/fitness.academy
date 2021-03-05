package br.fitness.academy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.fitness.academy.model.Grupo;
import br.fitness.academy.model.Usuario;

public interface GrupoRepository extends JpaRepository<Grupo, Long>{
	
	//List<Grupo> findByUsuariosIn(Usuario usuario);
}
