package br.fitness.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.fitness.academy.model.SenhaTemporaria;
import br.fitness.academy.model.Usuario;

public interface SenhaTemporariaRepository extends JpaRepository<SenhaTemporaria, Long>{
	public  SenhaTemporaria findByUsuario(Usuario usuario);
}
