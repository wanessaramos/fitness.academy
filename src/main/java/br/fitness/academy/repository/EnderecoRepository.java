package br.fitness.academy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.fitness.academy.model.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco,Long>{
	public List<Endereco> findByBairro(String mes);
}
