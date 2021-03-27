package br.fitness.academy.model;

import java.sql.Date;
import javax.persistence.*;

@Entity
@Table(name="pagamento")
public class Pagamento {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable=false)
	private Date entrega;
	
	@Column(nullable=false,length=10)
	private String status;
	
	@Column(nullable=false)
	private double valor;
	
	@Column(nullable=false,length=100)
	private String descricao;
	
	public Pagamento() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Pagamento(Date entrega, String status, double valor, String descricao) {
		super();
		this.entrega = entrega;
		this.status = status;
		this.valor = valor;
		this.descricao = descricao;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getEntrega() {
		return entrega;
	}

	public void setEntrega(Date entrega) {
		this.entrega = entrega;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return "Pagamento [id=" + id + ", entrega=" + entrega + ", status=" + status + ", valor=" + valor
				+ ", descricao=" + descricao + "]";
	}

}
