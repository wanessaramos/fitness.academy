package br.fitness.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.fitness.academy.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	Usuario findByLogin(String login);
	
}
