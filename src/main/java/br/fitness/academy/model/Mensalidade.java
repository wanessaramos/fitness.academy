package br.fitness.academy.model;

import java.sql.Date;
import javax.persistence.*;

@Entity
@Table(name="mensalidade")
public class Mensalidade {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable=false,length=25)
	private String mes;
	
	@Column(nullable=false)
	private Date vencimento;
	
	@Column(nullable=false,length=10)
	private String status;
	
	@Column(nullable=false)
	private double valor;
	
	public Mensalidade() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Mensalidade(String mes, Date vencimento, String status, double valor) {
		this.mes = mes;
		this.vencimento = vencimento;
		this.status = status;
		this.valor = valor;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public Date getVencimento() {
		return vencimento;
	}

	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
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

	@Override
	public String toString() {
		return "Mensalidade [id=" + id + ", mes=" + mes + ", vencimento=" + vencimento + ", status=" + status
				+ ", valor=" + valor + "]";
	}
	
}

