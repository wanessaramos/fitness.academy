package br.fitness.academy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.fitness.academy.model. Mensalidade;

public interface MensalidadeRepository extends JpaRepository< Mensalidade,Long>{
	public List<Mensalidade> findByMes(String mes);
}
