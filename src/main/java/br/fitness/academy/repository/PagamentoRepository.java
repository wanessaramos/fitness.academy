package br.fitness.academy.repository;

import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.fitness.academy.model.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento,Long>{
	public List<Pagamento> findByDescricao(String descricao);
	public List<Pagamento> findByStatus(String status);
	public List<Pagamento> findByEntrega(Date entrega);
}
